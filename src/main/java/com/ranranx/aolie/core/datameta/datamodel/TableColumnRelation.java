package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.datameta.dto.TableColumnRelationDto;

import java.beans.Transient;
import java.util.Map;

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
    private TableInfo tableFrom;
    /**
     * 终止表信息
     */
    private TableInfo tableTo;

    /**
     * 这里主要是为了更新公式
     *
     * @param columnIds
     */
    public void columnIdChanged(Map<Long, Long> columnIds) {
        if (columnIds.containsKey(dto.getFieldFrom())) {
            dto.setFieldFrom(columnIds.get(dto.getFieldFrom()));
        }
        if (columnIds.containsKey(dto.getFieldTo())) {
            dto.setFieldTo(columnIds.get(dto.getFieldTo()));
        }
    }

    public TableColumnRelationDto getDto() {
        return dto;
    }

    public void setDto(TableColumnRelationDto dto) {
        this.dto = dto;
    }

    @Transient
    public TableInfo getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(TableInfo tableFrom) {
        this.tableFrom = tableFrom;
    }

    @Transient
    public TableInfo getTableTo() {
        return tableTo;
    }

    public void setTableTo(TableInfo tableTo) {
        this.tableTo = tableTo;
    }
}
