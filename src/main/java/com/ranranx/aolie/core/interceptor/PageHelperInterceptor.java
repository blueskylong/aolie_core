package com.ranranx.aolie.core.interceptor;

import com.github.pagehelper.PageHelper;
import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.QueryParam;

import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/27 0027 16:25
 **/
@DbOperInterceptor
public class PageHelperInterceptor implements IOperInterceptor {
    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return Constants.HandleType.TYPE_QUERY.equalsIgnoreCase(type);
    }

    @Override
    public HandleResult beforeOper(Object param, String handleType, Map<String, Object> globalParamData) throws InvalidException {
        QueryParam queryParam = (QueryParam) param;
        if (queryParam.getPage() != null) {
            PageHelper.startPage(queryParam.getPage().getCurrentPage(), queryParam.getPage().getPageSize());
        }

        return null;
    }

}
