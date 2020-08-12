package com.ranranx.aolie.handler;

import com.ranranx.aolie.common.Constants;
import com.ranranx.aolie.handler.param.DeleteParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xxl
 * @Description 数据删除服务
 * @Date 2020/8/4 14:35
 * @Version V0.0.1
 **/
@Component
public class DeleteHandler<T extends DeleteParam> extends BaseHandler<T> {

    /**
     * 默认可以处理的类型
     *
     * @return
     */
    @Override
    String getCanHandleType() {
        return Constants.HandleType.TYPE_DELETE;
    }


    @Override
    protected HandleResult handle(DeleteParam deleteParam) {
        deleteParam.getTable().getTableDto().getDataOperId();
        return null;
    }

    /**
     * 生成结构化参数
     *
     * @param mapParam
     * @return
     */
    @Override
    protected T checkAndMakeParam(Map<String, Object> mapParam) {
        //删除需要指定表信息
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ids", new long[]{1L});
        DeleteParam param = new DeleteParam();
        try {
            org.apache.commons.beanutils.BeanUtils.populate(param, map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        DeleteHandler handler = new DeleteHandler<DeleteParam>();
        handler.checkAndMakeParam(null);
    }
}
