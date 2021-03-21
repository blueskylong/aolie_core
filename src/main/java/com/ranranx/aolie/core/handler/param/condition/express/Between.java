package com.ranranx.aolie.core.handler.param.condition.express;

import com.ranranx.aolie.core.common.CommonUtils;

/**
 * between and条件表达式
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/11 0011 22:21
 **/
public class Between extends BaseCondition {
    public Between(String tableName, String fieldName, Object value1, Object value2) {
        super(tableName, fieldName, value1, value2);
    }

    @Override
    String getOperExpress() {
        return PLACEHOLDER_FIELD_NAME + " between " + PLACEHOLDER_FIRST_VALUE + " and " + PLACEHOLDER_SECOND_VALUE;
    }

    @Override
    public String checkValid() {
        String err = super.checkValid();
        if (CommonUtils.isNotEmpty(err)) {
            return err;
        }
        if (CommonUtils.isEmpty(value2)) {
            return "第二个值不可以为空";
        }
        return null;
    }
}
