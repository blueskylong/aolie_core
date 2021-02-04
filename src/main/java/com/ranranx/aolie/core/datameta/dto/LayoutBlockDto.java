package com.ranranx.aolie.core.datameta.dto;

import javax.persistence.Table;

/**
 * @author xxl
 *
 * @date 2020/8/19 17:33
 * @version V0.0.1
 **/
@Table(name = "aolie_dm_layout_block")
public class LayoutBlockDto extends SchemaBaseDto {

    /**
     * id
     */
    private Long layoutId;

    /**
     * 布局块ID
     */
    private Long layoutBlockId;

    /**
     * 页面ID
     */
    private Long pageId;
    /**
     * 所占列数,如果大于12 ,则为实际像素,为负数,则表示屏宽减去此数的像素.
     */
    private Integer colNum;


    /**
     * 某些控件可以使用的标题
     */
    private String title;
    /**
     * 所占行数,0表示自适应,小于等于0 表不自己适应,如果是负数,则表示最大不超过.
     */
    private Integer rowNum;
    /**
     * 横向对齐方式
     */
    private Integer alignHorizon;
    /**
     * 纵向对齐方式
     */
    private Integer alignVertical;
    /**
     * 使能
     */
    private Integer enabled;
    /**
     * 顺序号
     */
    private Integer orderNum;
    /**
     * 显示方式,默认是DIV，也可能是分隔条，panel等其它类型的控件
     */
    private Integer displayType;
    /**
     * 以下的View数量根据控件的不同,需要不同数量的控件.
     */
    private Long blockId1;
    private Long blockId2;
    private Long blockId3;
    private Long blockId4;

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public Integer getColNum() {
        return colNum;
    }

    public void setColNum(Integer colNum) {
        this.colNum = colNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getAlignHorizon() {
        return alignHorizon;
    }

    public void setAlignHorizon(Integer alignHorizon) {
        this.alignHorizon = alignHorizon;
    }

    public Integer getAlignVertical() {
        return alignVertical;
    }

    public void setAlignVertical(Integer alignVertical) {
        this.alignVertical = alignVertical;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getDisplayType() {
        return displayType;
    }

    public void setDisplayType(Integer displayType) {
        this.displayType = displayType;
    }

    public Long getBlockId1() {
        return blockId1;
    }

    public void setBlockId1(Long blockId1) {
        this.blockId1 = blockId1;
    }

    public Long getBlockId2() {
        return blockId2;
    }

    public void setBlockId2(Long blockId2) {
        this.blockId2 = blockId2;
    }

    public Long getBlockId3() {
        return blockId3;
    }

    public void setBlockId3(Long blockId3) {
        this.blockId3 = blockId3;
    }

    public Long getBlockId4() {
        return blockId4;
    }

    public void setBlockId4(Long blockId4) {
        this.blockId4 = blockId4;
    }

    public Long getLayoutBlockId() {
        return layoutBlockId;
    }

    public void setLayoutBlockId(Long layoutBlockId) {
        this.layoutBlockId = layoutBlockId;
    }
}
