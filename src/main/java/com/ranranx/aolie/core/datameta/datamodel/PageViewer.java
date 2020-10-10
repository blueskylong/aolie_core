package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.datameta.dto.PageViewerDto;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/9/1 14:00
 * @Version V0.0.1
 **/
public class PageViewer {

    /**
     * DTO
     */
    private PageViewerDto pageViewerDto;
    /**
     * 布局对象
     */
    private Layout layout;
    /**
     * 区块对象
     */
    private List<BlockViewer> lstBlockViewer;


    public PageViewerDto getPageViewerDto() {
        return pageViewerDto;
    }

    public void setPageViewerDto(PageViewerDto pageViewerDto) {
        this.pageViewerDto = pageViewerDto;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public List<BlockViewer> getLstBlockViewer() {
        return lstBlockViewer;
    }

    public void setLstBlockViewer(List<BlockViewer> lstBlockViewer) {
        this.lstBlockViewer = lstBlockViewer;
    }
}
