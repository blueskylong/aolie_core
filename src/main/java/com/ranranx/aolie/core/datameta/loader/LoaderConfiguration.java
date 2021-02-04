package com.ranranx.aolie.core.datameta.loader;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author xxl
 *
 * @date 2020/8/13 17:26
 * @version V0.0.1
 **/
public class LoaderConfiguration implements BeanFactoryPostProcessor {

    @Autowired
    private LoaderProperties properties;


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
