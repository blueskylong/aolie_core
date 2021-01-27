package com.ranranx.aolie.application.user.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * @author xxl
 * @version 1.0
 * @date 2021-01-12 15:27:57
 */
@Table(name = "aolie_s_right_resource")
public class RightSourceDto extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long rsId;
    private String rsName;
    private Integer resourceType;
    private Long resourceId;
    private Short transmitable;
    private String filterType;
    private String lvlCode;

    public void setRsId(Long rsId) {
        this.rsId = rsId;
    }

    public Long getRsId() {
        return this.rsId;
    }

    public void setRsName(String rsName) {
        this.rsName = rsName;
    }

    public String getRsName() {
        return this.rsName;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getResourceType() {
        return this.resourceType;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getResourceId() {
        return this.resourceId;
    }

    public void setTransmitable(Short transmitable) {
        this.transmitable = transmitable;
    }

    public Short getTransmitable() {
        return this.transmitable;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterType() {
        return this.filterType;
    }

    public void setLvlCode(String lvlCode) {
        this.lvlCode = lvlCode;
    }

    public String getLvlCode() {
        return this.lvlCode;
    }

}