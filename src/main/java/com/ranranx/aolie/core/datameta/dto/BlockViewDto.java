package com.ranranx.aolie.core.datameta.dto;

import javax.persistence.Table;

/**
 * @author xxl
 * 复合组件信息, 视图信息, 包含面板 ,列表及树.
 * @version V0.0.1
 * @date 2020/8/5 10:16
 **/
@Table(name = "aolie_dm_blockview")
public class BlockViewDto extends SchemaBaseDto {
    private Long blockViewId;

    private String blockViewName;
    /**
     * 默认视图类型 列表,树,面板?
     */
    private Short defaultShowType;
    /**
     * 视图编码,主要是用来在客户端生成界面,便于查找和记忆.可以带上有意义的编码
     */
    private String blockCode;

    private String memo;

    private String lvlCode;
    /**
     * 是否需要把字段自动转换成驼峰型
     */
    private Short fieldToCamel;

    /**
     * 宽度
     */
    private Integer colSpan;
    /**
     * 高度
     */
    private Integer rowSpan;
    private String title;
    private Integer showHead;

    private Integer layoutType;

    private Short appendFixrow;

    public Long getBlockViewId() {
        return blockViewId;
    }

    public void setBlockViewId(Long blockViewId) {
        this.blockViewId = blockViewId;
    }

    public String getBlockViewName() {
        return blockViewName;
    }

    public void setBlockViewName(String blockViewName) {
        this.blockViewName = blockViewName;
    }

    public Short getDefaultShowType() {
        return defaultShowType;
    }

    public void setDefaultShowType(Short defaultShowType) {
        this.defaultShowType = defaultShowType;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public Short getFieldToCamel() {
        return fieldToCamel;
    }

    public void setFieldToCamel(Short fieldToCamel) {
        this.fieldToCamel = fieldToCamel;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getShowHead() {
        return showHead;
    }

    public void setShowHead(Integer showHead) {
        this.showHead = showHead;
    }

    public String getLvlCode() {
        return lvlCode;
    }

    public void setLvlCode(String lvlCode) {
        this.lvlCode = lvlCode;
    }

    public Integer getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(Integer layoutType) {
        this.layoutType = layoutType;
    }

    public Short getAppendFixrow() {
        return appendFixrow;
    }

    public void setAppendFixrow(Short appendFixrow) {
        this.appendFixrow = appendFixrow;
    }
}
