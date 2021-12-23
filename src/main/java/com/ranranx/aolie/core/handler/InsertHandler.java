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
import com.ranranx.aolie.core.exceptions.UnknownException;
import com.ranranx.aolie.core.handler.param.InsertParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author xxl
 * 数据新增或更新服务服务
 * @version V0.0.1
 * @date 2020/8/4 14:32
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
        try {
            if (param.getSqlExp() != null) {
                return HandleResult.success(factory.getDefaultDataOperator().executeDirect(param.getSqlExp().getExecuteMap()));
            }
            if (param == null) {
                throw new InvalidParamException("没有提供插入参数");
            }
            if (param.getLstRows() == null || param.getLstRows().isEmpty()) {
                throw new InvalidParamException("没有提供插入行数据");
            }
            List<List<Long>> keys = genKeys(param);
            InsertParamDefinition definition = new InsertParamDefinition();
            definition.setTableName(param.getTable().getTableDto().getTableName());
            definition.setLstRows(param.getLstRows());
            HandleResult result = new HandleResult();
            result.setSuccess(false);

            int num = factory.getDataOperatorByKey(param.getTable().getDsKey()).insert(definition);
            result.setChangeNum(num);
            List<Map<String, Object>> lstData = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ConstFieldName.CHANGE_KEYS_FEILD, keys);
            lstData.add(map);
            result.setData(lstData);
            if (num == param.getLstRows().size()) {
                result.setSuccess(true);
            }
            return result;
        } catch (Exception e) {
            IllegalOperatorException ex = new IllegalOperatorException(e.getMessage());
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }

    private List<List<Long>> genKeys(InsertParam param) {

        //这里需要先生成数据的主键
        try {
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
            List<List<Long>> lstChangeId = new ArrayList();
            for (Map<String, Object> row : param.getLstRows()) {
                lstChangeId.add(genAndUpdateKey(row, tableId, keyField, errTable));
            }
            return lstChangeId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnknownException(e.getMessage());

        }
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
    private List<Long> genAndUpdateKey(Map<String, Object> row, Long tableId, String keyField, String errTable) {
        Object obj = row.get(keyField);
        if (obj != null && (!CommonUtils.isNumber(obj.toString()) || Long.parseLong(obj.toString()) >= 0)) {
            throw new InvalidParamException(errTable + " 指定的值不符合预期,预期为一负数,或为空");
        }
        long nextId = IdGenerator.getNextId("table_" + SessionUtils.getLoginVersion() + "_" + tableId);
        row.put(keyField, nextId);

        return Arrays.asList(obj == null ? -1 * IdGenerator.getNextId("") : Long.parseLong(obj.toString()), nextId);
    }
}
