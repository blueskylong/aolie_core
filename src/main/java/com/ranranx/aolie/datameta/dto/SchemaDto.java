package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 9:39
 * @Version V0.0.1
 **/
public class SchemaDto extends BaseDto {
    private Double schemaId;
    private Double schemaName;
    private Double memo;

    public Double getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Double schemaId) {
        this.schemaId = schemaId;
    }

    public Double getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(Double schemaName) {
        this.schemaName = schemaName;
    }

    public Double getMemo() {
        return memo;
    }

    public void setMemo(Double memo) {
        this.memo = memo;
    }
}
