package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.SqlExp;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/8 14:00
 * @Version V0.0.1
 **/
public class InsertParam {
    /**
     * 表信息
     */
    private TableInfo table;

    /**
     * 更新的列信息
     */
    private List<Map<String, Object>> lstRows;
    /**
     * 直接语句.暂时提供在复杂语句下使用.
     */
    private SqlExp sqlExp;

    public SqlExp getSqlExp() {
        return sqlExp;
    }

    public void setSqlExp(SqlExp sqlExp) {
        this.sqlExp = sqlExp;
    }

    public TableInfo getTable() {
        return table;
    }

    public void setTable(TableInfo table) {
        this.table = table;
    }

    public List<Map<String, Object>> getLstRows() {
        return lstRows;
    }

    public void setLstRows(List<Map<String, Object>> lstRows) {
        this.lstRows = lstRows;
    }

    public void setObjects(List<?> lstObj, long schemaId) {
        if (table == null && lstObj != null && !lstObj.isEmpty()) {
            String tableName = CommonUtils.getTableName(lstObj.get(0).getClass());
            if (!CommonUtils.isEmpty(tableName)) {
                this.table = SchemaHolder.findTableByTableName(tableName,
                        schemaId, SessionUtils.getLoginVersion());
            }
        }
        this.lstRows = CommonUtils.toMapAndConvertToUnderLine(lstObj);
    }
}
