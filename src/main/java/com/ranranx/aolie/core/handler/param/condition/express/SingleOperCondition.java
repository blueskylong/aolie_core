package com.ranranx.aolie.core.handler.param.condition.express;

/**
 * 单一值表达式
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 18:52
 **/
public abstract class SingleOperCondition extends BaseCondition {

    public SingleOperCondition(String tableName, String fieldName, Object value) {
        super(tableName, fieldName, value, null);
    }


    /**
     * 取得带占位符的表达式 如
     * field1 = ?
     *
     * @return
     */
    @Override
    public String getOperExpress() {
        return PLACEHOLDER_FIELD_NAME + getOperSign() + PLACEHOLDER_FIRST_VALUE;
    }


    abstract String getOperSign();
}
