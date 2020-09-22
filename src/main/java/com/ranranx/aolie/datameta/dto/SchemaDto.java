package com.ranranx.aolie.datameta.dto;

import javax.persistence.Table;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 9:39
 * @Version V0.0.1
 **/
@Table(name = "aolie_dm_schema")
public class SchemaDto extends SchemaBaseDto {
    private String schemaName;
    private String memo;
    private Integer enabled;
    private Integer width;
    private Integer height;


    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
