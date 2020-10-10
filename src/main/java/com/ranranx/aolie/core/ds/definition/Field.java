package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.common.Constants;

/**
 * @Author xxl
 * @Description  查询用的字段描述
 * @Date 2020/8/18 17:54
 * @Version V0.0.1
 **/
public class Field {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 分组类型
     *
     * @see com.ranranx.aolie.core.common.Constants.GroupType
     */
    private int groupType = Constants.GroupType.NONE;
    /**
     * 排序内容
     *
     * @see com.ranranx.aolie.core.common.Constants.OrderType
     */
    private int orderType = Constants.OrderType.NONE;

    /**
     * 是否显示
     */
    private boolean isShow = true;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    /**
     * 取得字段查询表达式
     *
     * @param alias
     * @return
     */
    public String getSelectExp(String alias) {
        switch (this.groupType) {
            case Constants.GroupType.AVG:
                return getGroupSql("avg", alias, fieldName);
            case Constants.GroupType.COUNT:
                return getGroupSql("count", alias, fieldName);
            case Constants.GroupType.MAX:
                return getGroupSql("max", alias, fieldName);
            case Constants.GroupType.MIN:
                return getGroupSql("min", alias, fieldName);
            case Constants.GroupType.SUM:
                return getGroupSql("sum", alias, fieldName);
            default:
                return alias + "." + fieldName;

        }
    }

    /**
     * 此字段是不是聚合字段
     *
     * @return
     */
    public boolean isGroupType() {
        return Constants.GroupType.NONE != this.getGroupType();
    }

    private String getGroupSql(String groupExp, String alias, String fieldName) {
        return groupExp + "(" + alias + "." + fieldName + ") as " + fieldName;
    }
}
