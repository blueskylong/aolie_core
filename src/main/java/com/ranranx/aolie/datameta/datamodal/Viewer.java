package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.datameta.dto.BlockViewDto;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:35
 * @Version V0.0.1
 **/
public class Viewer {

    private BlockViewDto blockViewDto;
    /**
     * 字段显示信息列表
     */
    private List<Field> lstField;

    public BlockViewDto getBlockViewDto() {
        return blockViewDto;
    }

    public void setBlockViewDto(BlockViewDto blockViewDto) {
        this.blockViewDto = blockViewDto;
    }

    public List<Field> getLstField() {
        return lstField;
    }

    public void setLstField(List<Field> lstField) {
        this.lstField = lstField;
    }
}
