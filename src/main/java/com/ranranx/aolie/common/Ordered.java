package com.ranranx.aolie.common;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/6 16:18
 * @Version V0.0.1
 **/
public interface Ordered {
    /**
     * 取得顺序
     *
     * @return
     */
    default int getOrder() {
        return 3000;
    }
}
