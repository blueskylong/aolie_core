package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.datameta.dto.BlockViewDto;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:35
 * @Version V0.0.1
 **/
public class BlockViewer {
    private BlockViewDto blockViewDto;
    /**
     * 字段显示信息列表
     */
    private List<Component> lstComponent;

    public BlockViewer() {
    }

    public BlockViewer(BlockViewDto blockViewDto, List<Component> lstField) {
        this.blockViewDto = blockViewDto;
        this.lstComponent = lstField;
    }


    public BlockViewDto getBlockViewDto() {
        return blockViewDto;
    }

    public void setBlockViewDto(BlockViewDto blockViewDto) {
        this.blockViewDto = blockViewDto;
    }

    public List<Component> getLstComponent() {
        return lstComponent;
    }

    public void setLstComponent(List<Component> lstComponent) {
        this.lstComponent = lstComponent;
    }
}
