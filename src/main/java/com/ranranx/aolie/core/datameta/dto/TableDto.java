package com.ranranx.aolie.core.datameta.dto;

import org.springframework.core.annotation.Order;

import javax.persistence.Table;

/**
 * @author xxl
 * 表信息
 * @version V0.0.1
 * @date 2020/8/4 16:33
 **/
@Table(name = "AOLIE_DM_TABLE")
public class TableDto extends SchemaBaseDto {

    /**
     * 表ID
     */
    private Long tableId;
    /**
     * 表英文名
     */
    @Order
    private String tableName;
    /**
     * 表中文名
     */
    private String title;

    /**
     * 是否只读
     */
    private Integer readOnly;

    /**
     * 数据源名
     */
    private Long dataOperId;
    /**
     * 设计的界面元素的顶部位置
     */
    private Integer posTop;
    /**
     * 设计的界面元素的左边位置
     */
    private Integer posLeft;
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 高度
     */
    private Integer height;

    /**
     * 默认编辑视图ID
     */
    private Long blockViewId;
    /**
     * 固定行设置
     */
    private Short isFixrow;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Integer getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Integer readOnly) {
        this.readOnly = readOnly;
    }

    public Long getDataOperId() {
        return dataOperId;
    }

    public void setDataOperId(Long dataOperId) {
        this.dataOperId = dataOperId;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getBlockViewId() {
        return blockViewId;
    }

    public void setBlockViewId(Long blockViewId) {
        this.blockViewId = blockViewId;
    }

    public Short getIsFixrow() {
        return isFixrow;
    }

    public void setIsFixrow(Short isFixrow) {
        this.isFixrow = isFixrow;
    }
}

