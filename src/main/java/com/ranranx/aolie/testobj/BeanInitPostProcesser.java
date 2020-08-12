package com.ranranx.aolie.testobj;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Author xxl
 * @Description
 * @Date 2020/7/7 15:26
 * @Version V1.0
 **/
//@Component
public class BeanInitPostProcesser implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("--------------->postProcessBeforeInitialization>>>>>beanName:" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("--------------->postProcessAfterInitialization>>>>>beanName:" + beanName);
        return bean;
    }
}
