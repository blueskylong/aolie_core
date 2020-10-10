package com.ranranx.aolie.core.ds.dataoperator;

import com.ranranx.aolie.core.common.CommonUtils;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/12 20:50
 * @Version V0.0.1
 **/
public class DataSourceUtils {
    /**
     * 数据库操作接口注册BEAN是的前缀
     */
    public static final String OPERATOR_PREFIX = "datasource_";

    /**
     * 生成数据库操作接口的注册Key
     *
     * @param key 生成的Bean的key,一般是DataOperatorDto的name和version生成.
     * @return
     */
    public static String makeDsKey(String key) {
        return OPERATOR_PREFIX + "_" + key;
    }

    public static String getKeyByDsKey(String dsKey) {
        return dsKey.substring(OPERATOR_PREFIX.length() + 1);
    }


    /**
     * 取得默认的数据源key
     *
     * @return
     */
    public static String getDefaultDataSourceKey() {
        return (OPERATOR_PREFIX + "_default__0");
    }

    /**
     * 默认的操作KEY
     *
     * @return
     */
    public static String getDefaultDataOperatorKey() {
        return "default__0";
    }

    /**
     * 生成数据库操作接口的注册Key
     *
     * @param name    DataOperatorDto的name
     * @param version
     * @return
     */
    public static String makeDsKey(String name, String version) {
        return makeDsKey(CommonUtils.makeKey(name, version));
    }

}
