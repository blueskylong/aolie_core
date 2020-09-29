package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.datameta.dto.TableColumnRelationDto;

import javax.persistence.Transient;
import java.util.List;

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
    @Transient
    private TableInfo tableFrom;
    /**
     * 终止表信息
     */
    @Transient
    private TableInfo tableTo;

    /**
     * 这里主要是为了更新公式
     *
     * @param columnIds
     */
    public void columnIdChanged(List<Long[]> columnIds) {
        for (int i = 0; i < columnIds.size(); i++) {
            Long[] ids = columnIds.get(i);
            if (ids[0].equals(this.dto.getFieldFrom())) {
                this.dto.setFieldFrom(ids[1]);
            } else if (ids[0].equals(this.dto.getFieldTo())) {
                this.dto.setFieldTo(ids[1]);
            }
        }
    }

    public TableColumnRelationDto getDto() {
        return dto;
    }

    public void setDto(TableColumnRelationDto dto) {
        this.dto = dto;
    }

    public TableInfo getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(TableInfo tableFrom) {
        this.tableFrom = tableFrom;
    }

    public TableInfo getTableTo() {
        return tableTo;
    }

    public void setTableTo(TableInfo tableTo) {
        this.tableTo = tableTo;
    }
}
