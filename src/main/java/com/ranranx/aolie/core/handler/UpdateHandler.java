package com.ranranx.aolie.core.handler;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.UpdateParamDefinition;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 数据新增或更新服务服务
 * @version V0.0.1
 * @date 2020/8/4 14:32
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
        if (param.getSqlExp() != null) {
            return HandleResult.success(factory.getDefaultDataOperator().executeDirect(param.getSqlExp().getExecuteMap()));
        }
        HandleResult result = new HandleResult();
        List<Map<String, Object>> lstData = param.getLstRows();
        if (lstData == null || lstData.isEmpty()) {
            result.setErr("没有指定更新的数据");
            return result;
        }
        UpdateParamDefinition definition = new UpdateParamDefinition();
        BeanUtils.copyProperties(param, definition);
        definition.setTableName(param.getTable().getTableDto().getTableName());
        //如果表中含有版本信息,则补全更新条件
        if (param.getTable().findColumnByName(Constants.FixColumnName.VERSION_CODE) != null) {
            for (Map<String, Object> row : lstData) {
                if (CommonUtils.isEmpty(row.get(Constants.FixColumnName.VERSION_CODE))) {
                    row.put(Constants.FixColumnName.VERSION_CODE, SessionUtils.getLoginVersion());
                }
            }
        }
        definition.setIdField(param.getTable().getIdFieldIncludeVersionCode());
        int count = factory.getDataOperatorByKey(param.getTable().getDsKey()).update(definition);
        result.setSuccess(true);
        result.setChangeNum(count);
        return result;
    }

}
