package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.common.Constants;
import com.ranranx.aolie.datameta.dto.BlockViewDto;
import com.ranranx.aolie.ds.definition.Field;

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

    public BlockViewer() {
    }

    public BlockViewer(BlockViewDto blockViewDto, List<Component> lstField) {
        this.blockViewDto = blockViewDto;
        this.lstComponent = lstField;
    }

    /**
     * 根据ID找控件
     *
     * @param id
     * @return
     */
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

