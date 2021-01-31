package com.ranranx.aolie.core.interceptor;


import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.exceptions.InvalidConfigException;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 本拦截器为辅助作用, 收集更新前ID, 供验证器使用
 * @Date 2021/1/29 0029 14:35
 * @Version V0.0.1
 **/
@DbOperInterceptor
public class UpdateRowIdInterceptor implements IOperInterceptor {
    public static final String PARAM_IDS = "G_UPDATE_ID";

    @Autowired
    private HandlerFactory handlerFactory;

    @Override
    public int getOrder() {
        return BASE_ORDER - 20;
    }

    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return Constants.HandleType.TYPE_UPDATE.equals(type);
    }

    @Override
    public HandleResult beforeOper(Object param, String handleType,
                                   Map<String, Object> globalParamData) throws InvalidException {

        UpdateParam updateParam = (UpdateParam) param;
        List<Object> lstKey = getKeys(updateParam);
        //如果没有指定主键数据,则要根据条件去查询主键数据
        if (lstKey == null || lstKey.isEmpty()) {
            lstKey = findKey(updateParam);
        }
        //将更新的数据ID 放到全局参数中去
        if (lstKey != null && !lstKey.isEmpty()) {
            globalParamData.put(PARAM_IDS, lstKey);
        }

        return null;
    }

    private List<Object> findKey(UpdateParam param) {
        //如果没有指定条件,则不能执行查询
        if (param.getCriteria().isEmpty()) {
            return null;
        }
        QueryParam queryParam = new QueryParam();
        queryParam.setTable(new TableInfo[]{param.getTable()});
        List<Column> lstCol = param.getTable().getKeyColumn();
        if (lstCol == null || lstCol.size() != 1) {
            throw new InvalidConfigException("表主键配置不确,只允许一个主键字段");
        }

        String keyName = lstCol.get(0).getColumnDto().getFieldName();
        Field field = new Field();
        field.setFieldName(keyName);
        field.setTableName(param.getTable().getTableDto().getTableName());
        List<Field> lstField = new ArrayList<>();
        lstField.add(field);
        queryParam.setFields(lstField);
        queryParam.addCriteria(param.getCriteria());
        HandleResult result = handlerFactory.handleQuery(queryParam);
        if (!result.isSuccess()) {
            return null;
        }
        List<Map<String, Object>> lstData = result.getLstData();
        if (lstData == null || lstData.isEmpty()) {
            return null;
        }
        List<Object> lstResult = new ArrayList<>();
        lstData.forEach(map -> {
            lstResult.add(map.get(keyName));
        });
        return lstResult;
    }


    private List<Object> getKeys(UpdateParam param) {
        String keyField = param.getTable().getKeyField();
        List<Map<String, Object>> lstRows = param.getLstRows();
        if (lstRows == null || lstRows.isEmpty()) {
            return null;
        }
        List<Object> result = new ArrayList<>();
        Object value;
        for (Map<String, Object> row : lstRows) {
            value = row.get(keyField);
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }
}
