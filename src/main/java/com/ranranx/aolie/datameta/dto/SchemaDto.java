package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

import javax.persistence.Table;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 9:39
 * @Version V0.0.1
 **/
@Table(name = "aolie_dm_schema")
public class SchemaDto extends BaseDto {
    private Long schemaId;
    private String schemaName;
    private String memo;
    private Integer enabled;

    public Long getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Long schemaId) {
        this.schemaId = schemaId;
    }

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
}
