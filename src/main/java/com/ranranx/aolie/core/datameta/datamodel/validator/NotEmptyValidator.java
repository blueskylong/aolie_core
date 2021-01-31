package com.ranranx.aolie.core.datameta.datamodel.validator;

import com.ranranx.aolie.core.annotation.Validator;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;

import java.util.Map;

/**
 * 必填
 */
@Validator
class NotEmptyValidator implements IValidator {
    public static final String ERR_HINT = "字段[%s]:不可以为空";

    @Override
    public String validateField(String fieldName, Object value,
                                Map<String, Object> row, TableInfo tableInfo) {
        if (value == null || value.toString().trim() == "") {
            return String.format(ERR_HINT,
                    (tableInfo.findColumnByName(fieldName).getColumnDto().getTitle()));
        }
        return null;
    }

    /**
     * 此字段是否需要此验证器验证
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public boolean isConcerned(Column col, TableInfo tableInfo) {
        Byte bytes = col.getColumnDto().getNullable();
        return bytes != null && bytes == 0;
    }

    /**
     * 取得实例,有此验证器,可以是单例,有些多例,由验证器自己决定
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public IValidator getInstance(Column col, TableInfo tableInfo) {
        return this;
    }
}
