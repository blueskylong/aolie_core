package com.ranranx.aolie.handler.param;

import com.ranranx.aolie.datameta.datamodal.Table;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/8 14:00
 * @Version V0.0.1
 **/
public class InsertParam {
    /**
     * 表信息
     */
    private Table table;

    /**
     * 更新的列信息
     */
    private List<Map<String, Object>> lstRows;

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
