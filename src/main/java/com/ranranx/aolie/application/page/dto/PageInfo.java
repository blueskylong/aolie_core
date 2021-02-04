package com.ranranx.aolie.application.page.dto;

import java.util.List;

/**
 * @author xxl
 *
 * @date 2020/11/4 9:29
 * @version V0.0.1
 **/
public class PageInfo {
    /**
     * 主信息
     */
    private PageInfoDto pageInfoDto;
    /**
     * 明细信息
     */
    private List<PageDetailDto> lstPageDetail;

    public PageInfoDto getPageInfoDto() {
        return pageInfoDto;
    }

    public void setPageInfoDto(PageInfoDto pageInfoDto) {
        this.pageInfoDto = pageInfoDto;
    }

    public List<PageDetailDto> getLstPageDetail() {
        return lstPageDetail;
    }

    public void setLstPageDetail(List<PageDetailDto> lstPageDetail) {
        this.lstPageDetail = lstPageDetail;
    }
}
