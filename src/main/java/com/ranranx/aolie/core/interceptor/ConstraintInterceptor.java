package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.Ordered;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;

import java.util.Map;

/**
 * @Author xxl
 * @Description 数据保存前的验证, 约束
 * @Date 2021/1/29 0029 9:05
 * @Version V0.0.1
 **/
@DbOperInterceptor
public class ConstraintInterceptor implements IOperInterceptor {
    /**
     * 操作前调用,如果返回有内容,则会直接返回
     *
     * @param param
     * @param handleType
     * @return
     * @throws InvalidException
     */
    @Override
    public HandleResult beforeOper(Object param, String handleType,
                                   Map<String, Object> globalParamData) throws InvalidException {
        return null;
    }

    /**
     * 是否可以处理
     *
     * @param type
     * @param objExtinfo
     * @return
     */
    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return type == Constants.HandleType.TYPE_UPDATE
                || type == Constants.HandleType.TYPE_INSERT;
    }

    @Override
    public int getOrder() {
        return Ordered.BASE_ORDER + 50;
    }
}
