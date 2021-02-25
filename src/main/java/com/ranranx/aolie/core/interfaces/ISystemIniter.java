package com.ranranx.aolie.core.interfaces;

/**
 * 需要有顺序加载的服务, 此接口的类需要自己注册到容器中.
 * 所有的实现都在系统 基础数据(SchemaHolder)初始化完成后执行.代替 @PostConstruct
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/25 0025 15:09
 **/
public interface ISystemIniter {

    /**
     * 执行初始化
     */
    void init();

    /**
     * 执行顺序,越小越优先
     *
     * @return
     */
    int getOrder();
}
