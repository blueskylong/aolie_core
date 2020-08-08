package com.ranranx.aolie.ds.definition;

import com.ranranx.aolie.datameta.datamodal.Table;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/7 15:30
 * @Version V0.0.1
 **/
public class InsertSqlDefinition {

    private Table table;
    /**
     * 需要插入的信息
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
