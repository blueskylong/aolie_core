package com.ranranx.aolie.engine;

import com.ranranx.aolie.engine.param.InsertParam;
import com.ranranx.aolie.exceptions.InvalidException;
import com.ranranx.aolie.interceptor.InsertInterceptor;
import com.ranranx.aolie.interceptor.UpdateInterceptor;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 数据新增或更新服务服务
 * @Date 2020/8/4 14:32
 * @Version V0.0.1
 **/
public class InsertHandler {
    private List<InsertInterceptor> lstInterceptor;

    /**
     * 更新或新增
     *
     * @return
     */
    public int saveOrUpdate(Map<String, Object> mapParam) throws Exception {

        InsertParam InsertParam = checkAndMakeInsertParam(mapParam);
        //查询前执行
        int num = doBeforeUpdate(InsertParam);
        //如果有拦截器直接返回数据,则不再向后执行
        if (num > 0) {
            return num;
        }
        num = doInsert(InsertParam);
        doAfterInsert(InsertParam, num);
        return num;
    }

    private void doAfterInsert(InsertParam insertParam, int num) {
        for (InsertInterceptor interceptor : lstInterceptor) {
            interceptor.afterInsert(insertParam, num);

        }
    }

    private int doInsert(InsertParam insertParam) {
        return 0;
    }

    private int doBeforeUpdate(InsertParam InsertParam) {
        for (InsertInterceptor interceptor : lstInterceptor) {
            int num = interceptor.beforeInsert(InsertParam);
            if (num > 0) {
                return num;
            }
        }
        return 0;
    }

    /**
     * 检查并生成查询参数
     *
     * @param mapParams
     * @return
     */
    private InsertParam checkAndMakeInsertParam(Map<String, Object> mapParams) throws InvalidException {
        //TODO
        return null;
    }

}
