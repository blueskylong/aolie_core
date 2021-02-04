package com.ranranx.aolie.core.testobj;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author xxl
 *
 * @date 2020/7/7 15:26
 * @version V1.0
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
