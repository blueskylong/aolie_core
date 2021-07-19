package com.ranranx.aolie.core.datameta.datamodel.validator;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaParse;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//TODO 公式  ,多表约束 

/**
 * 单表约束检查,这个验证不再使用,约束应该扩展到所有受影响的表
 */
//@Validator
@Deprecated
public class TableConstraintValidator implements IValidator {
    private List<Constraint> lstConstraint;
    private FormulaParse formulaParse;

    @Override
    public String validateField(String fieldName, Object value, Map<String, Object> row,
                                TableInfo tableInfo) {
        for (Constraint cons : this.lstConstraint) {
            String filter = cons.getConstraintDto().getFilter();
            String expression = cons.getConstraintDto().getExpression();
            //先计算条件是不是符合,再计算表达式是不是符合
            if (filter != null) {
                String valueExp = this.formulaParse.transToValue(filter, tableInfo.getTableDto().getTableId(),
                        row, null, this.formulaParse, null, new HashMap<>());
                if (FormulaTools.calcExpresion(valueExp).equals(Boolean.FALSE)) {
                    continue;
                }
            }
            //计算表达式
            String valueExp = this.formulaParse.transToValue(expression, tableInfo.getTableDto().getTableId(),
                    row, null, this.formulaParse, null, new HashMap<>());
            if (FormulaTools.calcExpresion(valueExp).equals(Boolean.FALSE)) {
                String errInfo = cons.getConstraintDto().getMemo();
                if (CommonUtils.isEmpty(errInfo)) {
                    return "检查约束不通过.";
                }
                return errInfo;
            }
        }
        return null;
    }

    /**
     * 此字段是否需要此验证器验证
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public boolean isConcerned(Column col, TableInfo tableInfo) {
        Schema schema = SchemaHolder.getInstance().getSchema(col.getColumnDto().getSchemaId(),
                col.getColumnDto().getVersionCode());
        List<Constraint> columnConstraints = schema.getColumnConstraints(col.getColumnDto().getColumnId());
        if (columnConstraints != null && !columnConstraints.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 取得实例,验证器,可以是单例,有些多例,由验证器自己决定
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public IValidator getInstance(Column col, TableInfo tableInfo) {
        TableConstraintValidator instance = new TableConstraintValidator();
        Schema schema = SchemaHolder.getInstance()
                .getSchema(col.getColumnDto().getSchemaId(),
                        col.getColumnDto().getVersionCode());
        instance.lstConstraint = schema.getColumnConstraints(col.getColumnDto().getColumnId());
        instance.formulaParse = FormulaParse.getInstance(true, schema);
        return instance;

    }

}
