package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;

import java.util.*;

/**
 * @author xxl
 * 更新和插入的参数载体
 * @version V0.0.1
 * @date 2020/8/6 14:29
 **/
public class UpdateParam extends OperParam<UpdateParam> {


    /**
     * 需要设置的字段信息,如果启用此功能,则需要配合条件设置,如果没有指定条件,则不处理
     */
    private Map<String, Object> mapSetValues;
    /**
     * 是否只更新有值的列
     */
    private boolean isSelective = false;

    /**
     * 更新的列信息
     */
    private List<Map<String, Object>> lstRows;

    public List<Map<String, Object>> getLstRows() {
        //这里做一下处理,不在表字段中的数据去除
        return lstRows;
    }

    public Map<String, Object> getMapSetValues() {
        return mapSetValues;
    }

    public void setMapSetValues(Map<String, Object> mapSetValues) {
        this.mapSetValues = mapSetValues;
    }

    public void setLstRows(List<Map<String, Object>> lstRows) {
        this.lstRows = lstRows;
    }

    public boolean isSelective() {
        return isSelective;
    }

    public void setSelective(boolean selective) {
        isSelective = selective;
    }


    /**
     * 检查转换各个字段,如时间,如果不存在于表,则直接删除
     */
    private void validateFields() {
        if (this.getTable() == null || this.lstRows == null || this.lstRows.isEmpty()) {
            return;
        }
        this.lstRows.forEach(row -> {
            if (row == null || row.isEmpty()) {
                return;
            }
            Map<String, Column> mapColumn = this.getTable().getMapColumn();
            if (mapColumn == null) {
                return;
            }
            for (Iterator it = row.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> next = (Map.Entry<String, Object>) it.next();
                //检查有没有此列
                Column column = mapColumn.get(next.getKey());
                if (column == null) {
                    it.remove();
                    continue;
                }
                //如果是时间类型,则转换成时间
                if (column.isDateColumn()) {
                    Object obj = next.getValue();
                    if (obj == null || obj instanceof Date) {
                        continue;
                    }
                    //转换
                    try {
                        row.put(next.getKey(), Constants.DATE_FORMAT.parse(obj.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }

    /**
     * 更新指定DTO对象来生成更新语句,此根据ID字段更新
     *
     * @param schemaId
     * @param version
     * @param obj
     * @param isSelective
     * @return
     */
    public static UpdateParam genUpdateByObject(long schemaId, String version, Object obj, boolean isSelective) {
        String tableName = CommonUtils.getTableName(obj.getClass());
        UpdateParam param = new UpdateParam();
        param.setSelective(isSelective);
        if (CommonUtils.isNotEmpty(tableName)) {
            TableInfo tableInfo = SchemaHolder.findTableByTableName(tableName, schemaId, version);
            if (tableInfo != null) {
                param.setTable(tableInfo);
            }
        }
        param.setLstRows(Arrays.asList(CommonUtils.toMap(obj, true)));
        return param;
    }

    /**
     * 更新指定DTO对象来生成更新语句,此根据ID字段更新
     *
     * @param schemaId
     * @param version
     * @param obj
     * @param isSelective
     * @return
     */
    public static UpdateParam genUpdateParamByFilter(long schemaId, String version, Object obj, boolean isSelective) {
        String tableName = CommonUtils.getTableName(obj.getClass());
        UpdateParam param = new UpdateParam();
        param.setSelective(isSelective);
        if (CommonUtils.isNotEmpty(tableName)) {
            TableInfo tableInfo = SchemaHolder.findTableByTableName(tableName, schemaId, version);
            if (tableInfo != null) {
                param.setTable(tableInfo);
            }
        }
        Map<String, Object> mapSetValues = CommonUtils.toMap(obj, true);
        if (isSelective) {
            mapSetValues = CommonUtils.removeEmptyValues(mapSetValues);
        }
        param.setMapSetValues(mapSetValues);
        return param;
    }


    /**
     * 更新指定DTO对象组来生成更新语句,只允许一张表,不可以混合
     *
     * @param schemaId
     * @param version
     * @param lstObj
     * @param isSelective
     * @return
     */
    public static UpdateParam genUpdateByObjects(long schemaId, String version, List<?> lstObj, boolean isSelective) {
        if (lstObj == null || lstObj.isEmpty()) {
            return new UpdateParam();
        }
        String tableName = CommonUtils.getTableName(lstObj.get(0).getClass());
        UpdateParam param = new UpdateParam();
        param.setSelective(isSelective);
        if (CommonUtils.isNotEmpty(tableName)) {
            TableInfo tableInfo = SchemaHolder.findTableByTableName(tableName, schemaId, version);
            if (tableInfo != null) {
                param.setTable(tableInfo);
            }
        }
        param.setLstRows(CommonUtils.toMapAndConvertToUnderLine(lstObj));
        return param;
    }
}
