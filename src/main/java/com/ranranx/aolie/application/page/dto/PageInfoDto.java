package com.ranranx.aolie.application.page.dto;

import com.ranranx.aolie.core.datameta.dto.SchemaBaseDto;

import javax.persistence.Table;

@Table(name = "aolie_s_page")
public class PageInfoDto extends SchemaBaseDto {
    private Long pageId;
    private String pageName;
    private String lvlCode;
    private Integer width;
    private Integer height;
    private Integer layoutType;
    private Short canDrag;

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getLvlCode() {
        return lvlCode;
    }

    public void setLvlCode(String lvlCode) {
        this.lvlCode = lvlCode;
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

    public Integer getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(Integer layoutType) {
        this.layoutType = layoutType;
    }

    public Short getCanDrag() {
        return canDrag;
    }

    public void setCanDrag(Short canDrag) {
        this.canDrag = canDrag;
    }
}
