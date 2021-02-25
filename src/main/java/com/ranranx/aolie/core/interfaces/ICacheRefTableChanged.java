package com.ranranx.aolie.core.interfaces;

import java.util.List;

/**
 * 表数据发生改变,可以得到通知的操作,目前用于如果有缓存,则相关数据变化,此缓存也要刷新,如缓存的权限结构等
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/22 0022 13:22
 **/
public interface ICacheRefTableChanged {

    /**
     * 取得关心的表名
     *
     * @return
     */
    List<String> getCareTables();

    /**
     * 执行刷新操作
     *
     * @param tableName
     */
    void refresh(String tableName);
}
