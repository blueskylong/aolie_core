package com.ranranx.aolie.core.datameta.dto;

import com.ranranx.aolie.core.common.BaseDto;

/**
 * @Author xxl
 * @Description
 * @Date 2020/9/18 16:28
 * @Version V0.0.1
 **/
public class SchemaBaseDto extends BaseDto {
    private Long schemaId;

    public Long getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Long schemaId) {
        this.schemaId = schemaId;
    }
}
