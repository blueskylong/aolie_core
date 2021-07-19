package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.Ordered;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.Formula;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaCalculator;
import com.ranranx.aolie.core.datameta.dto.TableDto;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.OperParam;
import com.ranranx.aolie.core.handler.param.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 公式计算, 因为会有跨表的公式,所以需要在保存后计算
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/1/29 0029 9:05
 **/
@DbOperInterceptor
public class FormulaInterceptor implements IOperInterceptor {
    Logger logger = LoggerFactory.getLogger(FormulaInterceptor.class);

    @Autowired
    private HandlerFactory handlerFactory;

    @Override
    public HandleResult afterOper(OperParam param, String handleType,
                                  Map<String, Object> globalParamData, HandleResult result) {

        logger.info("----->拦截器=>公式计算");
        if (!result.isSuccess()) {
            return null;
        }
        List<Object> ids = (List<Object>) globalParamData.get(GetRowIdInterceptor.PARAM_IDS);
        if (ids == null || ids.isEmpty()) {
            return null;
        }

        Stack<Formula> lstFormula = null;
        TableDto tableDto = param.getTable().getTableDto();

        //检查是不是公式回写
        if (param.getControlParam(IOperInterceptor.AFTER_FORMULA_DIRECT_OPER) != null) {
            return null;
        }
        lstFormula = findCalcFormula(tableDto.getTableId(),
                param.getTable().getTableDto().getVersionCode(),
                (List<String>) globalParamData.get(GetRowIdInterceptor.PARAM_UPDATE_FIELDS));

        //取得变动的行数据,循环处理
        List<Map<String, Object>> lstRow = null;
        //如果是删除，则生成主键字段，
        if (handleType.equals(Constants.HandleType.TYPE_DELETE)) {
            lstRow = (List<Map<String, Object>>) globalParamData.get(GetRowIdInterceptor.PARAM_DELETE_ROWS);
        } else {
            lstRow = findTableRows(tableDto.getTableId(),
                    ids, tableDto.getVersionCode());
        }

        //取得外部公式
        Stack<Formula> lstOutFormula = findForeignTableFormulas(tableDto.getTableId(), tableDto.getVersionCode());
        //1.取得所有的关联此表的公式
        if ((lstFormula == null || lstFormula.isEmpty())
                && (lstOutFormula == null || lstOutFormula.isEmpty())) {
            return null;
        }
        //2.计算公式
        Long schemaId = tableDto.getSchemaId();
        String version = tableDto.getVersionCode();
        FormulaCalculator formulaCalculator =
                new FormulaCalculator(SchemaHolder.getInstance().getSchema(schemaId, version), handlerFactory);

        //这里要计算一下变化的表信息,以备后面约束检查
        Map<Long, List<Map<String, Object>>> changeValues = new HashMap<>();
        for (Map<String, Object> row : lstRow) {
            formulaCalculator.calcTableFormulas(lstFormula, tableDto.getTableId()
                    , row, null, null, lstOutFormula,
                    changeValues);
        }
        globalParamData.put(CHANGED_TABLE_ROWS, changeValues);
        return null;
    }

    /**
     * 查询需要计算的本表内的公式
     *
     * @param tableId
     * @param version
     * @param lstField
     * @return
     */
    Stack<Formula> findCalcFormula(long tableId, String version, List<String> lstField) {
        if (lstField == null || lstField.isEmpty()) {
            return findTableFormulas(tableId, version);
        }
        return findFieldNameFormulas(lstField, tableId, version);
    }


    /**
     * 查找指定字段的公式
     *
     * @param lstFieldName
     * @param tableId
     * @param version
     * @return
     */
    private Stack<Formula> findFieldNameFormulas(List<String> lstFieldName, long tableId, String version) {
        TableInfo table = SchemaHolder.getTable(tableId, version);
        Stack<Formula> lstResult = new Stack<>();
        for (String fieldName : lstFieldName) {
            Column col = table.findColumnByName(fieldName);
            if (col == null) {
                continue;
            }
            List<Formula> lstFormula = col.getLstFormula();
            if (lstFormula != null && !lstFormula.isEmpty()) {
                lstResult.addAll(lstFormula);
            }

        }
        return lstResult;
    }

    /**
     * 查询变化的行信息
     *
     * @param tableId
     * @param ids
     * @param version
     * @return
     */
    private List<Map<String, Object>> findTableRows(long tableId, List<Object> ids, String version) {
        QueryParam queryParam = new QueryParam();
        TableInfo tableInfo = SchemaHolder.getTable(tableId, version);
        queryParam.setTable(tableInfo);
        queryParam.appendCriteria().andIn(null, tableInfo.getKeyField(), ids);
        HandleResult result = handlerFactory.handleQuery(queryParam);
        if (result.isSuccess()) {
            return result.getLstData();
        }
        throw new InvalidException("查询失败:" + result.getErr());
    }

    /**
     * 按列查询表中的公式
     *
     * @param tableId
     * @return
     */
    private Map<Long, List<Formula>> findTableColumnFormulas(long tableId, String version) {
        TableInfo table = SchemaHolder.getTable(tableId, version);
        return table.getAllColumnFormula();
    }

    /**
     * 按列查询表中被影响的公式
     *
     * @param tableId
     * @return
     */
    private Stack<Formula> findTableFormulas(long tableId, String version) {
        TableInfo table = SchemaHolder.getTable(tableId, version);
        List<Formula> allFormula = table.getAllFormula();
        Stack<Formula> stack = new Stack<>();
        stack.addAll(allFormula);
        return stack;
    }

    /**
     * 按列查询表外的公式
     *
     * @param tableId
     * @return
     */
    private Stack<Formula> findForeignTableFormulas(long tableId, String version) {

        TableInfo table = SchemaHolder.getTable(tableId, version);
        Map<Long, List<Formula>> colRelationFormula =
                SchemaHolder.getInstance().getSchema(table.getTableDto().getSchemaId(), version)
                        .findColRelationFormula();
        Stack<Formula> lstFormula = new Stack<>();
        List<Formula> colFormulas;
        for (Column column : table.getLstColumn()) {
            colFormulas = colRelationFormula.get(column.getColumnDto().getColumnId());
            if (colFormulas != null && !colFormulas.isEmpty()) {
                for (Formula formula : colFormulas) {
                    if (!lstFormula.contains(formula)) {
                        if (table.findColumn(formula.getFormulaDto().getColumnId()) == null) {
                            lstFormula.add(formula);
                        }

                    }
                }
            }
        }
        return lstFormula;
    }


    /**
     * 是否可以处理
     *
     * @param type
     * @param objExtinfo
     * @return
     */
    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return Constants.HandleType.TYPE_UPDATE.equals(type)
                || Constants.HandleType.TYPE_INSERT.equals(type)
                || Constants.HandleType.TYPE_DELETE.equals(type);
    }

    @Override
    public int getOrder() {
        return Ordered.BASE_ORDER + 30;
    }
}
