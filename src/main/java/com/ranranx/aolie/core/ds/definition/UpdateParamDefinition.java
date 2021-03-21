package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 如果使用复杂条件更新, 则lstRows里放置的就是需要更新的列及值, 否则, 只会根据行数据的ID更新相应的行.
 * @version V0.0.1
 * @date 2020/8/7 15:53
 **/
public class UpdateParamDefinition {
    /**
     * 表信息
     */
    private String tableName;
    /**
     * 行数据信息
     */
    private List<Map<String, Object>> lstRows;
    /**
     * 是否只更新有值的列
     */
    private boolean isSelective;

    /**
     * 主健字段
     */
    private String idField;
    /**
     * 复杂条件更新
     */
    private Criteria criteria;

    public String getTableName() {
        return tableName;
    }

    public UpdateParamDefinition setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public UpdateParamDefinition setCriteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public List<Map<String, Object>> getLstRows() {
        return lstRows;
    }

    /**
     * 直接语句.暂时提供在复杂语句下使用.
     */
    private SqlExp sqlExp;

    public SqlExp getSqlExp() {
        return sqlExp;
    }

    public UpdateParamDefinition setSqlExp(SqlExp sqlExp) {
        this.sqlExp = sqlExp;
        return this;
    }

    public UpdateParamDefinition setObjects(List<?> lstObj, boolean needConvert) {
        if (lstObj == null || lstObj.isEmpty()) {
            this.lstRows = null;
            return this;
        }
        if (CommonUtils.isEmpty(this.tableName)) {
            this.tableName = CommonUtils.getTableName(lstObj.get(0).getClass());
        }
        this.lstRows = new ArrayList<>();
        for (Object obj : lstObj) {
            CommonUtils.toMap(obj, needConvert);
        }
        return this;
    }

    public UpdateParamDefinition setLstRows(List<Map<String, Object>> lstRows) {
        this.lstRows = lstRows;
        return this;
    }

    public boolean isSelective() {
        return isSelective;
    }

    public UpdateParamDefinition setTableNameByDto(Class clazz) {
        this.tableName = CommonUtils.getTableName(clazz);
        return this;
    }

    public UpdateParamDefinition setSelective(boolean selective) {
        isSelective = selective;
        return this;
    }

    public String getIdField() {
        return idField;
    }

    public UpdateParamDefinition setIdField(String idField) {
        this.idField = idField;
        return this;
    }
}
