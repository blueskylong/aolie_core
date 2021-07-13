package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.DbOperInterceptor;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.Ordered;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableColumnRelation;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.DeleteParam;
import com.ranranx.aolie.core.handler.param.OperParam;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.UpdateParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 级联删除的处理
 * 级联删除的条件，1，存在关联， 1.1 如果关联字段可以为空，则清除值，1.2 如果关联字段不可为空，则再次删除
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/12 0012 8:05
 **/
@DbOperInterceptor
public class RelationManageOnDeleteInterceptor implements IOperInterceptor {

    @Autowired
    private HandlerFactory factory;


    @Override
    public HandleResult beforeReturn(OperParam param, String handleType,
                                     Map<String, Object> globalParamData, HandleResult handleResult) {
        DeleteParam deleteParam = (DeleteParam) param;
        //取得 GetRowIdInterceptor拦截的受影响的ID值
        List<Object> lstId = (List<Object>) globalParamData.get(GetRowIdInterceptor.PARAM_IDS);
        if (lstId == null || lstId.isEmpty()) {
            return null;
        }
        handleTableDelete(param.getTable(), lstId);
        return null;
    }

    private void handleTableDelete(TableInfo tableInfo, List<Object> ids) {
        List<TableColumnRelation> lstRelation = findNeedHandleRelations(tableInfo);
        if (lstRelation == null || lstRelation.isEmpty()) {
            return;
        }
        //逐个处理关系
        for (TableColumnRelation relation : lstRelation) {
            handleOneRelation(relation, tableInfo, ids);
        }

    }

    /**
     * 查询指定了字段值列表
     *
     * @param tableInfo
     * @param fieldId
     * @param lstKeyValue
     * @return
     */
    private List<Object> findFieldValues(TableInfo tableInfo, Long fieldId, List<Object> lstKeyValue) {
        QueryParam param = new QueryParam();
        Field field = new Field();
        String fieldName = tableInfo.findColumn(fieldId).getColumnDto().getFieldName();
        field.setFieldName(fieldName);
        field.setTableName(tableInfo.getTableDto().getTableName());
        param.setFields(Arrays.asList(field));
        param.setTable(tableInfo);
        param.getCriteria().andIn(tableInfo.getTableDto().getTableName(), tableInfo.getKeyField(), lstKeyValue);
        HandleResult result = factory.handleQuery(param);
        if (result.isSuccess()) {
            List<Map<String, Object>> lstData = result.getLstData();
            if (lstData == null || lstData.isEmpty()) {
                return null;
            }
            List<Object> lstObj = new ArrayList<>();
            for (Map<String, Object> row : lstData) {
                lstObj.add(row.get(fieldName));
            }
            return lstObj;
        }
        return null;
    }


    /**
     * 处理单个表关系,原则是,如果关系列可为空,则设置为空,如果不可以为空则删除
     *
     * @param relation
     * @param lstIds
     */
    private void handleOneRelation(TableColumnRelation relation, TableInfo thisTable, List<Object> lstIds) {
        Long thisTableId = thisTable.getTableDto().getTableId();
        TableInfo targetTable = findTargetTable(relation, thisTableId);
        Column targetTableColumn = targetTable.findColumn(findTargetFieldId(relation, thisTableId));
        //不可为空,做删除动作
        List<Object> lstTargetId = lstIds;
        if (!thisFieldIsKeyField(relation, thisTableId)) {
            lstTargetId = findFieldValues(thisTable, targetTableColumn.getColumnDto().getColumnId(), lstIds);
        }
        if (targetTableColumn.getColumnDto().getNullable() != null && targetTableColumn.getColumnDto().getNullable().equals(0)) {
            deleteRow(targetTable, targetTableColumn, lstTargetId);
        } else {
            updateCol(targetTable, targetTableColumn, lstTargetId);
        }


    }

    private void deleteRow(TableInfo targetInfo, Column targetColumn, List<Object> lstIds) {
        DeleteParam param = new DeleteParam();
        param.setTable(targetInfo);
        param.getCriteria().andIn(targetInfo.getTableDto().getTableName(), targetColumn.getColumnDto().getFieldName(), lstIds);
        factory.handleDelete(param);
    }

    private void updateCol(TableInfo info, Column colToUpdate, List<Object> lstIds) {
        UpdateParam param = new UpdateParam();
        param.setTable(info);
        param.getCriteria().andIn(info.getTableDto().getTableName(),
                colToUpdate.getColumnDto().getFieldName(), lstIds);
        Map<String, Object> setMap = new HashMap<>();
        setMap.put(colToUpdate.getColumnDto().getFieldName(), null);
        param.setMapSetValues(setMap);
        param.setSelective(false);
        factory.handleUpdate(param);
    }

    /**
     * 查找目标表
     *
     * @param relation
     * @param thisTableId
     * @return
     */
    private TableInfo findTargetTable(TableColumnRelation relation, Long thisTableId) {
        if (relation.getTableTo().getTableDto().getTableId().equals(thisTableId)) {
            return relation.getTableFrom();
        } else {
            return relation.getTableTo();
        }
    }

    /**
     * 查询目标表字段
     *
     * @param relation
     * @param thisTableId
     * @return
     */
    private Long findTargetFieldId(TableColumnRelation relation, Long thisTableId) {
        if (relation.getTableTo().getTableDto().getTableId().equals(thisTableId)) {
            return relation.getDto().getFieldFrom();
        } else {
            return relation.getDto().getFieldTo();
        }
    }

    /**
     * 当前关联的本表字段是不是主键 字段
     *
     * @param relation
     * @param thisTableId
     * @return
     */
    private boolean thisFieldIsKeyField(TableColumnRelation relation, Long thisTableId) {
        if (relation.getTableTo().getTableDto().getTableId().equals(thisTableId)) {
            Short isKey = relation.getTableTo().findColumn(relation.getDto().getFieldTo()).getColumnDto().getIsKey();
            return isKey != null && isKey.equals(1);

        } else {
            Short isKey = relation.getTableFrom().findColumn(relation.getDto().getFieldFrom()).getColumnDto().getIsKey();
            return isKey != null && isKey.equals(1);
        }
    }

    /**
     * 查找本表关联的字段
     *
     * @param relation
     * @param thisTableId
     * @return
     */
    private Long findThisTableField(TableColumnRelation relation, Long thisTableId) {
        if (relation.getTableTo().getTableDto().getTableId().equals(thisTableId)) {
            return relation.getDto().getFieldTo();
        } else {
            return relation.getDto().getFieldFrom();
        }
    }

    /**
     * 查询相关联的关系,并只关注 一对一或一对多(本表一)的关联
     *
     * @param tableInfo
     * @return
     */
    private List<TableColumnRelation> findNeedHandleRelations(TableInfo tableInfo) {
        //找到与此表相关联的其它表,只需要关注 一对一或一对多(本表一)的关联
        List<TableColumnRelation> tableRelations
                = SchemaHolder.getTableRelations(tableInfo.getTableDto().getVersionCode(),
                tableInfo.getTableDto().getTableId());
        //过虑指定类型关系
        if (tableRelations == null || tableRelations.isEmpty()) {
            return null;
        }
        Integer relationType;
        Long thisTableId = tableInfo.getTableDto().getTableId();
        List<TableColumnRelation> lstResult = new ArrayList<>();
        for (TableColumnRelation relation : tableRelations) {
            relationType = relation.getDto().getRelationType();
            if (relationType.equals(Constants.TableRelationType.TYPE_ONE_ONE)) {
                lstResult.add(relation);
            } else if (relationType.equals(Constants.TableRelationType.TYPE_ONE_MULTI)
                    && relation.getTableFrom().getTableDto().getTableId().equals(thisTableId)) {
                lstResult.add(relation);
            } else if (relationType.equals(Constants.TableRelationType.TYPE_MULTI_ONE)
                    && relation.getTableTo().getTableDto().getTableId().equals(thisTableId)) {
                lstResult.add(relation);
            }
        }
        return lstResult;
    }

    /**
     * 是否可以处理
     *
     * @param type
     * @param objExtinfo
     * @return
     */
    @Override
    public boolean isCanHandle(String type, Object objExtinfo) {
        return Constants.HandleType.TYPE_DELETE.equals(type);
    }

    @Override
    public int getOrder() {
        return Ordered.BASE_ORDER + 100;
    }
}
