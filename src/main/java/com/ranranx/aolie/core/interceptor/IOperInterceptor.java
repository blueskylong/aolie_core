package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.common.Ordered;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.interfaces.IHandleFilter;

import java.util.Map;

/**
 * @author xxl
 *  处理拦截器, 拦截器可以直接返回结果, 如果返回结果, 则不再向下处理, 直接返回
 * @date 2020/8/10 10:17
 * @version V0.0.1
 **/
public interface IOperInterceptor extends Ordered, IHandleFilter {
    /**
     * 直接操作的标记,遇到这个标记在全局参数里,可以不再进行处理
     */
    public static final String AFTER_FORMULA_DIRECT_OPER = "AFTER_FORMULA_DIRECT_OPER";

    /**
     * 由计算拦截器提供,记录计算了哪些表, 目前不保证是有变化的表
     */
    public static final String CHANGED_TABLE_ROWS = "CHANGED_TABLE_ROWS";


    /**
     * 操作前调用,如果返回有内容,则会直接返回
     *
     * @param param
     * @param handleType
     * @param globalParamData 扩展数据,所有的拦截器,可以多这里取数,也可以在这里放置数据,此数据贯穿整个处理过程
     * @return
     * @throws InvalidException
     */
    default HandleResult beforeOper(Object param, String handleType, Map<String, Object> globalParamData) throws InvalidException {
        return null;
    }

    /**
     * 数据查询过后,整理前调用,如果返回有数据,则直接返回
     *
     * @param param
     * @param handleType
     * @param globalParamData 扩展数据,所有的拦截器,可以多这里取数,也可以在这里放置数据,此数据贯穿整个处理过程
     * @param result
     * @return
     */
    default HandleResult afterOper(Object param, String handleType, Map<String, Object> globalParamData,
                                   HandleResult result) {
        return null;
    }

    /**
     * 返回前调用,这里会遍历调用,所以要返回结果,不需要处理的就直接返回
     *
     * @param param
     * @param handleType
     * @param globalParamData 扩展数据,所有的拦截器,可以多这里取数,也可以在这里放置数据,此数据贯穿整个处理过程
     * @param handleResult
     * @return
     */
    default HandleResult beforeReturn(Object param, String handleType, Map<String, Object> globalParamData,
                                      HandleResult handleResult) {
        return handleResult;
    }
}
