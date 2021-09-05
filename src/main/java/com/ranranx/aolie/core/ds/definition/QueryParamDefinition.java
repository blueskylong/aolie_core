package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.exceptions.IllegalOperatorException;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.Page;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xxl
 * 支持多表的查询定义
 * @version V0.0.1
 * @date 2020/8/7 15:20
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

    private Page page;


    /**
     * 复杂过滤条件
     */
    private List<Criteria> lstCriteria = new ArrayList<>();


    public List<FieldOrder> getLstOrder() {
        return lstOrder;
    }

    /**
     * 直接语句.暂时提供在复杂语句下使用.
     */
    private SqlExp sqlExp;

    public SqlExp getSqlExp() {
        return sqlExp;
    }

    public QueryParamDefinition setSqlExp(SqlExp sqlExp) {
        this.sqlExp = sqlExp;
        return this;
    }

    public QueryParamDefinition setLstOrder(List<FieldOrder> lstOrder) {
        this.lstOrder = lstOrder;
        return this;
    }


    public QueryParamDefinition addOrder(FieldOrder order) {
        if (this.lstOrder == null) {
            this.lstOrder = new ArrayList<>();
        }
        order.setOrder(this.lstOrder.size() + 1);
        this.lstOrder.add(order);
        return this;
    }

    /**
     * 增加一个字段的升序排序,要求在单表情况下使用
     */
    public QueryParamDefinition addOrderField(String field) {
        if (this.tableNames == null || this.tableNames.size() != 1) {
            throw new IllegalOperatorException("'addOrderField'方法不可以在多表,或无表情况下使用");
        }
        FieldOrder fieldOrder = new FieldOrder(this.tableNames.get(0), field, true, 1);
        this.addOrder(fieldOrder);
        return this;
    }


    public List<TableRelation> getLstRelation() {
        return lstRelation;
    }

    public QueryParamDefinition setLstRelation(List<TableRelation> lstRelation) {
        this.lstRelation = lstRelation;
        return this;
    }

    /**
     * 设置DTO代替表名
     *
     * @param lstClass
     */
    public QueryParamDefinition setTableDtos(Class... lstClass) {
        this.tableNames = new ArrayList<>();
        String tableName;
        for (Class clazz : lstClass) {
            tableName = CommonUtils.getTableName(clazz);
            if (CommonUtils.isEmpty(tableName)) {
                throw new InvalidException("指定的类没有@Table注解");

            }
            tableNames.add(tableName);
        }
        return this;
    }


    public List<Criteria> getCriteria() {
        return lstCriteria;
    }

    public Criteria getSingleCriteria() {
        if (lstCriteria.size() > 0) {
            return lstCriteria.get(0);
        }
        return this.appendCriteria();
    }

    public QueryParamDefinition setCriteria(List<Criteria> criteria) {
        this.lstCriteria = criteria;
        return this;
    }

    /**
     * 增加过滤条件
     *
     * @return
     */
    public Criteria appendCriteria() {
        return appendCriteria(null);
    }

    public Criteria appendCriteria(Criteria criteria) {
        if (criteria == null) {
            criteria = new Criteria();
        }
        lstCriteria.add(criteria);
        return criteria;
    }

    public boolean hasCriteria() {
        return lstCriteria != null && !lstCriteria.isEmpty();
    }


    public List<Criteria> getLstCriteria() {
        //这里需要整理一下,对没有表名的设置表名,只针对单表查询
        if (this.tableNames != null && this.tableNames.size() == 1) {
            return setTableNames(this.tableNames.get(0));
        }
        return lstCriteria;
    }

    private List<Criteria> setTableNames(String tableName) {
        if (this.lstCriteria == null || this.lstCriteria.isEmpty()) {
            return this.lstCriteria;
        }
        for (Criteria criteria : this.lstCriteria) {
            if (criteria.isEmpty()) {
                continue;
            }
            criteria.setTableName(tableName);
        }
        return this.lstCriteria;
    }

    public QueryParamDefinition setLstCriteria(List<Criteria> lstCriteria) {
        this.lstCriteria = lstCriteria;
        return this;
    }

    public List<String> getTableNames() {
        if (this.tableNames == null && fields != null && fields.size() > 0) {
            tableNames = new ArrayList<>();
            for (Field field : fields) {
                if (tableNames.indexOf(field.getTableName()) == -1) {
                    tableNames.add(field.getTableName());
                }
            }
        }
        return tableNames;
    }

    public QueryParamDefinition setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
        return this;
    }

    public QueryParamDefinition setTableNames(String... tableName) {

        this.tableNames = Arrays.asList(tableName);
        return this;
    }

    public List<Field> getFields() {
        return fields;
    }

    public QueryParamDefinition setFields(List<Field> fields) {
        this.fields = fields;
        return this;
    }

    public QueryParamDefinition addField(Field field) {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }
        this.fields.add(field);
        return this;
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
//        if (lstCriteria != null && !lstCriteria.isEmpty()) {
//            for (Criteria criteria : lstCriteria) {
//                if (tableNames.indexOf(criteria.) == -1) {
//                    return "条件指定的表名不存在";
//                }
//            }
//        }
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

    public Page getPage() {
        return page;
    }

    public QueryParamDefinition setPage(Page page) {
        this.page = page;
        return this;
    }

}

