package com.ranranx.aolie.core.datameta.dto;

import javax.persistence.Table;

/**
 * @author xxl
 * 视图列信息
 * @version V0.0.1
 * @date 2020/8/4 17:39
 **/
@Table(name = "aolie_dm_component")
public class ComponentDto extends SchemaBaseDto {

    private Long componentId;
    /**
     * 主表ID
     */
    private Long blockViewId;
    /**
     * 引用的列ID
     */
    private Long columnId;
    /**
     * 级次编码,本系统都采用的3位一级的编码格式
     */
    private String lvlCode;
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
     * 纵向行数,如textArea这类的,会占用多行...
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
     * 背景色
     */
    private String backgroundColor;
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
     * 其它个性的属性扩展,使用JSON的格式,在生成时会合并到创建属性中
     */
    private String extAttr;

    /**
     * 默认排序类型
     */
    private Short orderType;

    private String titlePosition;
    private Integer titleSpan;

    private Integer posTop;
    private Integer posLeft;
    /**
     * 如果树状显示,在树上的角色
     */
    private Integer treeRole;
    /**
     * 在列表中隐藏
     */
    private Integer hideInTable;

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public Long getBlockViewId() {
        return blockViewId;
    }

    public void setBlockViewId(Long blockViewId) {
        this.blockViewId = blockViewId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getLvlCode() {
        return lvlCode;
    }

    public void setLvlCode(String lvlCode) {
        this.lvlCode = lvlCode;
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

    public Short getOrderType() {
        return orderType;
    }

    public void setOrderType(Short orderType) {
        this.orderType = orderType;
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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTitlePosition() {
        return titlePosition;
    }

    public void setTitlePosition(String titlePosition) {
        this.titlePosition = titlePosition;
    }

    public Integer getTitleSpan() {
        return titleSpan;
    }

    public void setTitleSpan(Integer titleSpan) {
        this.titleSpan = titleSpan;
    }

    public Integer getTreeRole() {
        return treeRole;
    }

    public void setTreeRole(Integer treeRole) {
        this.treeRole = treeRole;
    }

    public Integer getPosTop() {
        return posTop;
    }

    public void setPosTop(Integer posTop) {
        this.posTop = posTop;
    }

    public Integer getPosLeft() {
        return posLeft;
    }

    public void setPosLeft(Integer posLeft) {
        this.posLeft = posLeft;
    }

    public String getExtAttr() {
        return extAttr;
    }

    public void setExtAttr(String extAttr) {
        this.extAttr = extAttr;
    }

    public Integer getHideInTable() {
        return hideInTable;
    }

    public void setHideInTable(Integer hideInTable) {
        this.hideInTable = hideInTable;
    }
}
