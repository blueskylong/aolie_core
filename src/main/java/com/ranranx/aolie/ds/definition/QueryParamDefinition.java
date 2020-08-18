package com.ranranx.aolie.ds.definition;

import com.ranranx.aolie.datameta.datamodal.Table;
import com.ranranx.aolie.handler.param.condition.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xxl
 * @Description 支持多表的查询定义
 * @Date 2020/8/7 15:20
 * @Version V0.0.1
 **/
public class QueryParamDefinition {

    /**
     * 需要有带有注解@Table的类.使用此类,则只可以单个表格查询,只可以使用criteria[0],lstOrder[0]作为参数
     */
    private Class<?> clazz;
    /**
     * 查询的表
     */
    private Table[] table;
    /**
     * 需要查询的表列表
     */
    private Long[] fields;

    /**
     * 版本
     */
    private String version;

    /**
     * 排序信息
     */
    private List<FieldOrder> lstOrder;

    /**
     * 表间关系,多表时传入
     */
    private List<TableRelation> lstRelation;


    /**
     * 复杂过滤条件,针对每张表
     */
    private List<Criteria> lstCriteria = new ArrayList<>();

    public Table[] getTable() {
        return table;
    }

    public void setTable(Table[] table) {
        this.table = table;
    }

    public Long[] getFields() {
        return fields;
    }

    public void setFields(Long[] fields) {
        this.fields = fields;
    }


    public List<FieldOrder> getLstOrder() {
        return lstOrder;
    }

    public void setLstOrder(List<FieldOrder> lstOrder) {
        this.lstOrder = lstOrder;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public List<TableRelation> getLstRelation() {
        return lstRelation;
    }

    public void setLstRelation(List<TableRelation> lstRelation) {
        this.lstRelation = lstRelation;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Criteria> getCriteria() {
        return lstCriteria;
    }

    public Criteria getSingleCriteria() {
        if (lstCriteria.size() > 0) {
            return lstCriteria.get(0);
        }
        return new Criteria();
    }

    public void setCriteria(List<Criteria> criteria) {
        this.lstCriteria = criteria;
    }

    /**
     * 增加过滤条件
     *
     * @return
     */
    public Criteria appendCriteria() {
        Criteria criteria = new Criteria();
        lstCriteria.add(criteria);
        return criteria;
    }

    public boolean hasCriteria() {
        return !lstCriteria.isEmpty();
    }
}

