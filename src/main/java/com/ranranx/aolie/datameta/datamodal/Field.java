package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.datameta.dto.FieldDto;

/**
 * @Author xxl
 * @Description 字段显示信息
 * @Date 2020/8/5 17:34
 * @Version V0.0.1
 **/
public class Field {
    private FieldDto fieldDto;
    /**
     * 对应的列设置
     */
    private Column column;

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public FieldDto getFieldDto() {
        return fieldDto;
    }

    public void setFieldDto(FieldDto fieldDto) {
        this.fieldDto = fieldDto;
    }
}
