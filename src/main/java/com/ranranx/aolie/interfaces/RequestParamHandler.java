package com.ranranx.aolie.interfaces;

import com.ranranx.aolie.ds.definition.QueryParamDefinition;

/**
 * 客户端查询参数处理器
 */
public interface RequestParamHandler {
    /**
     * 将参数转换成查询参数定义
     *
     * @return
     */
    QueryParamDefinition getQueryParamDefinition() throws Exception;
}
