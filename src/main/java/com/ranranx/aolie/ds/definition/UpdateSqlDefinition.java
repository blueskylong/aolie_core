package com.ranranx.aolie.ds.definition;

import com.ranranx.aolie.datameta.datamodal.Table;
import com.ranranx.aolie.engine.param.Criteria;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 如果使用复杂条件更新, 则lstRows里放置的就是需要更新的列及值, 否则, 只会根据行数据的ID更新相应的行.
 * @Date 2020/8/7 15:53
 * @Version V0.0.1
 **/
public class UpdateSqlDefinition {
    /**
     * 表信息
     */
    private Table table;
    /**
     * 行数据信息
     */
    private List<Map<String, Object>> lstRows;
    /**
     * 是否只更新有值的列
     */
    private boolean isSelective;
    /**
     * 复杂条件更新
     */
    private Criteria criteria;

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

    public boolean isSelective() {
        return isSelective;
    }

    public void setSelective(boolean selective) {
        isSelective = selective;
    }
}
