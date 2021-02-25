package com.ranranx.aolie.application.sysconfig.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * @author xxl
 * @version 1.0
 * @date 2021-02-17 17:50:59
 */
@Table(name = "aolie_s_config")
public class SysConfigDto extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long configId;
    private String name;
    private String memo;
    private String refStr;
    private String configValue;
    private Short isMulti;
    /**
     * 值类型,参考 DmConstants.FieldType
     */
    private Short valueType;

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getConfigId() {
        return this.configId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setRefStr(String refStr) {
        this.refStr = refStr;
    }

    public String getRefStr() {
        return this.refStr;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigValue() {
        return this.configValue;
    }

    public void setIsMulti(Short isMulti) {
        this.isMulti = isMulti;
    }

    public Short getIsMulti() {
        return this.isMulti;
    }

    public Short getValueType() {
        return valueType;
    }

    public void setValueType(Short valueType) {
        this.valueType = valueType;
    }
}