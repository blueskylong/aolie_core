package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.datameta.dto.TableDto;
import com.ranranx.aolie.ds.dataoperator.DataSourceUtils;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:34
 * @Version V0.0.1
 **/
public class Table {

    private TableDto tableDto;
    /**
     * 表内列表
     */
    private List<Column> lstColumn;

    public String getDsKey() {
        DataOperatorInfo dataOperatorInfo = SchemaHolder.getDataOperatorInfo(tableDto.getDataOperId(), tableDto.getVersionCode());
        return dataOperatorInfo.getDsKey();
    }

    /**
     * 表内约束
     */
    private List<Constraint> lstConstrant;

    public List<Column> getLstColumn() {
        return lstColumn;
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
