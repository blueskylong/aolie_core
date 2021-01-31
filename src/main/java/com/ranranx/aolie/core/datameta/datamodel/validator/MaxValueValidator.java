package com.ranranx.aolie.core.datameta.datamodel.validator;

import com.ranranx.aolie.core.annotation.Validator;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;

import java.util.Map;


/**
 * @Author xxl
 * @Description 数值最大值验证或字符串最长
 * @Date 2020/8/13 20:10
 * @Version V0.0.1
 **/
@Validator
class MaxValueValidator implements IValidator {
    private String errStr;
    private String errNumber;
    private double maxValue;
    private boolean isNumberColumn;

    @Override
    public String validateField(String fieldName, Object value,
                                Map<String, Object> row, TableInfo tableInfo) {
        if (CommonUtils.isEmpty(value)) {
            return "";
        }

        if (this.isNumberColumn) {
            if (!CommonUtils.isNumber(value.toString())) {
                return "";
            }
            double num = Double.parseDouble(value.toString());

            if (num > this.maxValue) {
                return this.errNumber;
            }
        } else {
            if (value.toString().length() > this.maxValue) {
                return this.errStr;
            }
        }
        return "";
    }

    /**
     * 此字段是否需要此验证器验证
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public boolean isConcerned(Column col, TableInfo tableInfo) {
        Double maxValue = col.getColumnDto().getMaxValue();
        return maxValue != null;
    }

    /**
     * 取得实例,有此验证器,可以是单例,有些多例,由验证器自己决定
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public IValidator getInstance(Column col, TableInfo tableInfo) {
        MaxValueValidator max = new MaxValueValidator();
        max.maxValue = col.getColumnDto().getMaxValue();
        max.isNumberColumn = col.isNumberColumn();
        if (max.isNumberColumn) {
            max.errNumber = "数字最大不可以超过" + max.maxValue;
        } else {
            max.errStr = "最多可输入" + max.maxValue + "个字符";
        }
        return max;
    }
}
