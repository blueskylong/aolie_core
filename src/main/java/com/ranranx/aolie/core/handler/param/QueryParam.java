package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.ds.definition.FieldOrder;
import com.ranranx.aolie.core.ds.definition.SqlExp;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.beans.Transient;
import java.util.ArrayList;
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
    private List<Criteria> lstCriteria;
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

    private Class resultClass;

    @javax.persistence.Transient
    /**
     * 排序字段  综合条件
     */
    private List<FieldOrder> lstOrder;
    private List<Field> fields;
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

    public List<Criteria> getLstCriteria() {
        return lstCriteria;
    }

    public void setLstCriteria(List<Criteria> lstCriteria) {
        this.lstCriteria = lstCriteria;
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

    public void addOrder(FieldOrder order) {
        if (this.lstOrder == null) {
            this.lstOrder = new ArrayList<>();
        }
        order.setOrder(this.lstOrder.size() + 1);
        this.lstOrder.add(order);
    }

    public void addOrders(List<FieldOrder> order) {
        if (order == null || order.isEmpty()) {
            return;
        }
        if (this.lstOrder == null) {
            this.lstOrder = new ArrayList<>();
        }
        this.lstOrder.addAll(order);
    }

    /**
     * 增加过滤条件
     * TODO  这里的条件需要支持字段的表示方式 ,比如EQUALSTO第一个参数可以用字段信息
     *
     * @return
     */
    public Criteria appendCriteria() {
        if (lstCriteria == null) {
            lstCriteria = new ArrayList<>();
        }
        Criteria criteria = new Criteria();
        lstCriteria.add(criteria);
        return criteria;
    }


    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Transient
    public Class getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class resultClass) {
        this.resultClass = resultClass;
    }
}
