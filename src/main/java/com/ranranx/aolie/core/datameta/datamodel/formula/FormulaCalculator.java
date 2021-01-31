package com.ranranx.aolie.core.datameta.datamodel.formula;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公式计算器,目前的设计是一切合法的JS 表达式,业务上要求是 +_*\\/括号运算
 */
public class FormulaCalculator {

    private Map<String, List<Formula>> mapFieldToFormula;
    private FormulaParse filterParser;
    private FormulaParse formulaParser;
    private Schema schema;
    private BlockViewer viewerInfo;

    FormulaCalculator(Schema schema, BlockViewer viewerInfo) {
        this.schema = schema;
        this.initFieldFormula();
    }


    //计算,只适合表内公式
    public static Object calc(String formula, Map<String, Object> row, Schema schema) {
        FormulaParse instance = FormulaParse.getInstance(false, schema);
        String valueExp = instance.transToValue(formula, row, schema, null);
        return FormulaTools.calcExpresion(valueExp);
    }

    public static FormulaCalculator getInstance(BlockViewer viewerInfo) {
        Schema schema = SchemaHolder.getInstance().getSchema(viewerInfo.getBlockViewDto().getSchemaId(),
                viewerInfo.getBlockViewDto().getVersionCode());
        return new FormulaCalculator(schema, viewerInfo);
    }

    public Map<String, Object> fieldValueChanged(long fieldId, Map<String, Object> row,
                                                 Map<String, Formula> mapHasCalcFormula
            , Map<String, Object> mapChangedField) {
        //记录已计算过的公式,防止循环的出现
        if (mapHasCalcFormula == null) {
            mapHasCalcFormula = new HashMap<>();
        }
        if (mapChangedField == null) {
            mapChangedField = new HashMap<>();
        }
        //查看此字段没有相关的公式
        List<Formula> lstFormula = this.getFieldFormulas(fieldId);
        if (lstFormula == null || lstFormula.isEmpty()) {
            return mapChangedField;
        }
        for (Formula formula : lstFormula) {
            if (mapHasCalcFormula.containsKey(formula.getFormulaDto().getFormulaId() + "")) {
                throw new Error("公式[" + formula.getFormulaDto().getMemo() + ":" +
                        formula.getFormulaDto().getFormulaId() + "]存在循环调用.");
            }
            mapHasCalcFormula.put(formula.getFormulaDto().getFormulaId() + "", null);
            this.calcOneFormula(row, formula, mapHasCalcFormula, mapChangedField);
        }
        return mapChangedField;

    }

    private List<Formula> getFieldFormulas(long fieldId) {
        return this.mapFieldToFormula.get(fieldId);
    }

    private void calcOneFormula(Map<String, Object> row,
                                Formula formula,
                                Map<String, Formula> mapHasCalcFormula,
                                Map<String, Object> mapChangedField) {
        //1.先计算本公式
        //1.1 计算过滤条件是不是满足
        if (formula.getFormulaDto().getFilter() != null) {
            if (!this.calcExpresion(this.filterParser.transToValue(formula.getFormulaDto().getFilter(), row
                    , this.schema, null), true).equals(Boolean.FALSE)) {
                return;
            }

        }
        //1.2 计算公式
        String formulaStr = this.formulaParser.transToValue(formula.getFormulaDto().getFormula(), row, this.schema, null);
        Object value = this.calcExpresion(formulaStr, false);
        //比较一下此值有没有变化,如果变化了,则要将此变化进一步递归下去
        Column column = SchemaHolder.getColumn(formula.getFormulaDto().getColumnId(), formula.getFormulaDto().getVersionCode());
        String fieldName = column.getColumnDto().getFieldName();
        if (this.viewerInfo.getBlockViewDto().getFieldToCamel() != null
                && this.viewerInfo.getBlockViewDto().getFieldToCamel() == 1) {
            //目标字段
            fieldName = CommonUtils.toCamelStr(fieldName);
        }
        Object oldValue = row.get(fieldName);
        if (oldValue == null) {
            if (value == null) {
                return;
            }
        } else {
            if (oldValue == value) {
                return;
            }
        }
        //触发递归
        row.put(fieldName, value);
        mapChangedField.put(fieldName, value);
        this.fieldValueChanged(column.getColumnDto().getColumnId(), row, mapHasCalcFormula, mapChangedField);
    }

    private Object calcExpresion(String str, boolean isFilter) {
        return FormulaTools.calcExpresion(str);
    }

    /**
     * 分析此视图中公式结构,一个字段关联相应的公式列表
     */
    private void initFieldFormula() {
        this.mapFieldToFormula = new HashMap<>();
        this.filterParser = FormulaParse.getInstance(true, this.schema);
        this.formulaParser = FormulaParse.getInstance(false, this.schema);
        List<Formula> lstFormula = new ArrayList<>();
        List<String> colIds = new ArrayList<>();
        for (Component com : this.viewerInfo.getLstComponent()) {
            lstFormula = com.getColumn().getLstFormula();
            if (lstFormula != null && !lstFormula.isEmpty()) {
                for (Formula formulaInfo : lstFormula) {
                    colIds = FormulaTools.getColumnParams(formulaInfo.getFormulaDto().getFilter());
                    if (colIds != null && colIds.isEmpty()) {
                        for (String colId : colIds) {
                            CommonUtils.addMapListValue(this.mapFieldToFormula, colId, formulaInfo);
                        }
                    }
                    colIds = FormulaTools.getColumnParams(formulaInfo.getFormulaDto().getFormula());
                    if (colIds != null && !colIds.isEmpty()) {
                        for (String colId : colIds) {
                            CommonUtils.addMapListValue(this.mapFieldToFormula, colId, formulaInfo);
                        }
                    }
                }
            }
        }
    }

}
