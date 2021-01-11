package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.ds.dataoperator.IDataOperator;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2021/1/7 0007 20:05
 * @Version V0.0.1
 **/
public class SqlExp {

    private String sql;
    private Map<String, Object> paramValues;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getParamValues() {
        return paramValues;
    }

    public void setParamValues(Map<String, Object> paramValues) {
        this.paramValues = paramValues;
    }

    public Map<String, Object> getExecuteMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(IDataOperator.SQL_PARAM_NAME, this.sql);
        if (paramValues != null && !paramValues.isEmpty()) {
            map.putAll(paramValues);
        }
        return map;
    }

    public void addParam(String key, Object value) {
        if (this.paramValues == null) {
            this.paramValues = new HashMap<>();
        }
        this.paramValues.put(key, value);
    }
}
