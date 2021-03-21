package com.ranranx.aolie.core.handler.param.condition.express;

/**
 * 不以 指定结尾结束
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 19:04
 **/
public class NotEndWith extends BaseCondition {
    public NotEndWith(String tableName, String fieldName, Object value) {
        super(tableName, fieldName, value, null);
    }

    @Override
    String getOperExpress() {
        return PLACEHOLDER_FIELD_NAME + " not like concat('%'," + PLACEHOLDER_FIRST_VALUE + ")";
    }
}
