package com.ranranx.aolie.interceptor;

import com.ranranx.aolie.engine.param.UpdateParam;
import com.ranranx.aolie.exceptions.BaseException;
import org.springframework.core.annotation.Order;

/**
 * @Author xxl
 * @Description 数据增加或更新拦截器
 * @Date 2020/8/6 15:16
 * @Version V0.0.1
 **/
public interface UpdateInterceptor extends Order {
    /**
     * 更新前调用,如果返回数量大于0,则不再执行后面的操作
     *
     * @param param
     * @return
     */
    default int beforeUpdate(UpdateParam param) throws BaseException {
        return 0;
    }

    /**
     * 更新后做的处理
     *
     * @param param
     * @param updatedCount
     */
    default void afterUpdate(UpdateParam param, int updatedCount) {

    }
}
