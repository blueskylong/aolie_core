package com.ranranx.aolie.handler.param;

import com.ranranx.aolie.datameta.datamodal.Table;
import com.ranranx.aolie.handler.param.condition.Criteria;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 更新和插入的参数载体
 * @Date 2020/8/6 14:29
 * @Version V0.0.1
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
    private Table table;

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

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Map<String, Object>> getLstRows() {
        return lstRows;
    }

    public void setLstRows(List<Map<String, Object>> lstRows) {
        this.lstRows = lstRows;
    }
}
