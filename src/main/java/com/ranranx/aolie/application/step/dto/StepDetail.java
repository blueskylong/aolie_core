package com.ranranx.aolie.application.step.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * @author xxl
 * @version 1.0
 * @date 2021-08-03 07:56:20
 */
@Table(name = "aolie_s_step_detail")
public class StepDetail extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long stepId;
    private Long detailId;
    private String detailName;
    @OrderBy
    private String lvlCode;
    private String dispFilter;
    private Long pageId;
    private String customStep;

    private String params;
    private Long menuId;

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public Long getStepId() {
        return this.stepId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getDetailId() {
        return this.detailId;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailName() {
        return this.detailName;
    }

    public void setLvlCode(String lvlCode) {
        this.lvlCode = lvlCode;
    }

    public String getLvlCode() {
        return this.lvlCode;
    }

    public void setDispFilter(String dispFilter) {
        this.dispFilter = dispFilter;
    }

    public String getDispFilter() {
        return this.dispFilter;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public Long getPageId() {
        return this.pageId;
    }

    public void setCustomStep(String customStep) {
        this.customStep = customStep;
    }

    public String getCustomStep() {
        return this.customStep;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return this.params;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}