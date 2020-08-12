package com.ranranx.aolie.handler;

import com.ranranx.aolie.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.interceptor.IOperInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/10 11:00
 * @Version V0.0.1
 **/
public abstract class BaseHandler<T> implements IDbHandler {

    /**
     * 所有拦截器
     */
    protected List<IOperInterceptor> lstInterceptor;

    @Autowired
    private DataOperatorFactory operatorFactory;


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

    protected void doAfterOperater(Object param, HandleResult result) {
        List<IOperInterceptor> validInterceptor = findValidInterceptor(getCanHandleType(), param);
        if (validInterceptor != null && !validInterceptor.isEmpty()) {
            for (IOperInterceptor inter : validInterceptor) {
                inter.afterOper(param, result);
            }
        }

    }

    protected HandleResult doBeforeOperator(Object param) {
        int num;
        List<IOperInterceptor> validInterceptor = findValidInterceptor(getCanHandleType(), param);
        HandleResult result = null;
        for (IOperInterceptor inter : validInterceptor) {
            result = inter.beforeOper(param);
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
    public HandleResult doHandle(Map<String, Object> mapParam) {
        HandleResult result = new HandleResult();
        result.setSuccess(true);
        double transaction = -1;
        if (needTransaction()) {
            beginTransaction();
        }
        try {
            T param = checkAndMakeParam(mapParam);
            //查询前执行
            HandleResult iResult = doBeforeOperator(param);
            //如果有拦截器直接返回数据,则不再向后执行
            if (iResult != null) {
                return iResult;
            }
            result = handle(param);
            doAfterOperater(param, result);
            iResult = doBeforeReturn(param, result);
            if (needTransaction()) {
                commit();
            }
            if (iResult != null) {
                return iResult;
            }
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErr(e.getMessage());
            if (needTransaction()) {
                rollback();
            }
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

    protected abstract T checkAndMakeParam(Map<String, Object> mapParam);

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

    protected HandleResult doBeforeReturn(Object param, HandleResult result) {
        List<IOperInterceptor> validInterceptor = findValidInterceptor(getCanHandleType(), param);
        HandleResult hResult = null;
        if (validInterceptor != null && !validInterceptor.isEmpty()) {
            for (IOperInterceptor inter : validInterceptor) {
                hResult = inter.beforeReturn(param, result);
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
