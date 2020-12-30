package com.ranranx.aolie.core.handler;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.UpdateParamDefinition;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author xxl
 * @Description 数据新增或更新服务服务
 * @Date 2020/8/4 14:32
 * @Version V0.0.1
 **/
@Component
public class UpdateHandler<T extends UpdateParam> extends BaseHandler<T> {
    @Autowired
    private DataOperatorFactory factory;

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
    protected HandleResult handle(UpdateParam param) {
        HandleResult result = new HandleResult();
        if (param.getLstRows() == null || param.getLstRows().isEmpty()) {
            result.setErr("没有指定更新的数据");
            return result;
        }
        UpdateParamDefinition definition = new UpdateParamDefinition();
        BeanUtils.copyProperties(param, definition);
        definition.setTableName(param.getTable().getTableDto().getTableName());

        definition.setIdField(param.getTable().getIdFieldIncludeVersionCode());
        int count = factory.getDataOperatorByKey(param.getTable().getDsKey()).update(definition);
        result.setSuccess(true);
        result.setChangeNum(count);
        return result;
    }

}
