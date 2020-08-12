package com.ranranx.aolie.handler;

import com.ranranx.aolie.common.Ordered;
import com.ranranx.aolie.interfaces.IHandleFilter;

import java.util.Map;

/**
 * @Author xxl
 * @Description 数据处理接口
 * @Date 2020/8/8 19:52
 * @Version V0.0.1
 **/
public interface IDbHandler extends Ordered, IHandleFilter {

    /**
     * 处理操作
     *
     * @param mapParam
     * @return
     */
    HandleResult doHandle(Map<String, Object> mapParam);

    /**
     * 开始事务
     *
     * @return
     */
    default long beginTransaction() {
        return 0;
    }

    /**
     * 提交事务
     */
    default void commit() {

    }

    /**
     * 是否需要事务
     * @return
     */
    default boolean needTransaction() {
        return true;
    }


    /**
     * 回滚事务
     */
    default void rollback() {

    }

}
