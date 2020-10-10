package com.ranranx.aolie.core.handler;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author xxl
 * @Description 数据新增或更新服务服务
 * @Date 2020/8/4 14:32
 * @Version V0.0.1
 **/
@Component
public class UpdateHandler<T extends UpdateParam> extends BaseHandler<T> {

    /**
     * 默认可以处理的类型
     *
     * @return
     */
    @Override
    String getCanHandleType() {
        return Constants.HandleType.TYPE_UPDATE;
    }

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
        return (T) new UpdateParam();
    }
}
