package com.ranranx.aolie.core.handler;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.InsertParamDefinition;
import com.ranranx.aolie.core.exceptions.IllegalOperatorException;
import com.ranranx.aolie.core.exceptions.InvalidConfigException;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.handler.param.InsertParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 数据新增或更新服务服务
 * @Date 2020/8/4 14:32
 * @Version V0.0.1
 **/
@Component
public class InsertHandler<T extends InsertParam> extends BaseHandler<T> {


    @Autowired
    private DataOperatorFactory factory;

    /**
     * 执行的地方
     *
     * @param param
     * @return
     */
    @Override
    protected HandleResult handle(InsertParam param) {
        if (param == null) {
            throw new InvalidParamException("没有提供插入参数");
        }
        if (param.getLstRows() == null || param.getLstRows().isEmpty()) {
            throw new InvalidParamException("没有提供插入行数据");
        }
        List<long[]> keys = genKeys(param);
        InsertParamDefinition definition = new InsertParamDefinition();
        definition.setTableName(param.getTable().getTableDto().getTableName());
        definition.setLstRows(param.getLstRows());
        HandleResult result = new HandleResult();
        result.setSuccess(false);
        try {
            int num = factory.getDataOperatorByKey(param.getTable().getDsKey()).insert(definition);
            result.setChangeNum(num);
            List<Map<String, Object>> lstData = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ConstFieldName.CHANGE_KEYS_FEILD, keys);
            lstData.add(map);
            result.setLstData(lstData);
            if (num == param.getLstRows().size()) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            IllegalOperatorException ex = new IllegalOperatorException(e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
        return result;
    }

    private List<long[]> genKeys(InsertParam param) {

        //这里需要先生成数据的主键
        List<Column> keyColumn = param.getTable().getKeyColumn();
        String errTable = "表:[" + param.getTable().getTableDto().getTableId() + "]"
                + param.getTable().getTableDto().getTableName() + " ";

        if (keyColumn == null || keyColumn.size() != 1) {
            throw new InvalidConfigException("主键字段配置不正确,需要一个且只有一个长整型字段");
        }
        //TODO_2 这里只处理了单一主键的情况,即要求每一张表有独立的单一主键,除了VERSION_CODE 字段.
        if (keyColumn == null || keyColumn.size() != 1) {
            throw new InvalidConfigException(errTable + " 只可以设置一个字段为主键字段");
        }
        String keyField = keyColumn.get(0).getColumnDto().getFieldName();
        Long tableId = param.getTable().getTableDto().getTableId();
        //更新表ID值
        List<long[]> lstChangeId = new ArrayList();
        for (Map<String, Object> row : param.getLstRows()) {
            lstChangeId.add(genAndUpdateKey(row, tableId, keyField, errTable));
        }
        return lstChangeId;
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

    /**
     * 检查并生成主键,返回老的主键与新的主键值对
     *
     * @param row
     * @return
     */
    private long[] genAndUpdateKey(Map<String, Object> row, Long tableId, String keyField, String errTable) {
        Object obj = row.get(keyField);
        if (!CommonUtils.isNumber(obj.toString()) || Long.parseLong(obj.toString()) >= 0) {
            throw new InvalidParamException(errTable + " 指定的值不符合预期,预期为一负数");
        }
        long nextId = IdGenerator.getNextId("table_" + SessionUtils.getLoginVersion() + "_" + tableId);
        row.put(keyField, nextId);
        return new long[]{Long.parseLong(obj.toString()), nextId};
    }
}
