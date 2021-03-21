package com.ranranx.aolie.core.handler.param.condition.express;

/**
 * 等于条件
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 18:02
 **/
public class Equals extends SingleOperCondition {

    public Equals(String tableName, String fieldName, Object value) {
        super(tableName, fieldName, value);
    }

    @Override
    String getOperSign() {
        return "=";
    }
}
