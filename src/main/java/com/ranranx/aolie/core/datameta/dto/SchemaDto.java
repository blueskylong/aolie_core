package com.ranranx.aolie.core.datameta.dto;

import javax.persistence.Table;

/**
 * @author xxl
 *
 * @date 2020/8/5 9:39
 * @version V0.0.1
 **/
@Table(name = "aolie_dm_schema")
public class SchemaDto extends SchemaBaseDto {
    private String schemaName;
    private String memo;
    private Integer enabled;
    private Integer width;
    private Integer height;
    private Long dataOperId;


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

    public Long getDataOperId() {
        return dataOperId;
    }

    public void setDataOperId(Long dataOperId) {
        this.dataOperId = dataOperId;
    }
}
