package com.ranranx.aolie.core.interceptor;


import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.*;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 本拦截器为辅助作用, 收集更新前ID, 供验证器使用
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/1/29 0029 14:35
 **/
@DbOperInterceptor
public class GetRowIdInterceptor implements IOperInterceptor {
    private Logger logger = LoggerFactory.getLogger(GetRowIdInterceptor.class.getName());
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
                || Constants.HandleType.TYPE_INSERT.equals(type)
                || Constants.HandleType.TYPE_DELETE.equals(type);
    }

    @Override
    public HandleResult beforeOper(OperParam param, String handleType,
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
        } else if (param instanceof DeleteParam) {
            DeleteParam deleteParam = (DeleteParam) param;
            //如果直接指定了ID,则
            if (deleteParam.getIds() != null && deleteParam.getIds().isEmpty()) {
                globalParamData.put(PARAM_IDS, deleteParam.getIds());
            } else {
                //如果是按照条件来删除的,则需要先查询
                if (param.isNoFilter()) {
                    //为了安全,没有条件的删除,不做处理,如果需要处理,则手动增加等于true的条件
                    return null;
                }
                List<Criteria> lstCriteria = deleteParam.getCriterias();
                List lstKey = findTableKeys(deleteParam.getTable(), lstCriteria);
                globalParamData.put(PARAM_IDS, lstKey);
            }
        }
        return null;
    }

    private List<Object> findTableKeys(TableInfo tableInfo, List<Criteria> lstFilter) {
        QueryParam param = new QueryParam();
        String field = tableInfo.getKeyField();
        param.setFields(Arrays.asList(getKeyField(tableInfo)));
        param.setTable(tableInfo);
        param.setCriterias(lstFilter);
        HandleResult result = handlerFactory.handleQuery(param);

        if (result.isSuccess()) {
            //收集主键
            List<Object> lstObj = new ArrayList<>();
            for (Map row : result.getLstData()) {
                lstObj.add(row.get(field));
            }
            return lstObj;
        } else {
            logger.error("查询删除数据失败" + result.getErr());
            return null;
        }

    }

    private Field getKeyField(TableInfo tableInfo) {
        String keyField = tableInfo.getKeyField();
        Field field = new Field();
        field.setTableName(tableInfo.getTableDto().getTableName());
        field.setFieldName(keyField);
        return field;
    }

    @Override
    public HandleResult afterOper(OperParam param, String handleType, Map<String, Object> globalParamData,
                                  HandleResult result) {
        if (!result.isSuccess()) {
            return null;
        }
        if (param instanceof InsertParam) {
            List<List<Long>> lstId = (List<List<Long>>) ((Map<String, Object>) result.getLstData().get(0))
                    .get(Constants.ConstFieldName.CHANGE_KEYS_FEILD);
            if (lstId == null || lstId.isEmpty()) {
                return null;
            }
            List<Object> lst = new ArrayList<>();
            for (List<Long> ids : lstId) {
                lst.add(ids.get(1));
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
        queryParam.setTable(param.getTable());
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
        if (param.getTable() == null) {
            return null;
        }
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
