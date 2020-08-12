package com.ranranx.aolie.ds.dataoperator.multids;

import com.ranranx.aolie.common.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源
 *
 * @Author xxl
 * @Description
 * @Date 2020/8/11 15:46
 * @Version V0.0.1
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    @Autowired
    private List<DataSourceWrapper> lstWraper;

    @Override
    protected Object determineCurrentLookupKey() {
        logger.info("--> Using " + contextHolder.get() + " datasource.");
        return contextHolder.get();
    }


    public static void setDataSource(String sourceName) {
        contextHolder.set(sourceName);
    }


    public static String getType() {
        return contextHolder.get();
    }

    public static void cleanAll() {
        contextHolder.remove();
    }

    @Override
    public void afterPropertiesSet() throws BeansException {

        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        if (lstWraper == null) {
            return;
        }
        Iterator<DataSourceWrapper> iterator = lstWraper.iterator();
        String key;
        while (iterator.hasNext()) {
            DataSourceWrapper next = iterator.next();
            key = CommonUtils.makeKey(next.getDto().getName(), next.getDto().getVersionCode());
            logger.info("--> Add DataSource " + next.getDto().getName());
            targetDataSources.put(key, next.getDataSource());
        }
        this.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();

    }


}
