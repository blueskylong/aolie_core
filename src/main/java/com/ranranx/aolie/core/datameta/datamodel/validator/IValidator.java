package com.ranranx.aolie.core.datameta.datamodel.validator;

import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;

import java.util.Map;

/**
 * @author xxl
 *  验证器接口
 * @date 2020/8/13 20:10
 * @version V0.0.1
 **/
public interface IValidator {

    /**
     * 验证单个数据
     *
     * @param fieldName
     * @param value
     * @param row
     * @param tableInfo
     * @return 返回错误信息
     */
    String validateField(String fieldName, Object value,
                         Map<String, Object> row, TableInfo tableInfo);

    /**
     * 此字段是否需要此验证器验证
     *
     * @param column
     * @param tableInfo
     * @return
     */
    boolean isConcerned(Column column, TableInfo tableInfo);

    /**
     * 取得实例,有此验证器,可以是单例,有些多例,由验证器自己决定
     *
     * @param column
     * @param tableInfo
     */
    IValidator getInstance(Column column, TableInfo tableInfo);
}
