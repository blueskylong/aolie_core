package com.ranranx.aolie.core.ds.dataoperator.mybatis;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.dto.DataOperatorDto;
import com.ranranx.aolie.core.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.core.ds.dataoperator.multids.DataSourceWrapper;
import com.ranranx.aolie.core.ds.dataoperator.multids.DynamicDataSource;
import com.ranranx.aolie.core.exceptions.InvalidException;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;

/**
 * @author xxl
 *
 * @date 2020/8/11 13:00
 * @version V0.0.1
 **/
@Configuration
public class MybatisDbOperatorConfig implements BeanPostProcessor, BeanDefinitionRegistryPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MybatisAutoConfiguration.class);


    /**
     * 这里需要初始化 TODO
     */
    @Autowired
    private List<DataOperatorDto> lstOperSet;


    public MybatisDbOperatorConfig() {
        lstOperSet = new ArrayList<>();
        lstOperSet.add(loadDefaultDs());

    }

    /**
     * 加载默认数据源
     *
     * @return
     */
    private DataOperatorDto loadDefaultDs() {
        try {
            Properties properties = new Properties();
            // 使用ClassLoader加载properties配置文件生成对应的输入流
            InputStream in = MybatisDbOperatorConfig.class.getClassLoader().getResourceAsStream("aolie.properties");
            // 使用properties对象加载输入流
            properties.load(in);

            Map<String, Object> map = convertToMap(properties);
            DataOperatorDto dto = CommonUtils.populateBean(DataOperatorDto.class, map);
            setDefaultDsInfo(dto);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidException("默认数据库参数初始化失败,请确定aolie.properties文件存在,并正确设置了数据库连接参数");
        }
    }

    /**
     * 默认数据源的默认配置
     */
    private void setDefaultDsInfo(DataOperatorDto dto) {
        dto.setId(0L);
        dto.setName("default");
        dto.setIsDefault((short) 1);
        dto.setVersionCode("0");
    }


    private Map<String, Object> convertToMap(Properties properties) {
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        Map<String, Object> map = new HashMap<>();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> next = iterator.next();
            map.put(next.getKey().toString(), next.getValue());
        }
        return map;
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
                //如果没有指定操作器的类,则默认使用多数据MyBatis类来创建
                if (CommonUtils.isEmpty(dto.getOperatorClass())) {
                    registry.registerBeanDefinition(DataSourceUtils.makeDsKey(key),
                            new RootBeanDefinition(MyBatisDataOperator.class));
                } else {
                    registry.registerBeanDefinition(DataSourceUtils.makeDsKey(key),
                            new RootBeanDefinition(dto.getOperatorClass()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private DataOperatorDto findDataOperatorDto(String key) {
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
            ((DataSourceWrapper) bean).setDto(findDataOperatorDto(beanName));
        } else if (bean instanceof MyBatisDataOperator) {
            ((MyBatisDataOperator) bean).setDto(findDataOperatorDto(DataSourceUtils.getKeyByDsKey(beanName)));
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
        //这里开始注册
        System.out.println("这里开始注册");

    }


    @Bean
    @ConditionalOnMissingBean(value = {DataSource.class})
    @ConditionalOnBean(value = {DataOperatorDto.class})
    public DynamicDataSource getDds() {
        return new DynamicDataSource();
    }
}
