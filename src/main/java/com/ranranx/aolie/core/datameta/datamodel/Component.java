package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.datameta.dto.ComponentDto;

import java.io.Serializable;

/**
 * @author xxl
 *  字段显示信息
 * @date 2020/8/5 17:34
 * @version V0.0.1
 **/
public class Component implements Serializable {
    public Component() {
    }

    public Component(ComponentDto componentDto, Column column) {
        this.componentDto = componentDto;
        this.column = column;
    }

    /**
     * 是不是数字列
     */
    public boolean isNumberColumn() {
        return this.column.isNumberColumn();
    }


    private ComponentDto componentDto;
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

    public ComponentDto getComponentDto() {
        return componentDto;
    }

    public void setComponentDto(ComponentDto componentDto) {
        this.componentDto = componentDto;
    }
}
