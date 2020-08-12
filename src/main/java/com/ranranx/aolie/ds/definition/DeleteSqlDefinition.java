package com.ranranx.aolie.ds.definition;

import com.ranranx.aolie.datameta.datamodal.Table;
import com.ranranx.aolie.handler.param.Criteria;

import java.util.Map;

/**
 * @Author xxl
 * @Description 删除只能是单表删除
 * @Date 2020/8/7 15:20
 * @Version V0.0.1
 **/
public class DeleteSqlDefinition {
    private Table table;

    /**
     * 一般过滤条件
     */
    private Map<String, Object> mapFilter;
    /**
     * 要删除的ID
     */
    private Long[] ids;

    /**
     * 复杂过滤条件
     */
    private Criteria criteria;

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }
    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }


    public Map<String, Object> getMapFilter() {
        return mapFilter;
    }

    public void setMapFilter(Map<String, Object> mapFilter) {
        this.mapFilter = mapFilter;
    }
}

