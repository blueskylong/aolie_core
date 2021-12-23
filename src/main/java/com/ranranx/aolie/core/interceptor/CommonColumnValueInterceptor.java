package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.OperParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;

import java.sql.Date;
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
public class CommonColumnValueInterceptor implements IOperInterceptor {

    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return Constants.HandleType.TYPE_INSERT.equals(type) || Constants.HandleType.TYPE_UPDATE.equals(type);
    }

    @Override
    public HandleResult beforeOper(OperParam param, String handleType, Map<String, Object> globalParamData) throws InvalidException {
        Date date = new Date(System.currentTimeMillis());
        final Long userId = SessionUtils.getLoginUser() != null ? SessionUtils.getLoginUser().getUserId() : -1L;
        if (Constants.HandleType.TYPE_INSERT.equals(handleType)) {
            InsertParam insertParam = (InsertParam) param;
            List<Map<String, Object>> lstRows = insertParam.getLstRows();
            if (lstRows == null || lstRows.isEmpty()) {
                return null;
            }
            lstRows.forEach(row -> {
                row.put(Constants.FixColumnName.CREATE_DATE, date);
                row.put(Constants.FixColumnName.CREATE_USER, userId);
                row.put(Constants.FixColumnName.LAST_UPDATE_DATE, date);
                row.put(Constants.FixColumnName.LAST_UPDATE_USER, userId);
            });
        } else {
            UpdateParam param1 = (UpdateParam) param;
            Map map = param1.getMapSetValues();
            if (map != null && !map.isEmpty()) {
                map.put(Constants.FixColumnName.LAST_UPDATE_DATE, date);
                map.put(Constants.FixColumnName.LAST_UPDATE_USER, userId);
            }
            if (param1.getLstRows() != null && !param1.getLstRows().isEmpty()) {
                param1.getLstRows().forEach(row -> {
                    row.put(Constants.FixColumnName.CREATE_DATE, date);
                    row.put(Constants.FixColumnName.CREATE_USER, userId);
                    row.put(Constants.FixColumnName.LAST_UPDATE_DATE, date);
                    row.put(Constants.FixColumnName.LAST_UPDATE_USER, userId);
                });
            }

        }
        return null;
    }
}
