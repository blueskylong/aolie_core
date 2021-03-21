package com.ranranx.aolie.core.handler.param.condition.express;

/**
 * 以指定值开始
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 19:04
 **/
public class StartWith extends BaseCondition {
    public StartWith(String tableName, String fieldName, Object value) {
        super(tableName, fieldName, value, null);
    }

    @Override
    String getOperExpress() {
        return PLACEHOLDER_FIELD_NAME + "  like concat(" + PLACEHOLDER_FIRST_VALUE + ",'%')";
    }
}
