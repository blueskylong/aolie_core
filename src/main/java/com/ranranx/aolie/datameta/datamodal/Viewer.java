package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.datameta.dto.ViewerDto;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:35
 * @Version V0.0.1
 **/
public class Viewer {

    private ViewerDto viewerDto;
    /**
     * 字段显示信息列表
     */
    private List<Field> lstField;

    public ViewerDto getViewerDto() {
        return viewerDto;
    }

    public void setViewerDto(ViewerDto viewerDto) {
        this.viewerDto = viewerDto;
    }

    public List<Field> getLstField() {
        return lstField;
    }

    public void setLstField(List<Field> lstField) {
        this.lstField = lstField;
    }
}
