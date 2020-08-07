package com.ranranx.aolie.interceptor;

import com.ranranx.aolie.engine.param.DeleteParam;
import com.ranranx.aolie.exceptions.BaseException;
import org.springframework.core.annotation.Order;

/**
 * @Author xxl
 * @Description 删除过程中的拦截器
 * @Date 2020/8/6 14:02
 * @Version V0.0.1
 **/
public interface DeleteInterceptor extends Order {

    /**
     * 删除前处理,抛出错误异常来终止执行.返回删除的数量,如果返回量大于0,则不执行后默认的删除逻辑
     *
     * @param param
     * @return
     */
    default int beforeDelete(DeleteParam param) throws BaseException {
        return 0;
    }

    /**
     * 删除后做处理
     *
     * @param param
     */
    default void afterDelete(DeleteParam param) {

    }
}
