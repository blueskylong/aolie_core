package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

/**
 * @Author xxl
 * @Description 视图列信息
 * @Date 2020/8/4 17:39
 * @Version V0.0.1
 **/
public class FieldDto extends BaseDto {

    private Long fieldId;
    /**
     * 主表ID
     */
    private Long viewerId;
    /**
     * 引用的列ID
     */
    private Long columnId;
    /**
     * 级次编码,本系统都采用的3位一级的编码格式
     */
    private String LvlCode;
    /**
     * 标题
     */
    private String title;
    /**
     * 显示方式,如text,datetime等
     */
    private String dispType;
    /**
     * 横向占比例,类似于bootstrap的12列,占多少列的意思
     */
    private Integer horSpan;
    /**
     * 纵向行数,如textArea这类的,会占用多行.
     */
    private Integer verSpan;
    /**
     * 如果为列表显示,则显示的宽度,0表示列表中不显示
     */
    private Integer width;
    /**
     * 可编辑条件,空表示可编辑
     */
    private String editableFilter;
    /**
     * 可见条件,空表示可见
     */
    private String visibleFilter;
    /**
     * 显示格式,如数字,日期等
     */
    private String format;
    /**
     * 自定义标题的颜色
     */
    private String titleColor;
    /**
     * 说明
     */
    private String memo;
    /**
     * 自定义css样式类
     */
    private String cssClass;
    /**
     * 扩展样式
     */
    private String extStyle;
    /**
     * 分组类型
     */
    private Short groupType;

    /**
     * 默认排序类型
     */
    private Short orderByType;

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getViewerId() {
        return viewerId;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getLvlCode() {
        return LvlCode;
    }

    public void setLvlCode(String lvlCode) {
        LvlCode = lvlCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDispType() {
        return dispType;
    }

    public void setDispType(String dispType) {
        this.dispType = dispType;
    }


    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getEditableFilter() {
        return editableFilter;
    }

    public void setEditableFilter(String editableFilter) {
        this.editableFilter = editableFilter;
    }

    public String getVisibleFilter() {
        return visibleFilter;
    }

    public void setVisibleFilter(String visibleFilter) {
        this.visibleFilter = visibleFilter;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getExtStyle() {
        return extStyle;
    }

    public void setExtStyle(String extStyle) {
        this.extStyle = extStyle;
    }

    public Short getGroupType() {
        return groupType;
    }

    public void setGroupType(Short groupType) {
        this.groupType = groupType;
    }

    public Short getOrderByType() {
        return orderByType;
    }

    public void setOrderByType(Short orderByType) {
        this.orderByType = orderByType;
    }

    public Integer getHorSpan() {
        return horSpan;
    }

    public void setHorSpan(Integer horSpan) {
        this.horSpan = horSpan;
    }

    public Integer getVerSpan() {
        return verSpan;
    }

    public void setVerSpan(Integer verSpan) {
        this.verSpan = verSpan;
    }
}
