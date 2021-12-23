package com.ranranx.aolie.core.handler.param.condition.express;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.handler.param.condition.ICondition;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 条件基础类
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 17:26
 **/
public abstract class BaseCondition implements ICondition {
    /**
     * 字段名占位符
     */
    static final String PLACEHOLDER_FIELD_NAME = "__FIELD__";
    static final String PLACEHOLDER_FIRST_VALUE = "__VALUE1__";
    static final String PLACEHOLDER_SECOND_VALUE = "__VALUE2__";


    /**
     * 表名
     */
    protected String tableName;
    /**
     * 字段名
     */
    protected String fieldName;
    /**
     * 逻辑符
     */
    protected String andOr = AND;
    /**
     * 条件
     */
    protected Object value1;
    protected Object value2;
    /**
     * 分组函数
     */
    protected String groupType;


    public BaseCondition(String tableName, String fieldName, Object value1, Object value2) {
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.value1 = value1;
        this.value2 = value2;
        String err = this.checkValid();
        if (CommonUtils.isNotEmpty(err)) {
            throw new InvalidParamException(err);
        }

    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }


    public BaseCondition setIsOr(boolean isOr) {
        this.andOr = isOr ? OR : AND;
        return this;
    }

    /**
     * 取得参数的名称
     *
     * @param index
     * @return
     */
    protected String getParamName(int index) {
        return "P" + index;
    }

    /**
     * 取得字段完整名
     *
     * @param alias
     * @return
     */
    protected String getFullFieldName(Map<String, String> alias) {
        if (CommonUtils.isEmpty(this.fieldName)) {
            return null;
        }
        String tableAlias = "";
        if (CommonUtils.isNotEmpty(this.tableName)) {
            if (alias != null && alias.containsKey(this.tableName)) {
                tableAlias = alias.get(this.tableName) + ".";
            } else {
                tableAlias = tableName + ".";
            }

        }


        String fullFieldName = fieldName;
        if (CommonUtils.isNotEmpty(fieldName)) {
            fullFieldName = tableAlias + fieldName;
            if (CommonUtils.isNotEmpty(groupType)) {
                fullFieldName = groupType + "(" + fieldName + ")";
            }
        }
        return fullFieldName;
    }

    @Override
    public String getSqlWhere(Map<String, Object> mapValue, Map<String, String> alias,
                              int[] index, boolean needLogic) {
        String firstParamName = getParamName(index[0]) + "_1";
        String secondParamName = getParamName(index[0]) + "_2";
        String andOrStr = (needLogic ? " " + andOr + "  " : "");
        String express = this.getOperExpress();
        if (CommonUtils.isNotEmpty(fieldName)) {
            express = StringUtils.replace(express, PLACEHOLDER_FIELD_NAME, getFullFieldName(alias));
        }
        //替换第一个参数
        if (CommonUtils.isNotEmpty(value1)) {
            express = StringUtils.replace(express, PLACEHOLDER_FIRST_VALUE, toParam(firstParamName));
            //加入值到MAP
            mapValue.put(firstParamName, value1);
        }
        //如果有第二个参数,则替换
        if (CommonUtils.isNotEmpty(value2)) {
            express = StringUtils.replace(express, PLACEHOLDER_SECOND_VALUE, toParam(secondParamName));
            //加入值到MAP
            mapValue.put(secondParamName, value2);
        }

        return andOrStr + express;

    }

    /**
     * 转换成参数形式
     *
     * @param paramName 参数名
     * @return
     */
    private String toParam(String paramName) {
        return "#{" + paramName + "}";
    }

    /**
     * 取得带占位符的表达式 如
     *
     * @return
     */
    abstract String getOperExpress();

    public String getAndOr() {
        return andOr;
    }

    public void setAndOr(String andOr) {
        this.andOr = andOr;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }


    public String checkValid() {
        if (CommonUtils.isEmpty(fieldName)) {
            return "条件字段没有提供";
        }
        if (CommonUtils.isEmpty(value1)) {
            return "条件值没有提供";
        }
        return null;

    }

    /**
     * 是不是指定字段的过滤条件
     *
     * @param condition
     * @return
     */
    public boolean isHasFieldFilter(BaseCondition condition) {
        return condition.andOr.equals(this.andOr) && this.fieldName.equals(condition.fieldName)
                && this.getOperExpress().equals(condition.getOperExpress());
    }


    public String getFieldName() {
        return fieldName;
    }

    public Object getValue1() {
        return value1;
    }

    public Object getValue2() {
        return value2;
    }
}
