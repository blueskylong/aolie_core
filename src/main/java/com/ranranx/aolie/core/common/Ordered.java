package com.ranranx.aolie.core.common;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/6 16:18
 * @Version V0.0.1
 **/
public interface Ordered {
    int BASE_ORDER = 3000;

    /**
     * 取得顺序
     *
     * @return
     */
    default int getOrder() {
        return BASE_ORDER;
    }
}
