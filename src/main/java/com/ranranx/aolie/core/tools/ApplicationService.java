package com.ranranx.aolie.core.tools;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author xxl
 *
 * @date 2020/12/23 0023 10:34
 * @version V0.0.1
 **/
@Component
public class ApplicationService implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    private ApplicationService() {
        super();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationService.applicationContext = applicationContext;
    }

    /**
     * 取得容器中的服务
     *
     * @param serviceName
     * @return
     */
    public static Object getService(String serviceName) {
        return applicationContext.getBean(serviceName);
    }
}
