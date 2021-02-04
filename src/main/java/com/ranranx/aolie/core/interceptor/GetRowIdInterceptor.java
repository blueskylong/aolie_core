package com.ranranx.aolie.core.interceptor;


import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 本拦截器为辅助作用, 收集更新前ID, 供验证器使用
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/1/29 0029 14:35
 **/
@DbOperInterceptor
public class GetRowIdInterceptor implements IOperInterceptor {
    public static final String PARAM_IDS = "G_UPDATE_ID";
    public static final String PARAM_UPDATE_FIELDS = "G_UPDATE_FIELDS";

    @Autowired
    private HandlerFactory handlerFactory;

    @Override
    public int getOrder() {
        return BASE_ORDER - 20;
    }

    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return Constants.HandleType.TYPE_UPDATE.equals(type)
                || Constants.HandleType.TYPE_INSERT.equals(type);
    }

    @Override
    public HandleResult beforeOper(Object param, String handleType,
                                   Map<String, Object> globalParamData) throws InvalidException {

        if (param instanceof UpdateParam) {
            UpdateParam updateParam = (UpdateParam) param;
            List<Object> lstKey = getKeys(updateParam);
            //如果没有指定主键数据,则要根据条件去查询主键数据
            if (lstKey == null || lstKey.isEmpty()) {
                lstKey = findKey(updateParam);
            }
            //将更新的字段入到全局参数里
            List<String> updateFields = findUpdateFields(updateParam.getLstRows());
            if (updateFields != null) {
                globalParamData.put(PARAM_UPDATE_FIELDS, updateFields);
            }
            //将更新的数据ID 放到全局参数中去
            if (lstKey != null && !lstKey.isEmpty()) {
                globalParamData.put(PARAM_IDS, lstKey);
            }
        }
        return null;
    }

    @Override
    public HandleResult afterOper(Object param, String handleType, Map<String, Object> globalParamData,
                                  HandleResult result) {
        if (!result.isSuccess()) {
            return null;
        }
        if (param instanceof InsertParam) {
            List<Map<String, Object>> lstData = result.getLstData();
            List<long[]> lstId = (List<long[]>) result.getLstData().get(0)
                    .get(Constants.ConstFieldName.CHANGE_KEYS_FEILD);
            if (lstId == null || lstId.isEmpty()) {
                return null;
            }
            List<Object> lst = new ArrayList<>();
            for (long[] ids : lstId) {
                lst.add(ids[1]);
            }
            globalParamData.put(PARAM_IDS, lst);
        }
        return null;
    }

    /**
     * 取得要更新的字段信息
     *
     * @param rows
     * @return
     */
    List<String> findUpdateFields(List<Map<String, Object>> rows) {
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Iterator<String> iterator = row.keySet().iterator();
            while (iterator.hasNext()) {
                String fieldName = iterator.next();
                if (result.indexOf(fieldName) == -1) {
                    result.add(fieldName);
                }
            }
        }
        return result;
    }

    private List<Object> findKey(UpdateParam param) {
        //如果没有指定条件,则不能执行查询
        if (param.getCriteria().isEmpty()) {
            return null;
        }
        QueryParam queryParam = new QueryParam();
        queryParam.setTable(new TableInfo[]{param.getTable()});
        String keyName = param.getTable().getKeyField();
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
