package com.ranranx.aolie.handler;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.exceptions.InvalidParamException;
import com.ranranx.aolie.exceptions.NotExistException;
import com.ranranx.aolie.interceptor.IOperInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/9 15:34
 * @Version V0.0.1
 **/
@Component
public class HandlerFactory {

    @Autowired
    private List<IDbHandler> lstHandler;

    @Autowired(required = false)
    private List<IOperInterceptor> lstInterceptor;

    public IDbHandler getHandler(String type, Object objExtinfo) {
        if (CommonUtils.isEmpty(type)) {
            throw new InvalidParamException("处理器类型不可以为空");

        }
        for (IDbHandler handler : lstHandler) {
            //这里只取优先级高的处理器
            if (handler.isCanHandle(type, objExtinfo)) {
                return handler;
            }
        }
        throw new NotExistException("处理器类型(" + type + ")");
    }

    @PostConstruct
    public void afterInit() {
        sort();
        setInterceptor();
    }

    private void sort() {
        CommonUtils.sortOrder(lstHandler);
        CommonUtils.sortOrder(lstInterceptor);
    }

    private void setInterceptor() {
        if (lstInterceptor == null || lstInterceptor.isEmpty()) {
            return;
        }
        for (IDbHandler handler : lstHandler) {
            if (handler instanceof BaseHandler) {
                ((BaseHandler) handler).setLstInterceptor(lstInterceptor);
            }
        }
    }
}
