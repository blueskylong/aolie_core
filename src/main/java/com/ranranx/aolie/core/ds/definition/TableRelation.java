package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;


/**
 * @author xxl
 * 用来描述二表中的相等字段信息, 其中左右表/字段在没有主表的情况下没有其它含义,只是区分.
 * @version V0.0.1
 * @date 2020/8/8 14:38
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

    /**
     * 连接额外条件,放到on 后面
     */
    private List<Criteria> lstCriteria;

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

    @Transient
    public List<Criteria> getLstCriteria() {
        return lstCriteria;
    }

    /**
     * 是否含有条件
     *
     * @return
     */
    public boolean hasWhere() {
        return lstCriteria != null && !lstCriteria.isEmpty();
    }

    public void setLstCriteria(List<Criteria> lstCriteria) {
        this.lstCriteria = lstCriteria;
    }

    @Transient
    public Criteria appendCriteria() {
        if (this.lstCriteria == null) {
            this.lstCriteria = new ArrayList<>();
        }
        Criteria criteria = new Criteria();
        this.lstCriteria.add(criteria);
        return criteria;
    }
}
