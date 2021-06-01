package com.ranranx.aolie.core.datameta.datamodel.formula;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;
import com.ranranx.aolie.core.exceptions.InvalidConfigException;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.exceptions.UnknownException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import com.ranranx.aolie.core.interceptor.IOperInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author xxl
 * 公式计算器, 目前的设计是一切合法的JS 表达式,业务上要求是 +_*\\/括号运算
 * * 计算的思想:
 * * 一张表一张表的计算,计算完成后,要更新表数据
 * * 记录本表变化影响的其它表(公式),并提供对应的行数据.
 * * 直到所有表都计算完成
 * @version V0.0.1
 * @date 2020/8/13 20:10
 **/
public class FormulaCalculator {
    Logger logger = LoggerFactory.getLogger(FormulaCalculator.class);
    /**
     * 这里设置最多的循环次数,因为公式可能存在相互套用,而且有优先级,如果先计算了优先级高的公式,再计算优先级低的公式时,还要触发优先级低的公式,会存在重复计算的情况.
     * 这种情况出现,会无法判断是出现了循环还是优先级造成的重复,这里就设置一个阈值,超过这个重复次数,就判断是公式设置出现了循环.
     */
    private static final int MAX_REPEAT_TIME = 20;

    private Map<String, List<Formula>> mapFieldToFormula;
    private FormulaParse filterParser;
    private FormulaParse formulaParser;
    private Schema schema;
    private HandlerFactory handlerFactory;

    public FormulaCalculator(Schema schema, HandlerFactory handlerFactory) {
        this.schema = schema;
        this.handlerFactory = handlerFactory;
        filterParser = FormulaParse.getInstance(true, schema);
        formulaParser = FormulaParse.getInstance(false, schema);
    }


    /**
     * 计算一表一行的公式
     *
     * @param lstFormula        当前表所有的待计算公式
     * @param tableId           当前表ID
     * @param row               当前要计算的行数据
     * @param mapHasCalcFormula 已计算过的公式,key: formulaId,value:times,用于判断循环
     * @param mapChangedField   变化过的字段和值,用于收集所有的变化,等本表所有公式都计算结束后,统一更新到表中
     * @param lstForeignFormula 字段变化影响的外部公式,待本表计算更新后,再逐一处理
     * @return
     */
    public void calcTableFormulas(Stack<Formula> lstFormula, Long tableId,
                                  Map<String, Object> row,
                                  Map<Long, Integer> mapHasCalcFormula,
                                  Map<String, Object> mapChangedField,
                                  List<Formula> lstForeignFormula,
                                  Map<Long, List<Map<String, Object>>> changedValues) {
        //记录当前是不是第一批的表,第二批以后的表,值的变化是真实的,而第一个表不真实
        boolean isFirstTable = false;
        logger.info("-->计算表:" + tableId);
        //记录已计算过的公式,防止循环的出现
        if (mapHasCalcFormula == null) {
            isFirstTable = true;
            mapHasCalcFormula = new HashMap<>();
        }
        if (mapChangedField == null) {
            mapChangedField = new HashMap<>();
        }
        if (lstForeignFormula == null) {
            lstForeignFormula = new ArrayList<>();
        }
        if (changedValues == null) {
            changedValues = new HashMap<>();
        }

        //增加表变动记录,这里,目前还不能区分本表的记录有没有变化,因为如果前端计算结果是正确的,
        // 这里计算出的值,与查询出来的值,应该是一致的,所以这里只要参与计算的,都记录下来,以备完全检查约束
        //这是一处可以优化的地方
        if (isFirstTable) {
            CommonUtils.addMapListValue(changedValues, tableId, row);
        }
        while (!lstFormula.isEmpty()) {
            Formula formula = lstFormula.pop();
            //1.1 计算过滤条件是不是满足
            if (formula.getFormulaDto().getFilter() != null) {
                if (!this.calcExpresion(this.filterParser.transToValue(formula.getFormulaDto().getFilter(),
                        tableId, row, this.schema, null, formula), true)
                        .equals(Boolean.FALSE)) {
                    continue;
                }
            }
            //计算前检查循环次数
            Integer times = mapHasCalcFormula.get(formula.getFormulaDto().getFormulaId());
            if (times == null) {
                mapHasCalcFormula.put(formula.getFormulaDto().getFormulaId(), new Integer(1));
            } else if (times >= MAX_REPEAT_TIME) {
                throw new Error("公式[" + formula.getFormulaDto().getMemo() + ":" +
                        formula.getFormulaDto().getFormulaId() + "]存在循环调用.");
            } else {
                mapHasCalcFormula.put(formula.getFormulaDto().getFormulaId(), new Integer(times++));
            }
            //计算此公式
            this.calcOneFormula(row, formula, tableId, mapChangedField, lstFormula, lstForeignFormula);
        }
        //本表公式计算结束
        //更新变化到表中
        if (!mapChangedField.isEmpty()) {
            //增加主键字段值
            String keyField = schema.findTableById(tableId).getKeyField();
            mapChangedField.put(keyField, row.get(keyField));
            saveChange(tableId, mapChangedField, schema.getSchemaDto().getVersionCode());
            if (!isFirstTable) {
                CommonUtils.addMapListValue(changedValues, tableId, row);
            }
            //保存后清除变化数据
            mapChangedField.clear();

        }


        if (lstForeignFormula.isEmpty()) {
            return;
        }
        //收集受影响的外表数据,进入递归
        Stack<Formula> formulas;
        Long foreignTableId;
        Map<String, Object> foreignRowData;
        while (true) {
            //如果没有外部公式,则结束计算
            if (lstForeignFormula.isEmpty()) {
                return;
            }
            formulas = pickOneTableFormula(lstForeignFormula);
            foreignTableId = getFormulaTableId(formulas.get(0));
            foreignRowData = findForeignRow(tableId, foreignTableId,
                    row, formulas.get(0).getFormulaDto().getVersionCode());
            if (foreignRowData == null) {
                //如果此外表的数据还不存在,则不需要计算
                continue;
            } else {
                break;
            }
        }
        //进入递归
        calcTableFormulas(formulas, foreignTableId, foreignRowData, mapHasCalcFormula,
                mapChangedField, lstForeignFormula, changedValues);
    }

    private long getFormulaTableId(Formula formula) {
        Long columnId = formula.getFormulaDto().getColumnId();
        Column column = SchemaHolder.getColumn(columnId, formula.getFormulaDto().getVersionCode());
        return column.getColumnDto().getTableId();
    }

    /**
     * 依据二表关系,查询另一个表的一行数据,如果出现多行,则为错误
     *
     * @param tableIdFrom 当前表ID
     * @param tableIdTo   外键表ID
     * @param mapFromRow  本表行数据
     * @return 外表行数据
     */
    private Map<String, Object> findForeignRow(Long tableIdFrom, long tableIdTo,
                                               Map<String, Object> mapFromRow, String version) {
        TableInfo table = SchemaHolder.getTable(tableIdFrom, version);
        Schema schema = SchemaHolder.getInstance().getSchema(table.getTableDto().getSchemaId(), version);
        TableColumnRelation tableRelation = schema.findTableRelation(tableIdFrom, tableIdTo);
        if (tableRelation == null) {
            throw new InvalidConfigException("查询外表数据出错,表间不存在关联关系:" + tableIdFrom + "=>" + tableIdTo);
        }
        //查询字段的对应关系,这里就不判断是关系的正确性,只查找
        Long fieldFrom, fieldTo;
        if (tableIdFrom.equals(tableRelation.getTableFrom().getTableDto().getTableId())) {
            fieldFrom = tableRelation.getDto().getFieldFrom();
            fieldTo = tableRelation.getDto().getFieldTo();
        } else {
            fieldTo = tableRelation.getDto().getFieldFrom();
            fieldFrom = tableRelation.getDto().getFieldTo();
        }
        //根据对应关系查询外表数据
        List<Map<String, Object>> lstRow = queryForeignRows(SchemaHolder.getColumn(fieldFrom, version),
                SchemaHolder.getColumn(fieldTo, version), mapFromRow, version);
        if (lstRow != null && lstRow.size() != 1) {
            throw new InvalidConfigException("外表公式数据查询,多于一个或不存在:" +
                    "" + table.getTableDto().getTitle()
                    + "=>" + SchemaHolder.getTable(
                    SchemaHolder.getColumn(fieldTo, version).getColumnDto().getTableId(), version)
                    .getTableDto().getTitle());
        }
        return lstRow.get(0);
    }

    /**
     * 检一张表的所有公式
     *
     * @param lstFormula
     * @return
     */
    private Stack<Formula> pickOneTableFormula(List<Formula> lstFormula) {
        Stack<Formula> stack = new Stack<>();
        //取得第一个公式所在的表
        Long colId = lstFormula.get(0).getFormulaDto().getColumnId();
        String version = lstFormula.get(0).getFormulaDto().getVersionCode();
        Long tableId = SchemaHolder.getColumn(colId, version).getColumnDto().getTableId();
        TableInfo tableInfo = SchemaHolder.getTable(tableId, version);
        Formula formula;
        for (int i = lstFormula.size() - 1; i >= 0; i--) {
            formula = lstFormula.get(i);
            if (tableInfo.findColumn(formula.getFormulaDto().getColumnId()) != null) {
                stack.push(lstFormula.remove(i));
            }
        }
        return stack;
    }


    /**
     * 保存表一行所有的变化
     */
    private void saveChange(Long tableId, Map<String, Object> mapRow, String version) {
        UpdateParam updateParam = new UpdateParam();
        List<Map<String, Object>> lst = new ArrayList<>();
        lst.add(mapRow);
        updateParam.setLstRows(lst);
        updateParam.setTable(SchemaHolder.getTable(tableId, version));
        updateParam.setSelective(true);

        updateParam.addControlParam(IOperInterceptor.AFTER_FORMULA_DIRECT_OPER, 1);
        handlerFactory.handleUpdate(updateParam);
    }

    /**
     * 计算一个公式
     *
     * @param row               行数据
     * @param formula           公式
     * @param tableId           本表ID
     * @param mapChangedField   本表变化字段
     * @param lstFormula        待计算堆,如果本次计算,涉及值的变化,则要将受影响的本表公式放入栈中
     * @param lstForeignFormula 受影响的外部表(公式)
     */
    private void calcOneFormula(Map<String, Object> row,
                                Formula formula,
                                Long tableId,
                                Map<String, Object> mapChangedField,
                                Stack<Formula> lstFormula,
                                List<Formula> lstForeignFormula) {
        //1.2 计算公式
        String formulaStr = formulaParser.transToValue(formula.getFormulaDto().getFormula(), tableId,
                row, this.schema, null, formula);
        Object value = this.calcExpresion(formulaStr, false);
        //比较一下此值有没有变化,如果变化了,则要将此变化进一步递归下去
        Column column = SchemaHolder.getColumn(formula.getFormulaDto().getColumnId(), formula.getFormulaDto().getVersionCode());
        String fieldName = column.getColumnDto().getFieldName();
        logger.info("@@@@表达式:" + formula.getFormulaDto().getFormula()
                + " 翻译值表达式:" + formulaStr + "  结果:" + value);
        Object oldValue = row.get(fieldName);
        //所有涉外表的公式,不管有没有变化,都要重新计算一次,如果不加入,则没有机会计算到外部表,因为在前端,基本是可以保证
        // 所有的计算都是正确的,这里的表像是,所有字段都没有变化,只有在很特殊的情况下,比如公式引用了外表,则可能和前端的计算值不一致
        // .这里可以做的更好,提前保存涉外表字段的值,在此比较
        List<Formula>[] lstFormulaArr = findColInnerAndOuterRelationFormula(schema, column);
        //先把外表加入
        if (lstFormulaArr != null) {
            addFormula(lstFormulaArr[1], lstForeignFormula);
        }

        if (oldValue == null) {
            if (value == null || CommonUtils.isEmpty(value)) {
                return;
            }
        } else {
            if (oldValue == value || oldValue.equals(value)) {
                return;
            }
            if ((oldValue instanceof Number) && (value instanceof Number)) {
                if (((Number) oldValue).doubleValue() == ((Number) value).doubleValue()) {
                    return;
                }
            }
        }
        logger.info("@@@@值发生变化:" + oldValue + "=>" + value);
        //处理值变化
        row.put(fieldName, value);
        mapChangedField.put(fieldName, value);
        //查询变化字段所关联的公式,并放入计算公式放到栈中
        if (lstFormulaArr != null) {
            addFormula(lstFormulaArr[0], lstFormula);
        }


    }

    /**
     * 列值变化
     *
     * @param lstFormula
     * @param lstFormula
     */
    private void addFormula(List<Formula> lstFormula, List<Formula> sFormula) {

        if (lstFormula != null && !lstFormula.isEmpty()) {
            for (Formula formula : lstFormula) {
                if (!sFormula.contains(formula)) {
                    sFormula.add(formula);
                }
            }
        }
    }

    private List<Formula>[] findColInnerAndOuterRelationFormula(Schema schema, Column column) {
        List<Formula> formulas = schema.findColRelationFormula().get(column.getColumnDto().getColumnId());
        if (formulas == null || formulas.isEmpty()) {
            return null;
        }
        List<Formula> lstInner = new ArrayList<>();
        List<Formula> lstOuter = new ArrayList<>();
        String version = column.getColumnDto().getVersionCode();
        Long tableId = column.getColumnDto().getTableId();
        for (Formula formula : formulas) {
            Column formulaCol = SchemaHolder.getColumn(formula.getFormulaDto().getColumnId(), version);
            if (tableId.equals(formulaCol.getColumnDto().getTableId())) {
                lstInner.add(formula);
            } else {
                lstOuter.add(formula);
            }
        }
        return new List[]{lstInner, lstOuter};
    }


    private List<Formula> getFieldFormulas(long fieldId) {
        return this.mapFieldToFormula.get(fieldId);
    }

    private Object calcExpresion(String str, boolean isFilter) {
        return FormulaTools.calcExpresion(str);
    }

    /**
     * 查询外表行
     *
     * @return
     */
    private List<Map<String, Object>> queryForeignRows(Column colFrom, Column colTo,
                                                       Map<String, Object> mapFrom, String version) {
        QueryParam queryParam = new QueryParam();
        TableInfo table = SchemaHolder.getTable(colTo.getColumnDto().getTableId(), version);
        //查询字段
        queryParam.setTable(new TableInfo[]{table});
        Object objFilterValue = mapFrom.get(colFrom.getColumnDto().getFieldName());
        if (objFilterValue == null) {
            throw new InvalidParamException("查询主表值不存在:" + colFrom.getColumnDto().getTitle() + "["
                    + colFrom.getColumnDto().getFieldName() + "]");
        }
        queryParam.appendCriteria().andEqualTo(null, colTo.getColumnDto().getFieldName(),
                objFilterValue);
        HandleResult result = handlerFactory.handleQuery(queryParam);
        if (result.isSuccess()) {
            return result.getLstData();
        } else {
            throw new UnknownException(result.getErr());
        }
    }

}
