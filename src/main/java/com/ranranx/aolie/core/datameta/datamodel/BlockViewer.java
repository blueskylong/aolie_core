package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.dto.BlockViewDto;
import com.ranranx.aolie.core.datameta.dto.ComponentDto;
import com.ranranx.aolie.core.ds.definition.Field;

import java.beans.Transient;
import java.util.ArrayList;
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


    public BlockViewer(BlockViewDto blockViewDto, List<Component> lstField) {
        this.blockViewDto = blockViewDto;
        this.lstComponent = lstField;
    }

    public void updateViewKeys() {
        long viewId = this.blockViewDto.getBlockViewId();
        String versionCode = SessionUtils.getLoginVersion();
        if (this.blockViewDto.getBlockViewId() < 0) {
            viewId = IdGenerator.getNextId(BlockViewDto.class.getName());
            this.blockViewDto.setBlockViewId(viewId);
        }
        this.blockViewDto.setVersionCode(versionCode);
        if (this.lstComponent != null && !this.lstComponent.isEmpty()) {
            for (Component com : lstComponent) {
                com.getComponentDto().setBlockViewId(viewId);
                com.getComponentDto().setVersionCode(versionCode);
                if (com.getComponentDto().getComponentId() < 0) {
                    com.getComponentDto().setComponentId(IdGenerator.getNextId(ComponentDto.class.getName()));
                }
            }
        }
    }

    /**
     * 根据ID找控件
     *
     * @param id
     * @return
     */
    @Transient
    public Component getComponentByID(Long id) {
        if (this.lstComponent == null || this.lstComponent.isEmpty()) {
            return null;
        }
        for (Component component : lstComponent) {
            if (component.getComponentDto().getComponentId().equals(id)) {
                return component;
            }
        }
        return null;
    }

    /**
     * 根据字段名找控件
     *
     * @param fieldName
     * @return
     */
    @Transient
    public Component getComponentByFieldName(String fieldName) {
        if (this.lstComponent == null || this.lstComponent.isEmpty()) {
            return null;
        }
        for (Component component : lstComponent) {
            if (component.getColumn().getColumnDto().getFieldName().equals(fieldName)) {
                return component;
            }
        }
        return null;
    }


    public BlockViewDto getBlockViewDto() {
        return blockViewDto;
    }

    /**
     * 转换成查询字段
     *
     * @return
     */
    @Transient
    public List<Field> getFields() {
        List<Field> lstField = new ArrayList<>(lstComponent.size());
        String version = this.getBlockViewDto().getVersionCode();
        for (Component com : lstComponent) {
            Field field = new Field();
            field.setFieldName(com.getColumn().getColumnDto().getFieldName());
            field.setTableName(SchemaHolder.getTable(com.getColumn().getColumnDto().getTableId(),
                    version).getTableDto().getTableName());
            field.setGroupType(com.getComponentDto().getGroupType() != null ? com.getComponentDto().getGroupType() : Constants.GroupType.NONE);
            field.setOrderType(com.getComponentDto().getOrderType() != null ? com.getComponentDto().getOrderType() : Constants.OrderType.NONE);
            field.setShow(true);
            lstField.add(field);
        }
        return lstField;
    }

    @Transient
    public List<ComponentDto> getComponentDtos() {
        if (this.lstComponent == null || this.lstComponent.isEmpty()) {
            return null;
        }
        List<ComponentDto> lstDto = new ArrayList<>();
        for (Component com : this.lstComponent) {
            lstDto.add(com.getComponentDto());
        }
        return lstDto;
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

