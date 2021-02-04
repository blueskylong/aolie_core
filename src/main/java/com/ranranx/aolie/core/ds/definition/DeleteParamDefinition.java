package com.ranranx.aolie.core.ds.definition;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.handler.param.condition.Criteria;

import java.util.List;

/**
 * @author xxl
 *  删除只能是单表删除
 * @date 2020/8/7 15:20
 * @version V0.0.1
 **/
public class DeleteParamDefinition {

    /**
     * 要操作的表名
     */
    private String tableName;
    /**
     * 要删除的ID
     */
    private List<Object> ids;

    /**
     * 主健字段
     */
    private String idField;

    /**
     * 复杂过滤条件
     */
    private Criteria criteria;

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

    public List<Object> getIds() {
        return ids;
    }

    public DeleteParamDefinition setIds(List<Object> ids) {
        this.ids = ids;
        return this;
    }

    public Criteria getCriteria() {
        if (this.criteria == null) {
            this.criteria = new Criteria();
        }
        return criteria;
    }

    public DeleteParamDefinition setCriteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public DeleteParamDefinition setTableDto(Class clazz) {
        tableName = CommonUtils.getTableName(clazz);
        return this;
    }

    public String getIdField() {
        return idField;
    }

    public DeleteParamDefinition setIdField(String idField) {
        this.idField = idField;
        return this;
    }
}

