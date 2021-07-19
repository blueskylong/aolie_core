package com.ranranx.aolie.application.page.dto;

import com.ranranx.aolie.core.datameta.dto.SchemaBaseDto;

import javax.persistence.Table;

@Table(name = "aolie_s_page_detail")
public class PageDetailDto extends SchemaBaseDto {
    private Long pageId;
    private Long pageDetailId;
    private Long viewId;
    private String pagePosition;
    private Float initWidth;
    private Float initHeight;
    private Integer showType;
    private Integer innerButton;
    private Integer viewType;
    private Integer loadOnshow;
    private Integer initState;
    private String customUi;
    private String customParam;
    private String uiCode;
    private Integer noFullData;
    /**
     * 关联数据源,用于用户自定义界面,用户可以通过动态指定此数据源,来显示按钮,监听数据源的状态等,而
     * 不需要在代码中指定数据源的ID
     */
    private Long relationDs;

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public Long getPageDetailId() {
        return pageDetailId;
    }

    public void setPageDetailId(Long pageDetailId) {
        this.pageDetailId = pageDetailId;
    }


    public String getPagePosition() {
        return pagePosition;
    }

    public void setPagePosition(String pagePosition) {
        this.pagePosition = pagePosition;
    }

    public Float getInitWidth() {
        return initWidth;
    }

    public void setInitWidth(Float initWidth) {
        this.initWidth = initWidth;
    }

    public Float getInitHeight() {
        return initHeight;
    }

    public void setInitHeight(Float initHeight) {
        this.initHeight = initHeight;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public Integer getInnerButton() {
        return innerButton;
    }

    public void setInnerButton(Integer innerButton) {
        this.innerButton = innerButton;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public Integer getLoadOnshow() {
        return loadOnshow;
    }

    public void setLoadOnshow(Integer loadOnshow) {
        this.loadOnshow = loadOnshow;
    }

    public Integer getInitState() {
        return initState;
    }

    public void setInitState(Integer initState) {
        this.initState = initState;
    }

    public Long getViewId() {
        return viewId;
    }

    public void setViewId(Long viewId) {
        this.viewId = viewId;
    }

    public String getCustomUi() {
        return customUi;
    }

    public void setCustomUi(String customUi) {
        this.customUi = customUi;
    }

    public String getCustomParam() {
        return customParam;
    }

    public void setCustomParam(String customParam) {
        this.customParam = customParam;
    }

    public Long getRelationDs() {
        return relationDs;
    }

    public void setRelationDs(Long relationDs) {
        this.relationDs = relationDs;
    }

    public String getUiCode() {
        return uiCode;
    }

    public void setUiCode(String uiCode) {
        this.uiCode = uiCode;
    }

    public Integer getNoFullData() {
        return noFullData;
    }

    public void setNoFullData(Integer noFullData) {
        this.noFullData = noFullData;
    }
}
