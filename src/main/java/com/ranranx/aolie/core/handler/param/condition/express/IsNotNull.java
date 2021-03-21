package com.ranranx.aolie.core.handler.param.condition.express;

import com.ranranx.aolie.core.common.CommonUtils;

/**
 * 字段值不为空
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/12 0012 14:56
 **/
public class IsNotNull extends BaseCondition {

    public static final String express = PLACEHOLDER_FIELD_NAME + " is not null ";

    public IsNotNull(String tableName, String fieldName) {
        super(tableName, fieldName, null, null);
    }


    @Override
    String getOperExpress() {
        return express;
    }

    @Override
    public String checkValid() {
        if (CommonUtils.isEmpty(fieldName)) {
            return "条件字段没有提供";
        }
        return null;
    }

}
