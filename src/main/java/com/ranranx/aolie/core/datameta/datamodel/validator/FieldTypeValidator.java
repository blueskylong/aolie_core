package com.ranranx.aolie.core.datameta.datamodel.validator;

import com.ranranx.aolie.core.annotation.Validator;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;

import java.util.Map;


/**
 * @Author xxl
 * @Description 值类型检查, 检查数字
 * @Date 2020/8/13 20:10
 * @Version V0.0.1
 **/
@Validator
public class FieldTypeValidator implements IValidator {

    @Override
    public String validateField(String fieldName, Object value,
                                Map<String, Object> row, TableInfo tableInfo) {
        if (value == null) {
            return null;
        }
        if (CommonUtils.isNumber(value)) {
            return null;
        }
        return "请输入有效的数字";
    }

    /**
     * 此字段是否需要此验证器验证
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public boolean isConcerned(Column col, TableInfo tableInfo) {
        return col.isNumberColumn();
    }

    /**
     * 取得实例,有此验证器,可以是单例,有些多例,由验证器自己决定
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public IValidator getInstance(Column col, TableInfo tableInfo) {
        return new FieldTypeValidator();
    }
}
