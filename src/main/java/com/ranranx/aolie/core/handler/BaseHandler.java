package com.ranranx.aolie.core.handler;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.interceptor.IOperInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *
 * @date 2020/8/10 11:00
 * @version V0.0.1
 **/
public abstract class BaseHandler<T> implements IDbHandler {
    /**
     * 所有拦截器
     */
    protected List<IOperInterceptor> lstInterceptor;

    @Autowired
    private DataOperatorFactory operatorFactory;

    private Class<T> entityClass;


    /**
     * 默认可以处理的类型
     *
     * @return
     */
    abstract String getCanHandleType();

    public List<IOperInterceptor> getLstInterceptor() {
        return lstInterceptor;
    }

    public void setLstInterceptor(List<IOperInterceptor> lstInterceptor) {
        this.lstInterceptor = lstInterceptor;
    }

    protected HandleResult doAfterOperater(Object param, String handleType, Map<String, Object> mapGlobalParam, HandleResult result) {
        List<IOperInterceptor> validInterceptor = findValidInterceptor(getCanHandleType(), param);
        if (validInterceptor != null && !validInterceptor.isEmpty()) {
            HandleResult interResult;
            for (IOperInterceptor inter : validInterceptor) {
                interResult = inter.afterOper(param, handleType, mapGlobalParam, result);
                if (interResult != null) {
                    return interResult;
                }
            }
        }
        return null;

    }

    protected HandleResult doBeforeOperator(Object param, Map<String, Object> mapGlobalParam) {
        int num;
        List<IOperInterceptor> validInterceptor = findValidInterceptor(getCanHandleType(), param);
        HandleResult result = null;
        for (IOperInterceptor inter : validInterceptor) {
            result = inter.beforeOper(param, this.getCanHandleType(), mapGlobalParam);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * 处理操作
     *
     * @param mapParam
     * @return
     */
    @Override
    public HandleResult doHandle(Object mapParam) {
        HandleResult result = new HandleResult();
        result.setSuccess(true);
        double transaction = -1;
        if (needTransaction()) {
            beginTransaction();
        }
        try {
            //创建一个参数中转站,连通各个拦截器
            Map<String, Object> mapGlobalParam = new HashMap<>();
            T param = checkAndMakeParam(mapParam);
            //查询前执行
            HandleResult iResult = doBeforeOperator(param, mapGlobalParam);
            //如果有拦截器直接返回数据,则不再向后执行
            if (iResult != null) {
                return iResult;
            }
            result = handle(param);
            iResult = doAfterOperater(param, this.getCanHandleType(), mapGlobalParam, result);
            if (iResult != null) {
                return iResult;
            }
            iResult = doBeforeReturn(param, mapGlobalParam, result);

            if (iResult != null) {
                return iResult;
            }
            if (needTransaction() && iResult.isSuccess()) {
                commit();
            }
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErr(e.getMessage());
            if (needTransaction()) {
                rollback();
            }
            e.printStackTrace();
            return result;
        }
    }

    /**
     * 执行的地方
     *
     * @param param
     * @return
     */
    protected abstract HandleResult handle(T param);

    /**
     * 生成结构化参数
     *
     * @param mapParam
     * @return
     */

    protected T checkAndMakeParam(Object mapParam) {
        //TODO_2 要做检查
        T param;
        try {
            if (entityClass == null) {
                entityClass = (Class<T>) this.getClass().getTypeParameters()[0].getBounds()[0];
            }
            if (mapParam.getClass() == entityClass) {
                return (T) mapParam;
            }
            param = CommonUtils.populateBean(entityClass, (Map<String, Object>) mapParam);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return (T) param;
    }

    /**
     * 是否可以处理指定类型的请求
     *
     * @param type
     * @return
     * @Param type 请求类型
     */
    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return getCanHandleType().equals(type);
    }

    protected HandleResult doBeforeReturn(Object param, Map<String, Object> mapGlobalParam, HandleResult result) {
        List<IOperInterceptor> validInterceptor = findValidInterceptor(getCanHandleType(), param);
        HandleResult hResult = null;
        if (validInterceptor != null && !validInterceptor.isEmpty()) {
            for (IOperInterceptor inter : validInterceptor) {
                hResult = inter.beforeReturn(param, this.getCanHandleType(), mapGlobalParam, result);
                if (hResult != null) {
                    return hResult;
                }
            }
        }
        return null;
    }

    /**
     * 查找可用的拦截器
     *
     * @param type
     * @param extParams
     * @return
     */
    List<IOperInterceptor> findValidInterceptor(String type, Object extParams) {
        List<IOperInterceptor> lstResult = new ArrayList<>();
        for (IOperInterceptor iOperInterceptor : lstInterceptor) {
            if (iOperInterceptor.isCanHandle(type, extParams)) {
                lstResult.add(iOperInterceptor);
            }
        }
        return lstResult;
    }
}
