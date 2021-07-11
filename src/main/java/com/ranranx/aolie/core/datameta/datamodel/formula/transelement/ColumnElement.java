package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaTools;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.exceptions.InvalidConfigException;
import com.ranranx.aolie.core.exceptions.UnknownException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 列翻译元素
 * @version V0.0.1
 * @date 2021/1/28 15:10
 **/
@FormulaElementTranslator
public class ColumnElement implements TransElement {
    @Autowired
    HandlerFactory handlerFactory;

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

    /**
     * 翻译成值表达式,支持跨表取数
     *
     * @param curElement
     * @param rowTableId  当前rowData的表ID
     * @param rowData
     * @param schema
     * @param transcenter
     * @param formula     原始公式,如果是公式翻译则提供 ,如果不是,则不提供
     * @return
     */
    @Override
    public String transToValue(String curElement, long rowTableId, Map<String, Object> rowData,
                               Schema schema, TransCenter transcenter, Formula formula) {

        /**
         * 这里要注意,会有临时列,这里是查询不到的
         * @param exp
         */
        String destFieldType = null;
        if (formula != null) {
            //如果指定了公式,则数据的类型,应该由目标字段类型决定
            Column columnDest = SchemaHolder.getColumn(formula.getFormulaDto().getColumnId(), formula.getFormulaDto().getVersionCode());
            destFieldType = columnDest.getColumnDto().getFieldType();
        }
        List<String> columnParams = FormulaTools.getColumnParams(curElement);
        if (columnParams == null || columnParams.isEmpty()) {
            return curElement;
        }
        String fieldType = destFieldType;
        for (String param : columnParams) {
            Column column = SchemaHolder.getColumn(Long.parseLong(param), schema.getSchemaDto().getVersionCode());
            if (destFieldType == null) {
                fieldType = column.getColumnDto().getFieldType();
            }
            TableInfo tableInfo = SchemaHolder.getTable(column.getColumnDto().getTableId(),
                    column.getColumnDto().getVersionCode());

            if (column.getColumnDto().getTableId().equals(rowTableId)) {
                //如果是本表列,则直接从rowData中取数
                curElement = FormulaTools.replaceColumnValueStr(curElement, param,
                        getFieldValue(column, rowData).toString(), fieldType);
            } else {
                //如果不是本表数据,则需要去查询表之间的关系,并查询出来数据
                TableColumnRelation relation = schema.findTableRelation(rowTableId, column.getColumnDto().getTableId());
                String err = checkForeignColRelation(schema,
                        rowTableId,
                        SchemaHolder.getColumn(formula.getFormulaDto().getColumnId(), formula.getFormulaDto().getVersionCode())
                        , tableInfo.getTableDto().getTitle(), relation);
                if (CommonUtils.isNotEmpty(err)) {
                    throw new InvalidConfigException(err);
                }
                //取得关联的ID值
                //查询数据
                Object value = findForeignColValue(relation, column.isNumberColumn(), column,
                        findRowRelationFieldValue(relation, rowTableId, rowData), relation.getDto().getVersionCode());
                if (value == null) {
                    if (isNumberFieldType(fieldType)) {
                        value = 0;
                    } else {
                        value = "''";
                    }
                }
                curElement = FormulaTools.replaceColumnValueStr(curElement, param,
                        value == null ? "" : value.toString(), fieldType);
            }


        }
        return curElement;
    }


    private boolean isNumberFieldType(String fieldType) {
        return DmConstants.FieldType.DECIMAL.equals(fieldType)
                || DmConstants.FieldType.INT.equals(fieldType)
                || DmConstants.FieldType.BINARY.equals(fieldType);
    }

    /**
     * 查询本表中的关联字段的值
     *
     * @param relation
     * @param tableToFind
     * @param row
     * @return
     */
    private Object findRowRelationFieldValue(TableColumnRelation relation, Long tableToFind, Map<String, Object> row) {
        //先确定本表的字段
        Long fieldId = null;
        if (relation.getTableFrom().getTableDto().getTableId().equals(tableToFind)) {
            fieldId = relation.getDto().getFieldFrom();
        } else {
            fieldId = relation.getDto().getFieldTo();
        }
        Column column = SchemaHolder.getColumn(fieldId, relation.getDto().getVersionCode());
        return row.get(column.getColumnDto().getFieldName());
    }

    /**
     * 查询外表列值
     *
     * @param relation
     * @param isNumberValue 是不是数字字段,指的是目标字段,不是查询字段
     * @param queryColumn
     * @param id
     * @param version
     * @return
     */
    private Object findForeignColValue(TableColumnRelation relation,
                                       boolean isNumberValue,
                                       Column queryColumn,
                                       Object id, String version) {
        QueryParam queryParam = new QueryParam();
        TableInfo table = SchemaHolder.getTable(queryColumn.getColumnDto().getTableId(), version);
//查询字段
        queryParam.setTable(table);
        Field field = new Field();
        field.setTableName(table.getTableDto().getTableName());
        field.setFieldName(queryColumn.getColumnDto().getFieldName());
        //数字字段需要分组函数
        if (isNumberValue) {
            field.setGroupType(Constants.GroupType.SUM);
        }
        List<Field> lstField = new ArrayList<>();
        lstField.add(field);
        Column columnFilter = null;
        if (relation.getTableFrom().getTableDto().getTableId().equals(table.getTableDto().getTableId())) {
            columnFilter = SchemaHolder.getColumn(relation.getDto().getFieldFrom(), version);
        } else {
            columnFilter = SchemaHolder.getColumn(relation.getDto().getFieldTo(), version);
        }
        queryParam.appendCriteria().andEqualTo(null,columnFilter.getColumnDto().getFieldName(), id);
        HandleResult result = handlerFactory.handleQuery(queryParam);
        if (result.isSuccess()) {
            List<Map<String, Object>> lstData = result.getLstData();
            if (lstData == null || lstData.isEmpty()) {
                return null;
            }
            return lstData.get(0).get(queryColumn.getColumnDto().getFieldName());
        } else {
            throw new UnknownException(result.getErr());
        }
    }

    /**
     * 检查外表列关系
     */
    private String checkForeignColRelation(Schema schema, Long rowTableId, Column thisColumn,
                                           String foreignTableName,
                                           TableColumnRelation relation) {

        if (relation == null) {
            return "公式引用的二表没有设置关联关系:"
                    + foreignTableName + "  和  "
                    + schema.findTableById(rowTableId).getTableDto().getTitle();
        }
        //如果此字段为非数字字段,那么只支持 一对一关系和多对一关系,,
        //检查关系
        if (!thisColumn.isNumberColumn() && !relation.getDto()
                .getRelationType().equals(Constants.TableRelationType.TYPE_ONE_ONE)) {
            return "非数字字段取数,必须存在一对一或多对一的表关联关系:"
                    + schema.findTableById(rowTableId).getTableDto().getTitle()
                    + foreignTableName + "  和  "
                    + ",取数字段:" + thisColumn.getColumnDto().getTitle();
        }
        return null;
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
