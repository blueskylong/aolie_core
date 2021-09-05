package com.ranranx.aolie.core.datameta.dto;

import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * @author xxl
 * 列信息
 * @version V0.0.1
 * @date 2020/8/4 16:33
 **/
@Table(name = "aolie_dm_column")
public class ColumnDto extends SchemaBaseDto {
    /**
     * 表ID
     */
    private Long tableId;
    private Long columnId;
    private String fieldName;
    private Byte nullable;
    @OrderBy
    private Short fieldIndex;
    private String title;
    private String defaultValue;
    private Long refId;
    private String memo;
    private String fieldType;
    private Integer length;
    private Integer precisionNum;
    private Double maxValue;
    private Double minValue;
    private Short isKey;
    private String refFilter;
    private Short contentFix;
    private Short fixGroup;
    private Short fixRollUp;

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

    public Integer getPrecisionNum() {
        return precisionNum;
    }

    public void setPrecisionNum(Integer precisionNum) {
        this.precisionNum = precisionNum;
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


    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Short getIsKey() {
        return isKey;
    }

    public void setIsKey(Short isKey) {
        this.isKey = isKey;
    }

    public String getRefFilter() {
        return refFilter;
    }

    public void setRefFilter(String refFilter) {
        this.refFilter = refFilter;
    }

    public Short getContentFix() {
        return contentFix;
    }

    public void setContentFix(Short contentFix) {
        this.contentFix = contentFix;
    }

    public Short getFixGroup() {
        return fixGroup;
    }

    public void setFixGroup(Short fixGroup) {
        this.fixGroup = fixGroup;
    }

    public Short getFixRollUp() {
        return fixRollUp;
    }

    public void setFixRollUp(Short fixRollUp) {
        this.fixRollUp = fixRollUp;
    }
}
