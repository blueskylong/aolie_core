package com.ranranx.aolie.interceptor;

import com.ranranx.aolie.engine.param.QueryParam;
import com.ranranx.aolie.exceptions.InvalidException;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 查询过程中的拦截器
 * @Date 2020/8/6 14:02
 * @Version V0.0.1
 **/
public interface QueryInterceptor extends Order {

    /**
     * 取得拦截器的执行顺序号
     *
     * @return
     */
    default int getOrder() {
        return 99999;
    }

    /**
     * 查询前调用,如果返回有内容,则会直接返回
     *
     * @param param
     * @return
     * @throws InvalidException
     */
    default List<Map<String, Object>> beforeQuery(QueryParam param) throws InvalidException {
        return null;
    }

    /**
     * 数据查询过后,整理前调用,如果返回有数据,则直接返回
     *
     * @param param
     * @param mapData
     * @return
     */
    default List<Map<String, Object>> beforeMerge(QueryParam param, Map<String, List<Map<String, Object>>> mapData) {
        return null;
    }

    /**
     * 返回前调用,则会
     *
     * @param param
     * @param lstData
     * @return
     */
    default List<Map<String, Object>> beforeReturn(QueryParam param, List<Map<String, Object>> lstData) {
        return null;
    }
}
