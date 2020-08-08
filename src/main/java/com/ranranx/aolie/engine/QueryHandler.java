package com.ranranx.aolie.engine;

import com.ranranx.aolie.engine.param.QueryParam;
import com.ranranx.aolie.exceptions.InvalidException;
import com.ranranx.aolie.interceptor.QueryInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 数据查询服务
 * @Date 2020/8/4 14:33
 * @Version V0.0.1
 **/
public class QueryHandler {
    private List<QueryInterceptor> lstQueryInterceptor;

    /**
     * 执行查询
     *
     * @param mapParams
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> query(Map<String, Object> mapParams) throws Exception {
        QueryParam queryParam = checkAndMakeQueryParam(mapParams);
        //查询前执行
        List<Map<String, Object>> lstResult = doBeforeQuery(queryParam);
        //如果有拦截器直接返回数据,则不再向后执行
        if (lstResult != null) {
            return lstResult;
        }
        Map<String, List<Map<String, Object>>> allResult = findAllResult(queryParam);
        lstResult = doBeforeMerge(queryParam, allResult);
        if (lstResult != null) {
            return lstResult;
        }
        List<Map<String, Object>> lst = mergeResults(queryParam, allResult);
        lstResult = doBeforeReturn(queryParam, lst);
        if (lstResult != null) {
            return lstResult;
        }
        return lst;
    }

    /**
     * 查询所有的子查询的数据 //TODO
     *
     * @param param
     * @return
     */
    private Map<String, List<Map<String, Object>>> findAllResult(QueryParam param) {
        return null;
    }

    /**
     * 合并数据
     *
     * @param param
     * @param mapResult
     * @return
     */
    private List<Map<String, Object>> mergeResults(QueryParam param, Map<String, List<Map<String, Object>>> mapResult) {
        if (mapResult == null || mapResult.isEmpty()) {
            return new ArrayList<>();
        } else if (mapResult.size() == 1) {
            return mapResult.values().iterator().next();
        }
        //TODO doMerge
        return null;
    }


    /**
     * 检查并生成查询参数
     *
     * @param mapParams
     * @return
     */
    private QueryParam checkAndMakeQueryParam(Map<String, Object> mapParams) throws InvalidException {
        //TODO
        return null;
    }

    private List<Map<String, Object>> doBeforeQuery(QueryParam queryParam) {
        List<Map<String, Object>> result = null;
        for (QueryInterceptor inter : lstQueryInterceptor) {
            result = inter.beforeQuery(queryParam);
            if (result != null) {
                return result;
            }
        }
        //TODO
        return null;
    }

    private List<Map<String, Object>> doBeforeReturn(QueryParam queryParam, List<Map<String, Object>> lstResult) {
        List<Map<String, Object>> result = null;
        for (QueryInterceptor inter : lstQueryInterceptor) {
            result = inter.beforeReturn(queryParam, lstResult);
            if (result != null) {
                return result;
            }
        }
        //TODO
        return null;
    }

    private List<Map<String, Object>> doBeforeMerge(QueryParam queryParam, Map<String, List<Map<String, Object>>> mapResult) {
        List<Map<String, Object>> result = null;
        for (QueryInterceptor inter : lstQueryInterceptor) {
            result = inter.beforeMerge(queryParam, mapResult);
            if (result != null) {
                return result;
            }
        }
        //TODO
        return null;
    }
}
