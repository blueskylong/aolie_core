package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.datameta.dto.ColumnDto;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:34
 * @Version V0.0.1
 **/
public class Column {
    private ColumnDto columnDto;
    private Reference reference;

    public ColumnDto getColumnDto() {
        return columnDto;
    }

    public void setColumnDto(ColumnDto columnDto) {
        this.columnDto = columnDto;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }
}
