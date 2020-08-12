package com.ranranx.aolie.ds.dataoperator.mybatis;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.ds.dataoperator.multids.DataSourceWrapper;
import com.ranranx.aolie.ds.dataoperator.multids.DynamicDataSource;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/11 13:00
 * @Version V0.0.1
 **/
@Configuration
public class MybatisDbOperatorConfig implements BeanPostProcessor, BeanDefinitionRegistryPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MybatisAutoConfiguration.class);


    /**
     * 这里需要初始化 TODO
     */
    private List<DataOperatorDto> lstOperSet;


    public MybatisDbOperatorConfig() {
        lstOperSet = new ArrayList<>();
        DataOperatorDto dto = new DataOperatorDto();
//        dto.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dto.setUrl("jdbc:mysql://localhost:3306/world?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        dto.setUserName("root");
        dto.setPassword("root");
        dto.setName("mysql");
        dto.setVersionCode("1");
        lstOperSet.add(dto);
        dto = new DataOperatorDto();
//        dto.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dto.setUrl("jdbc:mysql://localhost:3306/world?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        dto.setUserName("root");
        dto.setPassword("root");
        dto.setName("mysql2");
        dto.setVersionCode("1");
        lstOperSet.add(dto);
    }

    /**
     * Modify the application context's internal bean definition registry after its
     * standard initialization. All regular bean definitions will have been loaded,
     * but no beans will have been instantiated yet. This allows for adding further
     * bean definitions before the next post-processing phase kicks in.
     *
     * @param registry the bean definition registry used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        logger.info("-->Start registry Dm Service");
        try {
            for (DataOperatorDto dto : lstOperSet) {
                String key = CommonUtils.makeKey(dto.getName(), dto.getVersionCode());
                registry.registerBeanDefinition(key,
                        new RootBeanDefinition(DataSourceWrapper.class));
                registry.registerBeanDefinition(DataSourceUtils.makeDsKey(key), new RootBeanDefinition(MyBatisDataOperator.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private DataOperatorDto findDto(String key) {
        for (DataOperatorDto dto : lstOperSet) {
            if (CommonUtils.makeKey(dto.getName(), dto.getVersionCode()).equals(key)) {
                return dto;
            }
        }
        return null;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSourceWrapper) {
            ((DataSourceWrapper) bean).setDto(findDto(beanName));
        } else if (bean instanceof MyBatisDataOperator) {
            ((MyBatisDataOperator) bean).setDto(findDto(DataSourceUtils.getKeyByDsKey(beanName)));
        }
        return bean;
    }

    /**
     * Modify the application context's internal bean factory after its standard
     * initialization. All bean definitions will have been loaded, but no beans
     * will have been instantiated yet. This allows for overriding or adding
     * properties even to eager-initializing beans.
     *
     * @param beanFactory the bean factory used by the application context
     * @throws BeansException in case of errors
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Bean
    public static DynamicDataSource getDds() {
        return new DynamicDataSource();
    }
}
