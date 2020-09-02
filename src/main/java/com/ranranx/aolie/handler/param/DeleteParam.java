package com.ranranx.aolie.handler.param;

import com.ranranx.aolie.datameta.datamodel.Table;
import com.ranranx.aolie.handler.param.condition.Criteria;

import java.util.List;

/**
 * @Author xxl
 * @Description 删除参数载体
 * @Date 2020/8/6 14:28
 * @Version V0.0.1
 **/
public class DeleteParam {
    /**
     * 复杂条件
     */
    private Criteria criteria;
    /**
     * 表ID
     */
    private long tableId;
    /**
     * 表信息
     */
    private Table table;
    /**
     * 要删除的ID
     */
    private List<Object> ids;

    public List<Object> getIds() {
        return ids;
    }

    public void setIds(List<Object> ids) {
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

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }
}
