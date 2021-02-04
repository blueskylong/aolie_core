package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.SqlExp;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *  更新和插入的参数载体
 * @date 2020/8/6 14:29
 * @version V0.0.1
 **/
public class UpdateParam {
    /**
     * 复杂条件
     */
    private Criteria criteria;
    /**
     * 字段条件
     */
    private Map<String, Object> mapFilter;
    /**
     * 表信息
     */
    private TableInfo table;

    /**
     * 是否只更新有值的列
     */
    private boolean isSelective = false;
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

    /**
     * 更新的列信息
     */
    private List<Map<String, Object>> lstRows;

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public Map<String, Object> getMapFilter() {
        return mapFilter;
    }

    public void setMapFilter(Map<String, Object> mapFilter) {
        this.mapFilter = mapFilter;
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

    public boolean isSelective() {
        return isSelective;
    }

    public void setSelective(boolean selective) {
        isSelective = selective;
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
