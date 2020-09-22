package com.ranranx.aolie.ds.definition;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.handler.param.condition.Criteria;

import java.util.List;

/**
 * @Author xxl
 * @Description 删除只能是单表删除
 * @Date 2020/8/7 15:20
 * @Version V0.0.1
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

    public List<Object> getIds() {
        return ids;
    }

    public void setIds(List<Object> ids) {
        this.ids = ids;
    }

    public Criteria getCriteria() {
        if (this.criteria == null) {
            this.criteria = new Criteria();
        }
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableDto(Class clazz) {
        tableName = CommonUtils.getTableName(clazz);
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }
}

