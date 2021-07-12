package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.Ordered;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.param.OperParam;

import java.util.Map;

/**
 * 级联删除的处理
 * 级联删除的条件，1，存在关联， 1.1 如果关联字段可以为空，则清除值，1.2 如果关联字段不可为空，则再次删除
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/12 0012 8:05
 **/
@DbOperInterceptor
public class RelationManageOnDeleteInterceptor implements IOperInterceptor {

    @Override
    public HandleResult afterOper(OperParam param, String handleType, Map<String, Object> globalParamData, HandleResult result) {
        return null;
    }

    /**
     * 是否可以处理
     *
     * @param type
     * @param objExtinfo
     * @return
     */
    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return Constants.HandleType.TYPE_DELETE.equals(type);
    }

    @Override
    public int getOrder() {
        return Ordered.BASE_ORDER + 100;
    }
}
