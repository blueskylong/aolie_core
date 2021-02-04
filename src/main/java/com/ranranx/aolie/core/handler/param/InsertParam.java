package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.SqlExp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *
 * @date 2020/8/8 14:00
 * @version V0.0.1
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
    /**
     * 增加控制信息,让拦截器使用
     */
    private Map<String, Object> mapControlParam;

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

    public Map<String, Object> getMapControlParam() {
        return mapControlParam;
    }

    public void setMapControlParam(Map<String, Object> mapControlParam) {
        this.mapControlParam = mapControlParam;
    }

    /**
     * 增加一个控制参数
     *
     * @param key
     * @param value
     */
    public void addControlParam(String key, Object value) {
        if (this.mapControlParam == null) {
            this.mapControlParam = new HashMap<>();
        }
        this.mapControlParam.put(key, value);
    }

    /**
     * 取得一个控制参数
     *
     * @param key
     * @return
     */
    public Object getControlParam(String key) {
        if (this.mapControlParam == null) {
            return null;
        }
        return this.mapControlParam.get(key);
    }
}
