package com.ranranx.aolie.core.interfaces;

/**
 * @Author xxl
 * @Description 判断类型是否适配的接口
 * @Date 2020/8/10 10:23
 * @Version V0.0.1
 **/
public interface IHandleFilter {
    /**
     * 是否可以处理
     *
     * @param type
     * @param objExtinfo
     * @return
     */
    boolean isCanHandle(String type, Object objExtinfo);
}
