package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.exceptions.InvalidParamException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/8/8 14:00
 **/
public class InsertParam extends OperParam<InsertParam> {


    /**
     * 更新的列信息
     */
    private List<Map<String, Object>> lstRows;


    public List<Map<String, Object>> getLstRows() {
        return lstRows;
    }

    public void setLstRows(List<Map<String, Object>> lstRows) {
        this.lstRows = lstRows;
    }

    public void setObjects(List<?> lstObj, long schemaId) {
        if (this.getTable() == null && lstObj != null && !lstObj.isEmpty()) {
            String tableName = CommonUtils.getTableName(lstObj.get(0).getClass());
            if (!CommonUtils.isEmpty(tableName)) {
                if (schemaId <= 0) {
                    List<TableInfo> tables = SchemaHolder.findTablesByTableName(tableName, SessionUtils.getLoginVersion());
                    //如果有多个则报错
                    if (tables != null && tables.size() > 1) {
                        throw new InvalidParamException("查询到多个表对象");
                    }
                    if (tables != null) {
                        this.setTable(tables.get(0));
                    }
                }
                TableInfo tableInfo = SchemaHolder.findTableByTableName(tableName,
                        schemaId, SessionUtils.getLoginVersion());
                if (tableInfo != null) {
                    this.setTable(tableInfo);
                }

            }
        }
        this.lstRows = CommonUtils.toMapAndConvertToUnderLine(lstObj);
    }

    /**
     * 设置插入的DTO
     *
     * @param dto
     * @param schemaId
     */
    public void setObject(Object dto, long schemaId) {
        setObjects(Arrays.asList(dto), schemaId);
    }

}
