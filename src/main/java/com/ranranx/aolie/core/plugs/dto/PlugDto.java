package com.ranranx.aolie.core.plugs.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * @author xxl
 * @version 1.0
 * @date 2021-07-06 07:58:52
 */
@Table(name = "aolie_s_plugs")
public class PlugDto extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long plugId;
    private String plugUnitCode;
    private String name;
    private Integer status;
    private java.util.Date installTime;
    private String version;
    private String memo;
    private String newVersion;

    public void setPlugId(Long plugId) {
        this.plugId = plugId;
    }

    public Long getPlugId() {
        return this.plugId;
    }

    public void setPlugUnitCode(String plugUnitCode) {
        this.plugUnitCode = plugUnitCode;
    }

    public String getPlugUnitCode() {
        return this.plugUnitCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setInstallTime(java.util.Date installTime) {
        this.installTime = installTime;
    }

    public java.util.Date getInstallTime() {
        return this.installTime;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
}