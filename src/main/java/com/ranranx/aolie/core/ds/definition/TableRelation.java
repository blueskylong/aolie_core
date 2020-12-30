package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.common.Constants;

/**
 * @Author xxl
 * @Description 用来描述二表中的相等字段信息, 其中左右表/字段在没有主表的情况下没有其它含义,只是区分.
 * @Date 2020/8/8 14:38
 * @Version V0.0.1
 **/
public class TableRelation {

    /**
     * 左边表
     */
    private String tableLeft;
    /**
     * 右边表
     */
    private String tableRight;
    /**
     * 主表名, 用于区分left join,right join 和 inner join
     */
    private String joinType = Constants.JoinType.INNER_JOIN;
    /**
     * 左表关联字段
     */
    private String[] fieldLeft;
    /**
     * 右表关联字段
     */
    private String[] fieldRight;

    public String getTableLeft() {
        return tableLeft;
    }

    public void setTableLeft(String tableLeft) {
        this.tableLeft = tableLeft;
    }

    public String getTableRight() {
        return tableRight;
    }

    public void setTableRight(String tableRight) {
        this.tableRight = tableRight;
    }

    public String[] getFieldLeft() {
        return fieldLeft;
    }

    public void setFieldLeft(String... fieldLeft) {
        this.fieldLeft = fieldLeft;
    }

    public String[] getFieldRight() {
        return fieldRight;
    }

    public void setFieldRight(String... fieldRight) {
        this.fieldRight = fieldRight;
    }

    /**
     * 取得从表名
     *
     * @return
     */
    public String getSlaveTable() {
        if (Constants.JoinType.LEFT_JOIN.equals(joinType)) {
            return tableLeft;
        } else if (Constants.JoinType.RIGHT_JOIN.equals(joinType)) {
            return tableRight;
        }
        return null;
    }

    private boolean isMasterSlave() {
        return Constants.JoinType.LEFT_JOIN.equals(joinType) || Constants.JoinType.RIGHT_JOIN.equals(joinType);
    }

    /**
     * 取得主表字段
     *
     * @return
     */
    public String[] getMasterFields() {
        if (Constants.JoinType.RIGHT_JOIN.equals(joinType)) {
            return fieldRight;
        } else if (Constants.JoinType.LEFT_JOIN.equals(joinType)) {
            return fieldLeft;
        }
        return null;
    }

    /**
     * 取得从表字段
     *
     * @return
     */
    public String[] getSlaveFields() {
        if (Constants.JoinType.LEFT_JOIN.equals(joinType)) {
            return fieldRight;
        } else if (Constants.JoinType.RIGHT_JOIN.equals(joinType)) {
            return fieldLeft;
        }
        return null;
    }

    public String getJoinType() {
        return joinType;
    }

    /**
     * 取得反转的关系类型
     *
     * @return
     */
    public String getReverseJoinType() {
        if (Constants.JoinType.LEFT_JOIN.equals(joinType)) {
            return Constants.JoinType.RIGHT_JOIN;
        } else if (Constants.JoinType.RIGHT_JOIN.equals(joinType)) {
            return Constants.JoinType.LEFT_JOIN;
        }
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }
}
