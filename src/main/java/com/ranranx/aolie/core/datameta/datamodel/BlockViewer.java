package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.dto.BlockViewDto;
import com.ranranx.aolie.core.datameta.dto.ColumnDto;
import com.ranranx.aolie.core.datameta.dto.ComponentDto;
import com.ranranx.aolie.core.ds.definition.Field;
import com.ranranx.aolie.core.ds.definition.FieldOrder;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/8/5 17:35
 **/
public class BlockViewer {
    private BlockViewDto blockViewDto;
    /**
     * 字段显示信息列表
     */
    private List<Component> lstComponent;
    /**
     * 树结构信息
     */
    private TreeInfo treeInfo = null;

    /**
     * 缓存查询字段,会在设计字段的基础上,增加ID字段,如果原来不存在的话
     */
    private List<Field> lstSelectFields = null;


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

    @Transient
    public TableInfo[] getViewTables() {
        if (lstComponent == null) {
            return null;
        }
        List<Long> tableIds = new ArrayList<>();
        Long tableId;
        for (Component com : lstComponent) {
            tableId = com.getColumn().getColumnDto().getTableId();
            if (tableIds.indexOf(tableId) != -1) {
                continue;
            }
            tableIds.add(tableId);
        }
        TableInfo[] tableInfos = new TableInfo[tableIds.size()];
        String versionCode = this.blockViewDto.getVersionCode();
        for (int i = 0; i < tableIds.size(); i++) {
            tableInfos[i] = SchemaHolder.getTable(tableIds.get(i), versionCode);
        }
        return tableInfos;
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
    public List<Field> getSelectFields() {
        if (lstSelectFields != null) {
            return lstSelectFields;
        }
        boolean hasGroup = false;
        List<String> lstFields = new ArrayList<>();
        TableInfo[] lstTable = getViewTables();
        List<Field> lstField = new ArrayList<>(lstComponent.size());
        String version = this.getBlockViewDto().getVersionCode();
        Field field;
        for (Component com : lstComponent) {
            field = toField(com, version, true);
            lstFields.add(field.getFieldName());
            if (field.isGroupType()) {
                hasGroup = true;
            }
            lstField.add(field);

        }
        //检查并默认增加
        if (!hasGroup) {
            for (int i = 0; i < lstTable.length; i++) {
                TableInfo tableInfo = lstTable[i];
                List<Column> keyColumn = tableInfo.getKeyColumn();
                keyColumn.forEach(column -> {
                    if (lstFields.indexOf(column.getColumnDto().getFieldName()) == -1
                            && !Constants.FixColumnName.VERSION_CODE.equals(column.getColumnDto().getFieldName())) {
                        Field field1 = toField(column.getColumnDto(), version, false);
                        lstField.add(field1);
                        lstFields.add(field1.getFieldName());
                    }
                });
            }

        }
        return lstField;
    }

    @Transient
    private Field toField(Component com, String version, boolean isShow) {
        Field field = new Field();
        ColumnDto dto = com.getColumn().getColumnDto();
        TableInfo tableInfo = SchemaHolder.getTable(dto.getTableId(),
                version);

        field.setFieldName(dto.getFieldName());
        field.setTableName(tableInfo.getTableDto().getTableName());
        Short groupType = com.getComponentDto().getGroupType();
        Short orderType = com.getComponentDto().getOrderType();
        if (groupType != null) {
            field.setGroupType(groupType);
        }
        if (orderType != null) {
            field.setOrderType(orderType);
        }
        field.setShow(isShow);
        return field;
    }

    @Transient
    private Field toField(ColumnDto dto, String version, boolean isShow) {
        Field field = new Field();
        TableInfo tableInfo = SchemaHolder.getTable(dto.getTableId(),
                version);
        field.setFieldName(dto.getFieldName());
        field.setTableName(tableInfo.getTableDto().getTableName());
        field.setShow(isShow);
        return field;
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

    @Transient
    public TreeInfo getTreeInfo() {
        if (treeInfo != null) {
            return treeInfo;
        }
        treeInfo = new TreeInfo();
        String version = this.blockViewDto.getVersionCode();
        for (ComponentDto dto : this.getComponentDtos()) {
            Integer treeRole = dto.getTreeRole();
            if (treeRole == null) {
                continue;
            }
            if (treeRole == Constants.TreeRole.idField) {
                treeInfo.setIdField(SchemaHolder.getColumn(dto.getColumnId(), version));
            } else if (treeRole == Constants.TreeRole.codeField) {
                treeInfo.setCodeField(SchemaHolder.getColumn(dto.getColumnId(), version));
            } else if (treeRole == Constants.TreeRole.nameField) {
                treeInfo.setTextField(SchemaHolder.getColumn(dto.getColumnId(), version));
            } else if (treeRole == Constants.TreeRole.parentField) {
                treeInfo.setParentField(SchemaHolder.getColumn(dto.getColumnId(), version));
            }
        }
        return treeInfo;
    }

    @Transient
    public List<FieldOrder> getDefaultOrder() {
        if (lstComponent == null || lstComponent.isEmpty()) {
            return null;
        }
        List<FieldOrder> lstOrder = new ArrayList<>();
        int index = 1;
        for (Component com : lstComponent) {
            if (com.getComponentDto().getOrderType() != null) {
                FieldOrder order = new FieldOrder(SchemaHolder.getTable(com.getColumn().getColumnDto().getTableId(),
                        SessionUtils.getLoginVersion()).getTableDto().getTableName(), com.getColumn().getColumnDto().getFieldName()
                        , com.getComponentDto().getOrderType() == Constants.OrderType.ASC, index++);
                lstOrder.add(order);
            }
        }
        return lstOrder;
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

