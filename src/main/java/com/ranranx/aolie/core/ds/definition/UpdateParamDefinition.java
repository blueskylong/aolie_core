package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *  如果使用复杂条件更新, 则lstRows里放置的就是需要更新的列及值, 否则, 只会根据行数据的ID更新相应的行.
 * @date 2020/8/7 15:53
 * @version V0.0.1
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

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
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

    public void setSqlExp(SqlExp sqlExp) {
        this.sqlExp = sqlExp;
    }

    public void setObjects(List<?> lstObj, boolean needConvert) {
        if (lstObj == null || lstObj.isEmpty()) {
            this.lstRows = null;
            return;
        }
        if (CommonUtils.isEmpty(this.tableName)) {
            this.tableName = CommonUtils.getTableName(lstObj.get(0).getClass());
        }
        this.lstRows = new ArrayList<>();
        for (Object obj : lstObj) {
            CommonUtils.toMap(obj, needConvert);
        }

    }

    public void setLstRows(List<Map<String, Object>> lstRows) {
        this.lstRows = lstRows;
    }

    public boolean isSelective() {
        return isSelective;
    }

    public void setTableNameByDto(Class clazz) {
        this.tableName = CommonUtils.getTableName(clazz);
    }

    public void setSelective(boolean selective) {
        isSelective = selective;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }
}
