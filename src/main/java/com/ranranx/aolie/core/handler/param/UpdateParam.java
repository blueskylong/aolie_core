package com.ranranx.aolie.core.handler.param;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.datameta.datamodel.TableInfo;
import com.ranranx.aolie.core.ds.definition.SqlExp;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.util.*;

/**
 * @author xxl
 * 更新和插入的参数载体
 * @version V0.0.1
 * @date 2020/8/6 14:29
 **/
public class UpdateParam {
    /**
     * 复杂条件
     */
    private Criteria criteria;
    /**
     * 字段条件
     */
    private Map<String, Object> mapFilter;
    /**
     * 表信息
     */
    private TableInfo table;

    /**
     * 是否只更新有值的列
     */
    private boolean isSelective = false;
    /**
     * 直接语句.暂时提供在复杂语句下使用.
     */
    private SqlExp sqlExp;

    /**
     * 增加控制信息,让拦截器使用
     */
    private Map<String, Object> mapControlParam;

    public SqlExp getSqlExp() {
        return sqlExp;
    }

    public void setSqlExp(SqlExp sqlExp) {
        this.sqlExp = sqlExp;
    }

    /**
     * 更新的列信息
     */
    private List<Map<String, Object>> lstRows;

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public Map<String, Object> getMapFilter() {
        return mapFilter;
    }

    public void setMapFilter(Map<String, Object> mapFilter) {
        this.mapFilter = mapFilter;
    }

    public TableInfo getTable() {
        return table;
    }

    public void setTable(TableInfo table) {
        this.table = table;
    }

    public List<Map<String, Object>> getLstRows() {
        //这里做一下处理,不在表字段中的数据去除
        return lstRows;
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

    public Map<String, Object> getMapControlParam() {
        return mapControlParam;
    }

    public void setMapControlParam(Map<String, Object> mapControlParam) {
        this.mapControlParam = mapControlParam;
    }

    /**
     * 增加一个控制参数
     *
     * @param key
     * @param value
     */
    public void addControlParam(String key, Object value) {
        if (this.mapControlParam == null) {
            this.mapControlParam = new HashMap<>();
        }
        this.mapControlParam.put(key, value);
    }

    /**
     * 取得一个控制参数
     *
     * @param key
     * @return
     */
    public Object getControlParam(String key) {
        if (this.mapControlParam == null) {
            return null;
        }
        return this.mapControlParam.get(key);
    }

    /**
     * 检查转换各个字段,如时间,如果不存在于表,则直接删除
     */
    private void validateFields() {
        if (this.table == null || this.lstRows == null || this.lstRows.isEmpty()) {
            return;
        }
        this.lstRows.forEach(row -> {
            if (row == null || row.isEmpty()) {
                return;
            }
            Map<String, Column> mapColumn = this.table.getMapColumn();
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
     * 更新指定DTO对象来生成更新语句
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
}
