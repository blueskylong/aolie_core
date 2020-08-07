package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

/**
 * @Author xxl
 * @Description 表信息
 * @Date 2020/8/4 16:33
 * @Version V0.0.1
 **/
public class TableDto extends BaseDto {
    /**
     * 设计ID
     */
    private Double schemaId;
    /**
     * 表ID
     */
    private Double tableId;
    /**
     * 表英文名
     */
    private String tableName;
    /**
     * 表中文名
     */
    private String title;

    /**
     * 是否只读
     */
    private Integer readOnly;



    public Double getTableId() {
        return tableId;
    }

    public void setTableId(Double tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Double schemaId) {
        this.schemaId = schemaId;
    }

    public Integer getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Integer readOnly) {
        this.readOnly = readOnly;
    }
}

