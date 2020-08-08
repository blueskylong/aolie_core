package com.ranranx.aolie.engine;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.engine.param.DeleteParam;
import com.ranranx.aolie.engine.param.QueryParam;
import com.ranranx.aolie.interceptor.DeleteInterceptor;
import com.ranranx.aolie.interceptor.QueryInterceptor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 数据删除服务
 * @Date 2020/8/4 14:35
 * @Version V0.0.1
 **/
public class DeleteHandler {
    private List<DeleteInterceptor> lstInterceptor;

    /**
     * 执行删除
     *
     * @param mapParams
     * @return
     * @throws Exception
     */
    public int delete(Map<String, Object> mapParams) throws Exception {
        DeleteParam deleteParam = checkAndMakeDeleteParam(mapParams);
        //查询前执行
        int num = doBeforeDelete(deleteParam);
        //如果有拦截器直接返回数据,则不再向后执行
        if (num > 0) {
            return num;
        }
        num = doDelete(deleteParam);
        doAfterDelete(deleteParam);
        return num;
    }

    private int doDelete(DeleteParam deleteParam) {

        return 0;
    }

    private void doAfterDelete(DeleteParam param) {
        for (DeleteInterceptor inter : lstInterceptor) {
            inter.afterDelete(param);
        }
    }

    private int doBeforeDelete(DeleteParam deleteParam) {
        int num;
        for (DeleteInterceptor inter : lstInterceptor) {
            num = inter.beforeDelete(deleteParam);
            if (num > 0) {
                return num;
            }
        }
        return 0;
    }

    private DeleteParam checkAndMakeDeleteParam(Map<String, Object> mapParams) {
        return null;
    }

}
