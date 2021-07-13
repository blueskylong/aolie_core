package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;

/**
 * @author xxl
 * 查询用的字段描述
 * @version V0.0.1
 * @date 2020/8/18 17:54
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
     * 别名
     */
    private String aliasName;

    /**
     * 默认值
     */
    private String defaultValue;
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
        String fieldAlias = CommonUtils.isEmpty(aliasName) ? fieldName : aliasName;
        switch (this.groupType) {
            case Constants.GroupType.AVG:
                return getGroupSql("avg", alias, fieldName, fieldAlias);
            case Constants.GroupType.COUNT:
                return getGroupSql("count", alias, fieldName, fieldAlias);
            case Constants.GroupType.MAX:
                return getGroupSql("max", alias, fieldName, fieldAlias);
            case Constants.GroupType.MIN:
                return getGroupSql("min", alias, fieldName, fieldAlias);
            case Constants.GroupType.SUM:
                return getGroupSql("sum", alias, fieldName, fieldAlias);
            default:
                //加个别名
                String field = (CommonUtils.isNotEmpty(alias) ? (alias + ".") : "") + fieldName;
                if (CommonUtils.isNotEmpty(aliasName)) {
                    if (CommonUtils.isNotEmpty(defaultValue)) {
                        return " case when " + field + " is null then '" + defaultValue + "' else " + field + " end " + " as " + aliasName;
                    }
                    return (CommonUtils.isNotEmpty(alias) ? (alias + ".") : "") + fieldName + " as " + aliasName;
                }
                if (CommonUtils.isNotEmpty(defaultValue)) {
                    return " case when " + field + " is null then '" + defaultValue + "' else " + field + " end " + " as " + fieldName;
                }
                return (CommonUtils.isNotEmpty(alias) ? (alias + ".") : "") + fieldName;

        }
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     * 此字段是不是聚合字段
     *
     * @return
     */
    public boolean isGroupType() {
        return Constants.GroupType.NONE != this.getGroupType();
    }

    private String getGroupSql(String groupExp, String alias, String fieldName, String alignName) {
        if (CommonUtils.isNotEmpty(alias)) {
            alias += ".";
        } else {
            alias = "";
        }
        return groupExp + "(" + alias + fieldName + ") as " + alignName;
    }

    /**
     * 生成查询所有
     *
     * @param tableName
     * @return
     */
    public static Field genFieldAll(String tableName) {
        Field field = new Field();
        field.setFieldName("*");
        field.setTableName(tableName);
        return field;
    }
    public Field() {
    }

    public Field(String tableName, String fieldName) {
        this.tableName = tableName;
        this.fieldName = fieldName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
