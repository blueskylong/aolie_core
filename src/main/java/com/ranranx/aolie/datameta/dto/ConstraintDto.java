package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

import javax.persistence.Table;

/**
 * @Author xxl
 * @Description 约束信息,这里表间和表内使用同一个数据,当需要做约束计算时,从方案中查询与此表和字段有关系的约束来检查.
 * @Date 2020/8/4 16:35
 * @Version V0.0.1
 **/
@Table(name = "aolie_dm_constraint")
public class ConstraintDto extends BaseDto {
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
}
