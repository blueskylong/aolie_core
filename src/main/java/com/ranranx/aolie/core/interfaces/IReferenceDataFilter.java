package com.ranranx.aolie.core.interfaces;

import com.ranranx.aolie.core.datameta.datamodel.ReferenceData;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.util.List;
import java.util.Map;

/**
 * 引用数据的过滤处理,用于很复杂的引用情况
 */
public interface IReferenceDataFilter {

    /**
     * 查询前调用,如果返回有内容,即使是空的内容,也不会再继续执行,会直接返回
     *
     * @return
     * @throws InvalidException
     */
    default List<ReferenceData> beforeQuery(Long refId, Long colId, Map<String, Object> values) throws InvalidException {
        return null;
    }

    default Criteria getExtFilter(Long refId, Long colId, Map<String, Object> values) throws InvalidException {
        if (values == null) {

        }
        return null;
    }

    /**
     * 数据查询过后,整理前调用,如果返回有数据,则直接返回
     *
     * @param param
     * @param result
     * @return
     */
    default List<ReferenceData> afterQuery(Long refId, Long colId, Object param, List<ReferenceData> result) {
        return result;
    }
}
