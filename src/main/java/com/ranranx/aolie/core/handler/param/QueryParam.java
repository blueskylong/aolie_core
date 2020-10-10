package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.FieldOrder;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

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
     * 视图ID   视图模式条件
     */
    private Long viewId;

    /**
     * 视图展现的方式    视图模式条件
     */
    private Integer displayType;
    /**
     * 复杂条件  表条件
     */
    private Criteria[] criteria;
    /**
     * 字段条件    视图模式条件
     */
    private Map<String, Object> mapFilter;
    /**
     * 表信息   表条件
     */
    private TableInfo[] table;
    /**
     * 分页信息  综合条件
     */
    private Page page;
    /**
     * 排序字段  综合条件
     */
    private List<FieldOrder> lstOrder;

    public Long getViewId() {
        return viewId;
    }

    public void setViewId(Long viewId) {
        this.viewId = viewId;
    }


    public Map<String, Object> getMapFilter() {
        return mapFilter;
    }

    public void setMapFilter(Map<String, Object> mapFilter) {
        this.mapFilter = mapFilter;
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

    public Criteria[] getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria[] criteria) {
        this.criteria = criteria;
    }

    public TableInfo[] getTable() {
        return table;
    }

    public void setTable(TableInfo[] table) {
        this.table = table;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
