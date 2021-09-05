package com.ranranx.aolie.core.service;

import com.ranranx.aolie.core.common.BaseDto;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.FieldOrder;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.DeleteParam;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import com.ranranx.aolie.core.interfaces.IBaseDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OrderBy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/6 0006 9:08
 **/
@Service("CommonBaseService")
public class BaseDbService implements IBaseDbService {
    protected static Map<String, List<FieldOrder>> mapOrders = new HashMap<>();
    @Autowired
    protected HandlerFactory factory;


    /**
     * 如果可以指定方案,则需要先指定方案,否则可能因为表的多重定义(一张表在多个方案中用到)不可以操作
     */
    protected Long schemaId = -1L;

    @Override
    @Transactional(readOnly = false)
    public <T extends BaseDto> int insert(T dto, Long schemaId) {
        if (dto == null) {
            return 0;
        }
        if (schemaId == null || schemaId < 1) {
            schemaId = this.schemaId;
        }
        InsertParam param = new InsertParam();
        param.setObject(dto, schemaId);
        return factory.handleInsert(param).getChangeNum();
    }

    @Override
    @Transactional(readOnly = false)
    public <T extends BaseDto> int insertBatch(List<T> dto, Long schemaId) {
        if (dto == null) {
            return 0;
        }
        if (schemaId == null || schemaId < 1) {
            schemaId = this.schemaId;
        }
        InsertParam param = new InsertParam();
        param.setObjects(dto, schemaId);
        return factory.handleInsert(param).getChangeNum();
    }

    @Override
    @Transactional(readOnly = false)
    public int insertBatch(List<Map<String, Object>> lstData, TableInfo tableInfo) {
        if (lstData == null) {
            return 0;
        }
        InsertParam param = new InsertParam();
        param.setTable(tableInfo);
        param.setLstRows(lstData);
        return factory.handleInsert(param).getChangeNum();
    }

    @Override
    @Transactional(readOnly = false)
    public <T extends BaseDto> int delete(T dto, Long schemaId) {
        if (dto == null) {
            return 0;
        }
        if (schemaId == null || schemaId < 1) {
            schemaId = this.schemaId;
        }
        DeleteParam param = new DeleteParam();
        param.setOperDto(schemaId, dto, getVersionCode(dto));
        return factory.handleDelete(param).getChangeNum();
    }

    /**
     * 根据条件删除，只限等于条件，条件不可以为空
     *
     * @param dto
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseDto> int deleteById(T dto, Long schemaId) {
        if (dto == null) {
            return 0;
        }
        if (schemaId == null || schemaId < 1) {
            schemaId = this.schemaId;
        }

        TableInfo tableInfo = SchemaHolder.findTableByDto(dto.getClass(), schemaId, getVersionCode(dto));
        Object id = CommonUtils.getObjectValue(dto, CommonUtils.toCamelStr(tableInfo.getKeyField()));
        DeleteParam param = DeleteParam.deleteById(schemaId, dto.getClass(), id, getVersionCode(dto));
        return factory.handleDelete(param).getChangeNum();
    }

    @Override
    public <T extends BaseDto> T queryOne(T dto, Long schemaId) {
        if (dto == null) {
            return null;
        }
        if (schemaId == null || schemaId < 1) {
            schemaId = this.schemaId;
        }
        QueryParam param = new QueryParam();
        param.setFilterObjectAndTableAndResultType(schemaId, getVersionCode(dto), dto);
        HandleResult lstResult = factory.handleQuery(param);
        if (lstResult.isSuccess()) {
            if (lstResult.getData() != null && !((List) lstResult.getData()).isEmpty()) {
                return (T) ((List) lstResult.getData()).get(0);
            }
        }
        return null;
    }

    @Override
    public <T extends BaseDto> List<T> queryList(T dto, Long schemaId) {
        if (dto == null) {
            return null;
        }
        if (schemaId == null || schemaId < 1) {
            schemaId = this.schemaId;
        }
        QueryParam param = new QueryParam();
        param.setFilterObjectAndTableAndResultType(schemaId, getVersionCode(dto), dto);
        //查询排序
        param.setLstOrder(getFieldOrder(dto.getClass()));

        HandleResult lstResult = factory.handleQuery(param);
        if (lstResult.isSuccess()) {
            return (List<T>) lstResult.getData();
        }
        return null;
    }

    /**
     * 查询多条信息
     *
     * @param dto
     * @return
     */
    @Override
    public <T extends BaseDto> List<Map<String, Object>> queryMapList(T dto, Long schemaId) {
        if (dto == null) {
            return null;
        }
        if (schemaId == null || schemaId < 1) {
            schemaId = this.schemaId;
        }
        QueryParam param = new QueryParam();
        param.setFilterObjectAndTable(schemaId, getVersionCode(dto), dto);
        //查询排序
        param.setLstOrder(getFieldOrder(dto.getClass()));

        HandleResult lstResult = factory.handleQuery(param);
        if (lstResult.isSuccess()) {
            return lstResult.getData();
        }
        return null;
    }

    private <T extends BaseDto> String getVersionCode(T baseDto) {
        String version = baseDto.getVersionCode();
        if (CommonUtils.isEmpty(version)) {
            version = SessionUtils.getLoginVersion();
            if (CommonUtils.isEmpty(version)) {
                version = SessionUtils.getDefaultVersion();
            }
        }
        return version;
    }

    @Override
    @Transactional(readOnly = false)
    public <T extends BaseDto> int updateByIds(List<T> lstDto, boolean isSelective, Long schemaId) {
        if (lstDto == null || lstDto.isEmpty()) {
            return 0;
        }
        if (schemaId == null || schemaId < 1) {
            schemaId = this.schemaId;
        }
        UpdateParam param = UpdateParam.genUpdateByObjects(schemaId, getVersionCode(lstDto.get(0)), lstDto, isSelective);
        HandleResult result = factory.handleUpdate(param);
        if (result.isSuccess()) {
            return result.getChangeNum();
        }
        return 0;
    }

    public static List<FieldOrder> getFieldOrder(Class<?> clazz) {
        String tableName = CommonUtils.getTableName(clazz);
        if (CommonUtils.isEmpty(tableName)) {
            return null;
        }
        if (mapOrders.get(tableName) != null) {
            return mapOrders.get(tableName);
        }
        List<FieldOrder> result = new ArrayList<>();
        mapOrders.put(tableName, result);
        int index = 0;
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return null;
        }
        for (Field field : fields) {
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            if (orderBy != null) {
                FieldOrder order = new FieldOrder();
                order.setTableName(tableName);
                order.setField(CommonUtils.convertToUnderline(field.getName()));
                order.setOrder(index++);
                String value = orderBy.value();
                order.setAsc(CommonUtils.isEmpty(value) || "ASC".equalsIgnoreCase(value));
                result.add(order);
            }
        }
        return result;
    }
}
