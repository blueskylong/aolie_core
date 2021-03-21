package com.ranranx.aolie.core.handler.param.condition.express;

/**
 * 以指定值结束
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 19:04
 **/
public class EndWith extends BaseCondition {
    public EndWith(String tableName, String fieldName, Object value) {
        super(tableName, fieldName, value, null);
    }

    @Override
    String getOperExpress() {
        return PLACEHOLDER_FIELD_NAME + "  like concat('%'," + PLACEHOLDER_FIRST_VALUE + ")";
    }
}
