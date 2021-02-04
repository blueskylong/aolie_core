package com.ranranx.aolie.core.datameta.dto;

import javax.persistence.Table;

/**
 * @author xxl
 *  约束信息, 这里表间和表内使用同一个数据, 当需要做约束计算时, 从方案中查询与此表和字段有关系的约束来检查.
 * @date 2020/8/4 16:35
 * @version V0.0.1
 **/
@Table(name = "aolie_dm_constraint")
public class ConstraintDto extends SchemaBaseDto {
    /**
     * 表达式
     */
    private String expression;
    /**
     * 过滤条件
     */
    private String filter;
    /**
     * 条件顺序,只作显示用
     */
    private Integer orderNum;
    /**
     * 约束的说明
     */
    private String memo;
    /**
     * 是不是表内的约束
     */
    private Short isInner;
    /**
     * 禁用
     */
    private Short disabled;

    private Long id;

    /**
     * 处理方式
     */
    private Integer handleType;


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Short getIsInner() {
        return isInner;
    }

    public void setIsInner(Short isInner) {
        this.isInner = isInner;
    }

    public Short getDisabled() {
        return disabled;
    }

    public void setDisabled(Short disabled) {
        this.disabled = disabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }
}
