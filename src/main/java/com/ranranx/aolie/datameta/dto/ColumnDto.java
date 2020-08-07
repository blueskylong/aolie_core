package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

/**
 * @Author xxl
 * @Description 列信息
 * @Date 2020/8/4 16:33
 * @Version V0.0.1
 **/
public class ColumnDto extends BaseDto {
    /**
     * 表ID
     */
    private Double schemaId;
    private Double tableId;
    private Double columnId;
    private String fieldName;
    private Byte nullable;
    private Short fieldIndex;
    private String title;
    private String defaultValue;
    private Double refTableId;
    private String memo;
    private String fieldType;
    private Integer length;
    private Integer precision;
    private Double maxValue;
    private Double minValue;

    public Double getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Double schemaId) {
        this.schemaId = schemaId;
    }

    public Double getTableId() {
        return tableId;
    }

    public void setTableId(Double tableId) {
        this.tableId = tableId;
    }

    public Double getColumnId() {
        return columnId;
    }

    public void setColumnId(Double columnId) {
        this.columnId = columnId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Byte getNullable() {
        return nullable;
    }

    public void setNullable(Byte nullable) {
        this.nullable = nullable;
    }

    public Short getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(Short fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getRefTableId() {
        return refTableId;
    }

    public void setRefTableId(Double refTableId) {
        this.refTableId = refTableId;
    }
}
