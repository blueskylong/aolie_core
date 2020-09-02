package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

import javax.persistence.Table;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/19 17:19
 * @Version V0.0.1
 **/
@Table(name = "aolie_dm_page")
public class PageViewerDto extends BaseDto {
    /**
     *
     */
    private Long schemaId;
    /**
     * id
     */
    private Long pageId;
    /**
     * 页面类型,对话框,内嵌面板
     */
    private Integer pageDisplayType;
    /**
     * 名称
     */
    private String name;
    /**
     * 对话框宽度
     */
    private Integer width;
    /**
     * 对话框高度
     */
    private Integer height;

    /**
     * 布局ID
     */
    private Long layoutId;

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public Integer getPageDisplayType() {
        return pageDisplayType;
    }

    public void setPageDisplayType(Integer pageDisplayType) {
        this.pageDisplayType = pageDisplayType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Long schemaId) {
        this.schemaId = schemaId;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }
}
