package com.ranranx.aolie.engine;

import com.ranranx.aolie.engine.param.QueryParam;
import com.ranranx.aolie.engine.param.UpdateParam;
import com.ranranx.aolie.exceptions.InvalidException;
import com.ranranx.aolie.interceptor.UpdateInterceptor;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 数据新增或更新服务服务
 * @Date 2020/8/4 14:32
 * @Version V0.0.1
 **/
public class UpdateHandler {
    private List<UpdateInterceptor> lstInterceptor;

    /**
     * 更新或新增
     *
     * @return
     */
    public int saveOrUpdate(Map<String, Object> mapParam) throws Exception {

        UpdateParam updateParam = checkAndMakeUpdateParam(mapParam);
        //查询前执行
        int num = doBeforeUpdate(updateParam);
        //如果有拦截器直接返回数据,则不再向后执行
        if (num > 0) {
            return num;
        }
        num = doUpdate(updateParam);
        doAfterUpdate(updateParam, num);
        return num;
    }

    private void doAfterUpdate(UpdateParam updateParam, int num) {
        for (UpdateInterceptor interceptor : lstInterceptor) {
            interceptor.afterUpdate(updateParam, num);

        }
    }

    //TODO
    private int doUpdate(UpdateParam updateParam) {
        return 0;
    }

    private int doBeforeUpdate(UpdateParam updateParam) {
        for (UpdateInterceptor interceptor : lstInterceptor) {
            int num = interceptor.beforeUpdate(updateParam);
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
    private UpdateParam checkAndMakeUpdateParam(Map<String, Object> mapParams) throws InvalidException {
        //TODO
        return null;
    }

}
