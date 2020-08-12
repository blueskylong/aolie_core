package com.ranranx.aolie.handler.param;

import com.ranranx.aolie.datameta.datamodal.Table;
import com.ranranx.aolie.ds.definition.FieldOrder;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 查询参数载体, 可以是多种组合, 如果是单表, 则可以使用table和mapFilter组合, 如果使用视图则需要根据视图的设定拆分
 * @Date 2020/8/6 14:28
 * @Version V0.0.1
 **/
public class QueryParam {
    /**
     * 视图ID
     */
    private Long viewId;

    /**
     * 视图展现的方式
     */
    private Integer displayType;
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
    private Table table;
    /**
     * 排序字段
     */
    private List<FieldOrder> lstOrder;

    public Long getViewId() {
        return viewId;
    }

    public void setViewId(Long viewId) {
        this.viewId = viewId;
    }

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

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<FieldOrder> getLstOrder() {
        return lstOrder;
    }

    public void setLstOrder(List<FieldOrder> lstOrder) {
        this.lstOrder = lstOrder;
    }

    public Integer getDisplayType() {
        return displayType;
    }

    public void setDisplayType(Integer displayType) {
        this.displayType = displayType;
    }
}
