package com.ranranx.aolie.core.datameta.datamodel.validator;

import com.ranranx.aolie.core.annotation.Validator;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;

import java.util.Map;


/**
 * @Author xxl
 * @Description 最小值验证
 * @Date 2020/8/13 20:10
 * @Version V0.0.1
 **/
@Validator
public class MinValueValidator implements IValidator {
    private String errStr;
    private String errNumber;
    private double minValue;
    private boolean isNumberColumn;

    @Override
    public String validateField(String fieldName, Object value,
                                Map<String, Object> row, TableInfo tableInfo) {
        if (CommonUtils.isEmpty(value)) {
            return "";
        }

        if (this.isNumberColumn) {
            if (!CommonUtils.isNumber(value)) {
                return "";
            }
            double num = Double.parseDouble(value.toString());
            if (num < this.minValue) {
                return this.errNumber;
            }
        } else {

            if (value.toString().length() < this.minValue) {
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
        Double minValue = col.getColumnDto().getMinValue();
        return minValue != null;
    }

    /**
     * 取得实例,有此验证器,可以是单例,有些多例,由验证器自己决定
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public IValidator getInstance(Column col, TableInfo tableInfo) {
        MinValueValidator min = new MinValueValidator();
        min.minValue = col.getColumnDto().getMinValue();
        min.isNumberColumn = col.isNumberColumn();
        if (min.isNumberColumn) {
            min.errNumber = "数字最小不可小于" + min.minValue;
        } else {
            min.errStr = "最少需要输入" + min.minValue + "个字符";
        }
        return min;
    }
}
