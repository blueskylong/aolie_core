package com.ranranx.aolie.core.service;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.JQParameter;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.exceptions.*;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.DeleteParam;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author xxl
 * 进行数据操作, 增删改查
 * @version V0.0.1
 * @date 2020/12/14 16:34
 **/
@Service
@Transactional(readOnly = true)
public class DmDataService {

    @Autowired
    private HandlerFactory handlerFactory;

    /**
     * 查询分模块的分页数据
     *
     * @param blockId
     * @param queryParams
     * @return
     * @throws Exception
     */
    public HandleResult findBlockDataForPage(Long blockId, JQParameter queryParams) throws Exception {
        QueryParam queryParam = queryParams.getQueryParam();
        queryParam.setViewId(blockId);
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_QUERY, queryParam);
    }

    /**
     * 查询不分页的模块数据
     *
     * @param blockId
     * @param queryParams
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> findBlockDataNoPage(Long blockId, JQParameter queryParams) throws Exception {
        QueryParam queryParam = queryParams.getQueryParam();
        queryParam.setViewId(blockId);
        HandleResult handleResult = handlerFactory.handleRequest(Constants.HandleType.TYPE_QUERY, queryParam);
        return (List<Map<String, Object>>) handleResult.getData();
    }


    /**
     * 保存从表行,这样前端可以不传入删除的行,通过关联关系删除相应的行
     *
     * @param rows
     * @param dsId
     * @param masterDsId
     * @param masterKey
     * @return
     */
    public HandleResult saveSlaveRows(List<Map<String, Object>> rows, Long dsId, Long masterDsId, Long masterKey) {
        if (masterKey == null) {
            return HandleResult.failure("没有指定主表主键");
        }
        String version = SessionUtils.getLoginVersion();
        TableColumnRelation tableRelation = findTableRelation(dsId, masterDsId, version);
        if (tableRelation == null) {
            throw new NotExistException("表关系不存在:" + dsId + "&" + masterDsId);
        }
        String outKeyFieldName = findSlaveTableField(tableRelation, dsId);
        //根据条件查询当前不存在的主健
        TableInfo table = SchemaHolder.getTable(dsId, version);
        List<Long> existIds = findRowIds(rows, dsId, table.getKeyField(), version);

        // 查询从表中需要删除的主键
        DeleteParam param = new DeleteParam();
        param.setTable(table);
        Criteria criteria = param.getCriteria().andEqualTo(null, outKeyFieldName, masterKey);
        if (existIds != null && !existIds.isEmpty()) {
            criteria.andNotIn(null, table.getKeyField(), existIds);
        }
        handlerFactory.handleDelete(param);
        //设置外主健字段值
        updateOutKeyField(rows, outKeyFieldName, masterKey);
        //执行其它保存
        return saveRows(rows, dsId);
    }

    private void updateOutKeyField(List<Map<String, Object>> rows, String outKeyField, Long outKeyValue) {
        if (rows == null || rows.isEmpty()) {
            return;
        }
        for (Map<String, Object> row : rows) {
            row.put(outKeyField, outKeyValue);
        }
    }

    private String findSlaveTableField(TableColumnRelation tableRelation, Long slaveDsId) {
        if (tableRelation.getTableFrom().getTableDto().getTableId().equals(slaveDsId)) {
            return tableRelation.getTableFrom().findColumn(tableRelation.getDto().getFieldFrom()).getColumnDto().getFieldName();
        } else {
            return tableRelation.getTableTo().findColumn(tableRelation.getDto().getFieldTo()).getColumnDto().getFieldName();
        }
    }

    /**
     * 取得已存在的ID值
     *
     * @param rows
     * @param dsId
     * @param fieldName
     * @param version
     * @return
     */
    private List<Long> findRowIds(List<Map<String, Object>> rows, Long dsId, String fieldName, String version) {
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        //查找主键字段
        TableInfo table = SchemaHolder.getTable(dsId, version);
        if (table == null) {
            throw new NotExistException("表不存在:" + dsId);
        }
        List<Long> existIds = new ArrayList<>();
        rows.forEach(map -> {
            Long id = CommonUtils.getLongField(map, fieldName);
            if (id == null || id < 0) {
                return;
            }
            existIds.add(id);
        });
        return existIds;

    }

    private TableColumnRelation findTableRelation(Long dsId1, Long dsId2, String version) {
        Long schemaId = getSchemaByDs(dsId1, version);
        if (schemaId == null) {
            throw new NotExistException("数据源不存在:" + dsId1);
        }
        return SchemaHolder.getInstance().getSchema(schemaId, version).findTableRelation(dsId1, dsId2);
    }

    private Long getSchemaByDs(Long dsId, String version) {
        TableInfo table = SchemaHolder.getTable(dsId, version);
        if (table == null) {
            return null;
        }
        return table.getTableDto().getSchemaId();
    }

    /**
     * 保存增加的数据和修改的数据,根据是否有主键来判断检查
     * 如果只指定了主键值,则为删除,目前为单主键配置
     *
     * @param rows
     * @param dsId
     * @return
     */
    @Transactional(readOnly = false)
    public HandleResult saveRows(List<Map<String, Object>> rows, Long dsId) {
        if (rows == null || rows.isEmpty()) {
            return HandleResult.success(0);
        }
        TableInfo table = SchemaHolder.getTable(dsId, SessionUtils.getLoginVersion());
        List<Column> keyColumn = table.getKeyColumn();

        //这里只处理一个主键的情况,
        if (keyColumn == null || keyColumn.size() > 1 || keyColumn.isEmpty()) {
            throw new InvalidConfigException("一张表只可以有一个主键:表[" + dsId + "]");
        }
        String keyField = keyColumn.get(0).getColumnDto().getFieldName();
        List<Map<String, Object>> lstAdd = new ArrayList<>();
        List<Map<String, Object>> lstEdit = new ArrayList<>();
        List<Object> lstDelele = new ArrayList<>();
        long key;
        Object keyValue;

        for (Map<String, Object> row : rows) {
            row.put(Constants.FixColumnName.VERSION_CODE, SessionUtils.getLoginVersion());
            keyValue = row.get(keyField);
            if (keyValue == null || !CommonUtils.isNumber(keyValue)) {
                throw new InvalidParamException("主键值不正确,目前只支持整型类型:" + keyValue);
            }
            key = Long.parseLong(keyValue.toString());
            //约定,如果主键小于0表示增加,否则表示修改
            if (row.size() == 2 && key > -1) {
                lstDelele.add(key);
            } else if (key < 0) {
                lstAdd.add(row);
            } else {
                lstEdit.add(row);
            }
        }
        HandleResult resultAdd = HandleResult.success(0);
        HandleResult resultEdit = HandleResult.success(0);
        HandleResult resultDelete = HandleResult.success(0);
        if (!lstAdd.isEmpty()) {
            resultAdd = saveRowByAdd(lstAdd, dsId);
            if (!resultAdd.isSuccess()) {
                throw new InvalidException(resultAdd.getErr());
            }
        }
        if (!lstEdit.isEmpty()) {
            resultEdit = saveRowByEdit(lstEdit, dsId);
            if (!resultEdit.isSuccess()) {
                throw new InvalidException(resultEdit.getErr());
            }
        }
        if (!lstDelele.isEmpty()) {
            resultDelete = deleteRowByIds(lstDelele, dsId);
            if (!resultDelete.isSuccess()) {
                throw new InvalidException(resultDelete.getErr());
            }
        }
        //如果二个都成功;
        resultAdd.setChangeNum(resultAdd.getChangeNum() + resultDelete.getChangeNum() + resultEdit.getChangeNum());
        return resultAdd;
    }

    /**
     * 保存增加的数据
     * 检查
     *
     * @param rows
     * @param dsId
     * @return
     */
    public HandleResult saveRowByAdd(List<Map<String, Object>> rows, Long dsId) {
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        TableInfo table = SchemaHolder.getTable(dsId, SessionUtils.getLoginVersion());
        if (table == null) {
            throw new NotExistException("表:" + dsId + " 不存在");
        }
        InsertParam insertParam = new InsertParam();
        insertParam.setTable(table);
        insertParam.setLstRows(rows);
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_INSERT, insertParam);
    }


    /**
     * 保存修改的数据
     *
     * @param rows
     * @param dsId
     * @return
     */
    private HandleResult saveRowByEdit(List<Map<String, Object>> rows, Long dsId) {
        HandleResult result = new HandleResult();
        if (rows == null || rows.isEmpty()) {
            result.setErr("没有提供保存的数据");
            return result;
        }
        TableInfo table = SchemaHolder.getTable(dsId, SessionUtils.getLoginVersion());
        if (table == null) {
            throw new NotExistException("表:" + dsId + " 不存在");
        }
        UpdateParam updateParam = new UpdateParam();
        updateParam.setTable(table);
        updateParam.setLstRows(rows);
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_UPDATE, updateParam);
    }


    @Transactional(readOnly = false)
    public HandleResult deleteRowByIds(List<Object> ids, Long dsId) {
        if (ids == null || ids.isEmpty()) {
            throw new InvalidParamException("没有指定要删除的主键");
        }
        TableInfo table = SchemaHolder.getTable(dsId, SessionUtils.getLoginVersion());
        if (table == null) {
            throw new NotExistException("表:" + dsId + " 不存在");
        }
        DeleteParam param = new DeleteParam();
        param.setIds(ids);
        param.setTable(table);
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_DELETE, param);
    }

    /**
     * 删除表数据,根据给定的字段信息删除,filter的格式可以使用表格提交时的条件格式
     * 当前只支持单行的删除,需要包含ID
     *
     * @param filter
     * @param dsId
     * @return
     */

    public HandleResult deleteRow(Map<String, Object> filter, Long dsId) {
        if (dsId == null) {
            throw new InvalidParamException("没有指定要删除的表");
        }
        TableInfo table = SchemaHolder.getTable(dsId, SessionUtils.getLoginVersion());
        if (table == null) {
            throw new NotExistException("表:" + dsId + " 不存在");
        }
        if (filter == null || filter.isEmpty()) {
            return null;
        }
        DeleteParam param = new DeleteParam();
        JQParameter.genFilter(param.getCriteria(), false, filter);
        param.setTable(table);
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_DELETE, param);
    }

    /**
     * 更新层次编码
     *
     * @param mapIdToCode
     */
    @Transactional(readOnly = false)
    public HandleResult updateLevel(Map<Long, String> mapIdToCode, long viewId) {
        BlockViewer viewerInfo = SchemaHolder.getViewerInfo(viewId, SessionUtils.getLoginVersion());
        TableInfo[] viewTables = viewerInfo.getViewTables();
        if (viewTables == null) {
            throw new IllegalOperatorException("指定视图不存在[" + viewId + "]");
        }
        if (viewTables.length != 1) {
            throw new IllegalOperatorException("指定视图存在多个数据源,不可以更新[" + viewId + "]");
        }
        HandleResult result = new HandleResult();
        if (mapIdToCode == null || mapIdToCode.isEmpty()) {
            result.setErr("没有指定更新级次的数据");
            return result;
        }

        UpdateParam param = new UpdateParam();
        param.setTable(viewTables[0]);
        param.setSelective(true);
        param.setLstRows(genRowDataForUpdate(mapIdToCode, viewerInfo, SessionUtils.getLoginVersion()));
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_UPDATE, param);
    }

    /**
     * 根据id和编码,生成更新行数据
     *
     * @param mapIdToCode
     * @param viewerInfo
     * @param versionCode
     * @return
     */
    private List<Map<String, Object>> genRowDataForUpdate(Map<Long, String> mapIdToCode, BlockViewer viewerInfo, String versionCode) {


        TreeInfo treeInfo = viewerInfo.getTreeInfo();
        if (!treeInfo.isValid()) {
            throw new InvalidParamException("指定的视图没有完整树结构信息的配置:["
                    + viewerInfo.getBlockViewDto().getBlockViewId() + "]");
        }
        Iterator<Map.Entry<Long, String>> iterator =
                mapIdToCode.entrySet().iterator();
        List<Map<String, Object>> lstData = new ArrayList<>(mapIdToCode.size());
        Map<String, Object> row = new HashMap<>();
        while (iterator.hasNext()) {
            row = new HashMap<>();
            Map.Entry<Long, String> idAndCode = iterator.next();
            row.put(treeInfo.getIdField().getColumnDto().getFieldName(), idAndCode.getKey());
            //这里暂时不考虑用parent的方式生成TODO_2,
            row.put(treeInfo.getCodeField().getColumnDto().getFieldName(), idAndCode.getValue());
            row.put(Constants.FixColumnName.VERSION_CODE, versionCode);
            lstData.add(row);
        }
        return lstData;
    }

    /**
     * 查询表的单行
     *
     * @param dsId
     * @param id
     * @param versionCode
     * @return
     */
    public HandleResult findTableRow(Long dsId, Long id, String versionCode) {
        TableInfo table = SchemaHolder.getTable(dsId, versionCode);
        if (table == null) {
            throw new NotExistException("表信息没定义:" + dsId);
        }
        if (id == null) {
            return null;
        }
        String keyField = table.getKeyField();
        QueryParam param = new QueryParam();
        param.setTable(table);
        param.appendCriteria()
                .andEqualTo(null, keyField, id);
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_QUERY, param);
    }

    /**
     * 查询表的多行
     *
     * @param dsId
     * @return
     */
    public HandleResult findTableRows(Long dsId, JQParameter queryParams) {
        String versionCode = SessionUtils.getLoginVersion();
        TableInfo table = SchemaHolder.getTable(dsId, versionCode);
        if (table == null) {
            throw new NotExistException("表信息没定义:" + dsId);
        }
        QueryParam param = queryParams.getDsQueryParam(dsId, versionCode);
        return handlerFactory.handleRequest(Constants.HandleType.TYPE_QUERY, param);
    }

    public HandleResult findTableFieldRows(Long tableId, Long fieldId, Map<String, Object> filter, String versionCode) {
        TableInfo table = SchemaHolder.getTable(tableId, versionCode);
        if (table == null) {
            return HandleResult.failure("没有查询到表信息");
        }
        if (filter == null || filter.isEmpty()) {
            return HandleResult.failure("没有提供查询条件");
        }
        QueryParam param = new QueryParam();
        param.setTable(table);
        if (fieldId != null && fieldId > 0) {
            Column column = table.findColumn(fieldId);
            Field field = new Field();
            field.setFieldName(column.getColumnDto().getFieldName());
            field.setTableName(table.getTableDto().getTableName());
            param.setFields(Arrays.asList(field));
        }
        param.addMapEqualsFilter(table.getTableDto().getTableName(), filter, true);
        return handlerFactory.handleQuery(param);
    }

}
