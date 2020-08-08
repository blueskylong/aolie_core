package com.ranranx.aolie.ds.definition;

import com.ranranx.aolie.datameta.datamodal.Table;
import com.ranranx.aolie.engine.param.Criteria;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 支持多表的查询定义
 * @Date 2020/8/7 15:20
 * @Version V0.0.1
 **/
public class QuerySqlDefinition {
    private Table table;
    /**
     * 需要查询的表列表
     */
    private String[] fields;
    /**
     * 排序信息
     */
    private List<FieldOrder> lstOrder;
    /**
     * 一般过滤条件
     */
    private Map<String, Object> mapFilter;

    /**
     * 复杂过滤条件
     */
    private Criteria criteria;

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public List<FieldOrder> getLstOrder() {
        return lstOrder;
    }

    public void setLstOrder(List<FieldOrder> lstOrder) {
        this.lstOrder = lstOrder;
    }

    public Map<String, Object> getMapFilter() {
        return mapFilter;
    }

    public void setMapFilter(Map<String, Object> mapFilter) {
        this.mapFilter = mapFilter;
    }
}

