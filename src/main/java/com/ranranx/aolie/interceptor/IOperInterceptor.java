package com.ranranx.aolie.interceptor;

import com.ranranx.aolie.common.Ordered;
import com.ranranx.aolie.exceptions.InvalidException;
import com.ranranx.aolie.handler.HandleResult;
import com.ranranx.aolie.handler.param.QueryParam;
import com.ranranx.aolie.interfaces.IHandleFilter;

import java.util.Map;

/**
 * @Author xxl
 * @Description 处理拦截器
 * @Date 2020/8/10 10:17
 * @Version V0.0.1
 **/
public interface IOperInterceptor extends Ordered, IHandleFilter {


    /**
     * 操作前调用,如果返回有内容,则会直接返回
     *
     * @param param
     * @return
     * @throws InvalidException
     */
    default HandleResult beforeOper(Object param) throws InvalidException {
        return null;
    }

    /**
     * 数据查询过后,整理前调用,如果返回有数据,则直接返回
     *
     * @param param
     * @param result
     * @return
     */
    default HandleResult afterOper(Object param, HandleResult result) {
        return null;
    }

    /**
     * 返回前调用,这里会遍历调用,所以要返回结果,不需要处理的就直接返回
     *
     * @param param
     * @param handleResult
     * @return
     */
    default HandleResult beforeReturn(Object param, HandleResult handleResult) {
        return handleResult;
    }
}
