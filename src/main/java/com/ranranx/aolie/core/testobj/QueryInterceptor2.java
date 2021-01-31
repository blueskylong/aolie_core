package com.ranranx.aolie.core.testobj;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.interceptor.IOperInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @Author xxl
 * @Description
 * @Date 2020/8/10 15:11
 * @Version V0.0.1
 **/
@Component
public class QueryInterceptor2 implements IOperInterceptor {
    Logger logger = LoggerFactory.getLogger(QueryInterceptor2.class);

    /**
     * 是否可以处理
     *
     * @param type
     * @param objExtinfo
     * @return
     */
    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return type.equals(Constants.HandleType.TYPE_QUERY);
    }

    /**
     * 操作前调用,如果返回有内容,则会直接返回
     *
     * @param param
     * @return
     * @throws InvalidException
     */
    @Override
    public HandleResult beforeOper(Object param, String handelType, Map<String, Object> mapGlobalParam) throws InvalidException {
        logger.error("--> Query  " + getOrder() + ":beforeOper");
        return null;
    }

    /**
     * 数据查询过后,整理前调用,如果返回有数据,则直接返回
     *
     * @param param
     * @param result
     * @return
     */
    @Override
    public HandleResult afterOper(Object param, String handelType, Map<String, Object> mapGlobalParam, HandleResult result) {
        logger.error("--> Query  " + getOrder() + ":afterOper");
        return null;
    }

    /**
     * 返回前调用,这里会遍历调用,所以要返回结果,不需要处理的就直接返回
     *
     * @param param
     * @param handleResult
     * @return
     */
    @Override
    public HandleResult beforeReturn(Object param, String handelType, Map<String, Object> mapGlobalParam, HandleResult handleResult) {
        logger.error("--> Query  " + getOrder() + ":beforeReturn");
        return null;
    }

    /**
     * 取得顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 2;
    }
}
