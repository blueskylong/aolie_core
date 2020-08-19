package com.ranranx.aolie.ds.definition;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/7 15:30
 * @Version V0.0.1
 **/
public class InsertParamDefinition {

    /**
     * 插入的表名
     */
    private String tableName;

    /**
     * 需要插入的信息
     */
    private List<Map<String, Object>> lstRows;

    public List<Map<String, Object>> getLstRows() {
        return lstRows;
    }

    public void setLstRows(List<Map<String, Object>> lstRows) {
        this.lstRows = lstRows;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}