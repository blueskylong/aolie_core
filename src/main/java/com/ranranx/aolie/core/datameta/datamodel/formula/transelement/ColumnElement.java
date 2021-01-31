package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaTools;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 列翻译元素
 * @Date 2021/1/28 15:10
 * @Version V0.0.1
 **/
@FormulaElementTranslator
public class ColumnElement implements TransElement {

    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.column;
    }

    @Override
    public String getName() {
        return "列参数";
    }

    @Override
    public String getExpressionCN() {
        return getName();
    }

    @Override
    public int getOrder() {
        return getElementType();
    }

    @Override
    public boolean isMatchCn(String str) {
        return FormulaTools.isColumnParam(str.trim());
    }

    @Override
    public boolean isMatchInner(String str) {
        return FormulaTools.isColumnParam(str.trim());
    }

    @Override
    public String transToCn(String curElement, TransCenter transcenter
    ) {
        System.out.println(getName() + "  matched!");
        /**
         * 这里要注意,会有临时列,这里是查询不到的
         * @param exp
         */

        List<String> columnParams = FormulaTools.getColumnParams(curElement);
        if (columnParams == null || columnParams.isEmpty()) {
            return curElement;
        }
        for (String param : columnParams) {
            //TODO 这里不适宜用SessionUtils.getLoginVersion()
            Column column = SchemaHolder.getColumn(Long.parseLong(param), SessionUtils.getLoginVersion());
            TableInfo tableInfo = SchemaHolder.getTable(column.getColumnDto().getTableId(), column.getColumnDto().getVersionCode());
            curElement = FormulaTools.replaceColumnNameStr(curElement, param,
                    tableInfo.getTableDto().getTitle() + "." + column.getColumnDto().getTitle());
        }
        return curElement;
    }

    @Override
    public String transToInner(String curElement, Schema schema, TransCenter transcenter) {
        System.out.println(getName() + "  matched!");
        List<String> columnParams = FormulaTools.getColumnParams(curElement);
        if (columnParams == null || columnParams.isEmpty()) {
            return curElement;
        }
        for (String paramName : columnParams) {
            String[] names = paramName.split(".");
            if (names.length != 2) {
                throw new Error("列全名不正确");
            }
            TableInfo table = schema.findTableByTitle(names[0]);
            if (table == null) {
                throw new Error("方案[" + schema.getSchemaDto().getSchemaName()
                        + "]中,表不存在[" + names[0] + "]");
            }
            Column column = table.findColumnByColTitle(names[1]);
            if (column == null) {
                throw new Error("方案[" + schema.getSchemaDto().getSchemaName()
                        + "]中,列不存在[" + paramName + "]");
            }
            TableInfo tableInfo = SchemaHolder.getTable(column.getColumnDto().getTableId()
                    , column.getColumnDto().getVersionCode());
            curElement = FormulaTools.replaceColumnNameStr(curElement, paramName,
                    column.getColumnDto().getColumnId() + "");
        }
        return curElement;
    }


    @Override
    public String transToValue(String curElement, Map<String, Object> rowData,
                               Schema schema, TransCenter transcenter) {
        System.out.println(getName() + "  matched!");
        /**
         * 这里要注意,会有临时列,这里是查询不到的
         * @param exp
         */

        List<String> columnParams = FormulaTools.getColumnParams(curElement);
        if (columnParams == null || columnParams.isEmpty()) {
            return curElement;
        }
        for (String param : columnParams) {
            Column column = SchemaHolder.getColumn(Long.parseLong(param), schema.getSchemaDto().getVersionCode());
            TableInfo tableInfo = SchemaHolder.getTable(column.getColumnDto().getTableId(),
                    column.getColumnDto().getVersionCode());

            curElement = FormulaTools.replaceColumnValueStr(curElement, param,
                    getFieldValue(column, rowData).toString(), null);
        }
        return curElement;
    }

    private Object getFieldValue(Column column, Map<String, Object> rowData) {
        Object value = rowData.get(column.getColumnDto().getFieldName());
        if (value == null) {
            if (column.isNumberColumn()) {
                value = new Integer(0);
            } else {
                value = "";
            }
        }
        //给字符串包裹引号
        if (!column.isNumberColumn()) {
            value = "'" + value + "'";
        }
        return value;
    }

    @Override
    public boolean isOnlyForFilter() {
        return false;
    }
}
