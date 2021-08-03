package com.ranranx.aolie.application.step.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * @author xxl
 * @version 1.0
 * @date 2021-08-03 07:56:06
 */
@Table(name = "aolie_s_step")
public class StepMain extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long stepId;
    private String stepName;
    private String lvlCode;
    private String stepCode;
    private String params;
    private Integer dispType;
    private String extClass;
    private Short hasFullstep;
    private Long schemaId;

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public Long getStepId() {
        return this.stepId;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepName() {
        return this.stepName;
    }

    public void setLvlCode(String lvlCode) {
        this.lvlCode = lvlCode;
    }

    public String getLvlCode() {
        return this.lvlCode;
    }

    public void setStepCode(String stepCode) {
        this.stepCode = stepCode;
    }

    public String getStepCode() {
        return this.stepCode;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return this.params;
    }

    public void setDispType(Integer dispType) {
        this.dispType = dispType;
    }

    public Integer getDispType() {
        return this.dispType;
    }

    public void setExtClass(String extClass) {
        this.extClass = extClass;
    }

    public String getExtClass() {
        return this.extClass;
    }

    public void setHasFullstep(Short hasFullstep) {
        this.hasFullstep = hasFullstep;
    }

    public Short getHasFullstep() {
        return this.hasFullstep;
    }

    public Long getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Long schemaId) {
        this.schemaId = schemaId;
    }
}