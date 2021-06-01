package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.InsertParam;

import java.util.List;
import java.util.Map;

/**
 * 默认设置版本列的值
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/22 0022 15:12
 **/

@DbOperInterceptor
public class VersionCodeValueInterceptor implements IOperInterceptor {

    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return Constants.HandleType.TYPE_INSERT.equals(type);
    }

    @Override
    public HandleResult beforeOper(Object param, String handleType, Map<String, Object> globalParamData) throws InvalidException {
        InsertParam insertParam = (InsertParam) param;
        List<Map<String, Object>> lstRows = insertParam.getLstRows();
        if (lstRows == null || lstRows.isEmpty()) {
            return null;
        }
        String version = SessionUtils.getLoginVersion();
        if (CommonUtils.isEmpty(version)) {
            return null;
        }
        lstRows.forEach(row -> {
            if (CommonUtils.isEmpty(row.get(Constants.FixColumnName.VERSION_CODE))) {
                row.put(Constants.FixColumnName.VERSION_CODE, version);
            }
        });
        return null;
    }
}
