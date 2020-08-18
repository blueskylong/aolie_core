package com.ranranx.aolie.datameta.loader;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/13 17:26
 * @Version V0.0.1
 **/
public class LoaderConfiguration implements BeanFactoryPostProcessor {

    @Autowired
    private LoaderProperties properties;


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
