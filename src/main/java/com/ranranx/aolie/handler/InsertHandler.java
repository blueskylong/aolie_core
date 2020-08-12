package com.ranranx.aolie.handler;

import com.ranranx.aolie.common.Constants;
import com.ranranx.aolie.handler.param.InsertParam;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author xxl
 * @Description 数据新增或更新服务服务
 * @Date 2020/8/4 14:32
 * @Version V0.0.1
 **/
@Component
public class InsertHandler<T extends InsertParam> extends BaseHandler<T> {


    /**
     * 执行的地方
     *
     * @param param
     * @return
     */
    @Override
    protected HandleResult handle(T param) {
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
        //TODO
        return (T) new InsertParam();
    }


    /**
     * 默认可以处理的类型
     *
     * @return
     */
    @Override
    String getCanHandleType() {
        return Constants.HandleType.TYPE_INSERT;
    }
}
