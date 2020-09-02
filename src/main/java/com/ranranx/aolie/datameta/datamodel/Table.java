package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.datameta.dto.TableDto;
import com.ranranx.aolie.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.exceptions.NotExistException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:34
 * @Version V0.0.1
 **/
public class Table {

    private TableDto tableDto;


    public Table(TableDto tableDto) {
        this.tableDto = tableDto;
    }

    /**
     * 表内列表
     */
    private List<Column> lstColumn = new ArrayList<>();

    public String getDsKey() {
        if (tableDto.getDataOperId() == null) {
            return DataSourceUtils.getDefaultDataSourceKey();
        }
        DataOperatorInfo dataOperatorInfo = SchemaHolder.getDataOperatorInfo(tableDto.getDataOperId(), tableDto.getVersionCode());
        if (dataOperatorInfo == null) {
            throw new NotExistException("数据库连接:[" + tableDto.getDataOperId() + "__" + tableDto.getVersionCode() + "]不存在");
        }
        return dataOperatorInfo.getDsKey();
    }

    /**
     * 表内约束
     */
    private List<Constraint> lstConstrant;

    public List<Column> getLstColumn() {
        return lstColumn;
    }

    public void addColumn(Column column) {
        lstColumn.add(column);
    }

    public void setLstColumn(List<Column> lstColumn) {
        this.lstColumn = lstColumn;
    }

    public TableDto getTableDto() {
        return tableDto;
    }

    public void setTableDto(TableDto tableDto) {
        this.tableDto = tableDto;
    }

    public List<Constraint> getLstConstrant() {
        return lstConstrant;
    }

    public void setLstConstrant(List<Constraint> lstConstrant) {
        this.lstConstrant = lstConstrant;
    }
}
