package com.ranranx.aolie.ds.definition;

import com.ranranx.aolie.common.CommonUtils;
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
     * 查询的表
     */
    private List<String> tableNames;
    /**
     * 需要查询的表列表,如果只是用于分组,则也要放入其中,
     */
    private List<Field> fields;

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


    public List<FieldOrder> getLstOrder() {
        return lstOrder;
    }

    public void setLstOrder(List<FieldOrder> lstOrder) {
        this.lstOrder = lstOrder;
    }


    public List<TableRelation> getLstRelation() {
        return lstRelation;
    }

    public void setLstRelation(List<TableRelation> lstRelation) {
        this.lstRelation = lstRelation;
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


    public List<Criteria> getLstCriteria() {
        return lstCriteria;
    }

    public void setLstCriteria(List<Criteria> lstCriteria) {
        this.lstCriteria = lstCriteria;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    /**
     * 是否需要分组
     *
     * @return
     */
    public boolean isHasGroup() {
        if (fields != null && !fields.isEmpty()) {
            for (Field field : fields) {
                if (field.isGroupType()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 做此简单的检查
     *
     * @return
     */
    public String check() {
        if (this.tableNames == null || tableNames.isEmpty()) {
            return "没有指定查询的表名";
        }
        //
        if (lstCriteria != null && !lstCriteria.isEmpty()) {
            for (Criteria criteria : lstCriteria) {
                if (tableNames.indexOf(criteria.getTableName()) == -1) {
                    return "条件指定的表名不存在";
                }
            }
        }
        if (fields != null && !fields.isEmpty()) {
            for (Field field : fields) {
                if (tableNames.indexOf(field.getTableName()) == -1) {
                    return "字段对应的表名不合法";
                }
                if (CommonUtils.isEmpty(field.getFieldName())) {
                    return "字段名称没有指定";
                }
            }
        }

        if (lstRelation != null && !lstRelation.isEmpty()) {
            for (TableRelation relation : lstRelation) {
                if (tableNames.indexOf(relation.getTableLeft()) == -1 || tableNames.indexOf(relation.getTableRight()) == -1) {
                    return "关系信息指定的表名不存在";
                }
                if (CommonUtils.isEmpty(relation.getFieldLeft()) || CommonUtils.isEmpty(relation.getFieldRight())) {
                    return "关系信息中的字段没有指定完全";
                }
            }
        }
        if (lstOrder != null && !lstOrder.isEmpty()) {
            for (FieldOrder order : lstOrder) {
                if (tableNames.indexOf(order.getTableName()) == -1) {
                    return "排序字段指定的表名不存在";
                }
            }
        }
        return null;

    }
}
