package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.datameta.dto.TableColumnRelationDto;
import com.ranranx.aolie.datameta.dto.TableDto;

/**
 * @Author xxl
 * @Description 表关系模型数据
 * @Date 2020/8/8 17:00
 * @Version V0.0.1
 **/
public class TableColumnRelation {
    /**
     * 表关系原始数据
     */
    private TableColumnRelationDto dto;
    /**
     * 起始表信息
     */
    private Table tableFrom;
    /**
     * 终止表信息
     */
    private Table tableTo;

    public TableColumnRelationDto getDto() {
        return dto;
    }

    public void setDto(TableColumnRelationDto dto) {
        this.dto = dto;
    }

    public Table getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(Table tableFrom) {
        this.tableFrom = tableFrom;
    }

    public Table getTableTo() {
        return tableTo;
    }

    public void setTableTo(Table tableTo) {
        this.tableTo = tableTo;
    }
}
