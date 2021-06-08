package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.DeleteParam;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import com.ranranx.aolie.core.interfaces.ICacheRefTableChanged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/6/3 0003 11:31
 **/
@DbOperInterceptor
@Order(900)
public class CacheRefreshInterceptor implements IOperInterceptor {

    @Autowired
    private List<ICacheRefTableChanged> lstCaches;
    /**
     * 将缓存刷新器按表名进行分类
     */
    private Map<String, List<ICacheRefTableChanged>> mapInterceptor;

    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return !Constants.HandleType.TYPE_QUERY.equals(type);
    }

    @Override
    public HandleResult beforeReturn(Object param, String handleType, Map<String, Object> globalParamData, HandleResult handleResult) {
        if (lstCaches == null || lstCaches.isEmpty()) {
            return null;
        }
        initMapInterceptor();
        String tableName = getTableName(param);
        List<ICacheRefTableChanged> lst = mapInterceptor.get(tableName);
        if (lst == null || lst.isEmpty()) {
            return null;
        }
        lst.forEach(iCacheRefTableChanged -> iCacheRefTableChanged.refresh(tableName));
        return null;
    }

    private String getTableName(Object param) {
        if (param instanceof UpdateParam) {
            return ((UpdateParam) param).getTable().getTableDto().getTableName();
        } else if (param instanceof DeleteParam) {
            return ((DeleteParam) param).getTable().getTableDto().getTableName();
        } else if (param instanceof InsertParam) {
            return ((InsertParam) param).getTable().getTableDto().getTableName();
        }
        return "";
    }

    private void initMapInterceptor() {
        if (mapInterceptor != null) {
            return;
        }
        synchronized (CacheRefreshInterceptor.class) {
            if (mapInterceptor != null) {
                return;
            }
            mapInterceptor = new HashMap<>();
            lstCaches.forEach(iCacheRefTableChanged -> {
                List<String> careTables = iCacheRefTableChanged.getCareTables();
                if (careTables == null || careTables.isEmpty()) {
                    return;
                }
                careTables.forEach(tableName -> {
                    List<ICacheRefTableChanged> lstCache = mapInterceptor.computeIfAbsent(tableName, key -> {
                        return new ArrayList<>();
                    });
                    lstCache.add(iCacheRefTableChanged);
                });
            });
        }

    }
}
