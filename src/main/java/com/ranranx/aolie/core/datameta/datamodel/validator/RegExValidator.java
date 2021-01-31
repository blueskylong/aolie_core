package com.ranranx.aolie.core.datameta.datamodel.validator;

import com.ranranx.aolie.core.annotation.Validator;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 正则表达式验证 ,这个验证由约束验证维护,不需要独立注册
 * TODO 这个验证器,不确定放置到哪个位置
 */
@Validator
class RegExValidator implements IValidator {
    private Pattern regex;


    public void setRegStr(String regStr) {
        this.regex = Pattern.compile(regStr);
    }

    @Override
    public String validateField(String fieldName,
                                Object value, Map<String, Object> row,
                                TableInfo tableInfo) {
        if (CommonUtils.isEmpty(value)) {
            return null;
        }
        if (!this.regex.matcher(value.toString()).find()) {
            return "输入内容不合规则";
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
        return false;
    }

    /**
     * 取得实例,有此验证器,可以是单例,有些多例,由验证器自己决定
     *
     * @param col
     * @param tableInfo
     */
    @Override
    public IValidator getInstance(Column col, TableInfo tableInfo) {
        return new RegExValidator();
    }

}
