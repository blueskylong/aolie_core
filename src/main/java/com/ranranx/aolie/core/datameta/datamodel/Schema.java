package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.datameta.dto.*;
import com.ranranx.aolie.core.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.interfaces.SchemaMessageReceiveHandler;
import com.ranranx.aolie.core.interfaces.SchemaMessageSendHandler;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *
 * @date 2020/8/5 17:35
 * @version V0.0.1
 **/
public class Schema {
    public Schema() {
    }

    public Schema(SchemaDto schemaDto) {
        this.schemaDto = schemaDto;
    }

    private SchemaDto schemaDto;

    private List<TableColumnRelation> lstRelation;
    /**
     * 下属表
     */
    private List<TableInfo> lstTable;
    /**
     * 表间约束
     */
    private List<Constraint> lstConstraint;
    /**
     * 外部消息处理器
     */
    private List<SchemaMessageReceiveHandler> lstReceiveHandler;
    /**
     * 通知外面消息
     */
    private List<SchemaMessageSendHandler> lstSendHandler;
    /**
     * 所有的引用信息
     */
    private List<Reference> lstReference;

    /**
     * tableId 对约束
     */
    private Map<String, List<Constraint>> mapTableConstraint;
    /**
     * colId 对约束
     */
    private Map<String, List<Constraint>> mapColumnConstraint;
    /**
     * 记录列所影响的公式信息,即包含这个列的所有公式,这个列变化,会影响公式的值.
     */
    private Map<Long, List<Formula>> mapColumnFormula;

    @Transient
    public List<TableDto> getTableDtos() {
        List<TableDto> lstResult = new ArrayList<>();
        if (this.lstTable != null) {
            this.lstTable.forEach((table -> {
                table.getTableDto().setSchemaId(this.getSchemaDto().getSchemaId());
                table.getTableDto().setVersionCode(this.getSchemaDto().getVersionCode());
                lstResult.add(table.getTableDto());
            }));

        }
        return lstResult;
    }

    @Transient
    public List<FormulaDto> getFormulaDtos() {
        List<FormulaDto> lstFormulaDto = new ArrayList<>();
        if (this.lstTable != null) {
            Long schemaId = getSchemaDto().getSchemaId();
            String version = getSchemaDto().getVersionCode();
            lstTable.forEach((table -> {
                List<FormulaDto> formulas = table.getFormulaDtos(schemaId,
                        version);
                if (formulas != null && !formulas.isEmpty()) {
                    lstFormulaDto.addAll(formulas);
                }
            }));
        }
        return lstFormulaDto;
    }

    @Transient
    public List<Formula> getFormulas() {
        List<Formula> lstFormula = new ArrayList<>();
        if (this.lstTable != null) {
            Long schemaId = getSchemaDto().getSchemaId();
            String version = getSchemaDto().getVersionCode();
            lstTable.forEach((table -> {
                List<Formula> formulas = table.getFormulas(schemaId,
                        version);
                if (formulas != null && !formulas.isEmpty()) {
                    lstFormula.addAll(formulas);
                }
            }));
        }
        return lstFormula;
    }

    @Transient
    public List<ColumnDto> getColumnDtos() {
        List<ColumnDto> lstFormulaDto = new ArrayList<>();
        if (this.lstTable != null) {
            Long schemaId = getSchemaDto().getSchemaId();
            String version = getSchemaDto().getVersionCode();
            lstTable.forEach((table -> {
                List<ColumnDto> columnDtos = table.getColumnDtos(schemaId, version);
                if (columnDtos != null && !columnDtos.isEmpty()) {
                    lstFormulaDto.addAll(columnDtos);
                }
            }));
        }
        return lstFormulaDto;
    }

    @Transient
    public List<ConstraintDto> getConstraintDtos() {
        List<ConstraintDto> lstResult = new ArrayList<>();
        if (this.lstConstraint != null && !this.lstConstraint.isEmpty()) {
            Long schemaId = getSchemaDto().getSchemaId();
            String version = getSchemaDto().getVersionCode();
            this.lstConstraint.forEach((constraint -> {
                ConstraintDto dto = constraint.getConstraintDto();
                dto.setSchemaId(schemaId);
                dto.setVersionCode(version);
                lstResult.add(dto);
            }));
        }
        return lstResult;
    }

    public List<Reference> getLstReference() {
        return lstReference;
    }

    public String validate() {
        return null;
    }

    public void setLstReference(List<Reference> lstReference) {
        this.lstReference = lstReference;
    }

    public List<Constraint> getLstConstraint() {
        return lstConstraint;
    }

    public void setLstConstraint(List<Constraint> lstConstraint) {
        this.lstConstraint = lstConstraint;
    }

    public List<TableInfo> getLstTable() {
        return lstTable;
    }

    public void setLstTable(List<TableInfo> lstTable) {
        this.lstTable = lstTable;
    }

    public SchemaDto getSchemaDto() {
        return schemaDto;
    }

    public void setSchemaDto(SchemaDto schemaDto) {
        this.schemaDto = schemaDto;
    }

    public List<SchemaMessageReceiveHandler> getLstReceiveHandler() {
        return lstReceiveHandler;
    }

    public void setLstReceiveHandler(List<SchemaMessageReceiveHandler> lstReceiveHandler) {
        this.lstReceiveHandler = lstReceiveHandler;
    }

    public List<SchemaMessageSendHandler> getLstSendHandler() {
        return lstSendHandler;
    }

    public void setLstSendHandler(List<SchemaMessageSendHandler> lstSendHandler) {
        this.lstSendHandler = lstSendHandler;
    }

    public List<TableColumnRelation> getLstRelation() {
        return lstRelation;
    }

    public void setLstRelation(List<TableColumnRelation> lstRelation) {
        this.lstRelation = lstRelation;
    }

    @Transient
    public List<TableColumnRelationDto> getRelationDto() {
        if (this.lstRelation != null && !this.lstRelation.isEmpty()) {
            List<TableColumnRelationDto> lstDto = new ArrayList<>();
            this.lstRelation.forEach(relation -> {
                lstDto.add(relation.getDto());
            });
            return lstDto;
        }
        return null;
    }

    @Transient
    public List<ReferenceDto> getReferenceDto() {
        if (!SchemaTools.isReferenceSchema(this.getSchemaDto().getSchemaId())) {
            return null;
        }
        List<ReferenceDto> lstDto = new ArrayList<>();
        int index = 1;
        if (this.getLstTable() != null) {
            for (TableInfo tableInfo : this.getLstTable()) {
                List<ReferenceDto> lstReference = tableInfo.getLstReference();
                if (lstReference == null || lstReference.isEmpty()) {
                    continue;
                }
                for (ReferenceDto dto : lstReference) {
                    if (dto.getRefId() < 0) {
                        dto.setRefId(IdGenerator.getNextId(Reference.class.getName()));
                    }
                    dto.setTableName(tableInfo.getTableDto().getTableName());
                    dto.setXh(index++);
                    lstDto.add(dto);
                }
            }
            return lstDto;
        }
        return null;
    }

    @Transient
    public String getDsKey() {
        if (this.getSchemaDto().getDataOperId() == null || this.getSchemaDto().getDataOperId() == 0) {
            return DataSourceUtils.getDefaultDataSourceKey();
        }
        DataOperatorInfo dataOperatorInfo = SchemaHolder.getDataOperatorInfo(schemaDto.getDataOperId(), schemaDto.getVersionCode());
        if (dataOperatorInfo == null) {
            throw new NotExistException("数据库连接:[" + schemaDto.getDataOperId() + "__" + schemaDto.getVersionCode() + "]不存在");
        }
        return dataOperatorInfo.getDsKey();
    }

    /**
     * 查找指定表的关系信息,使用组合去查询
     *
     * @param tableIds
     * @return
     */
    @Transient
    public List<TableColumnRelation> getTablesRelation(Long... tableIds) {
        if (tableIds == null || tableIds.length < 2) {
            return null;
        }
        TableColumnRelation relation;
        List<TableColumnRelation> lstResult = new ArrayList<>();
        for (int i = 0; i < tableIds.length - 1; i++) {
            for (int j = i + 1; j < tableIds.length; j++) {
                relation = findTableRelation(tableIds[i], tableIds[j]);
                if (relation != null) {
                    lstResult.add(relation);
                }
            }
        }
        return lstResult;
    }

    /**
     * 查询二张表的关系
     *
     * @param table1
     * @param table2
     * @return
     */
    public TableColumnRelation findTableRelation(long table1, long table2) {
        List<TableColumnRelation> lstRelation = this.getLstRelation();
        if (lstRelation == null || lstRelation.isEmpty()) {
            return null;
        }
        for (TableColumnRelation tableColumnRelation : lstRelation) {
            if ((tableColumnRelation.getTableFrom().getTableDto().getTableId() == table1 &&
                    tableColumnRelation.getTableTo().getTableDto().getTableId() == table2)) {
                return tableColumnRelation;
            }
            if (tableColumnRelation.getTableFrom().getTableDto().getTableId() == table2 &&
                    tableColumnRelation.getTableTo().getTableDto().getTableId() == table1) {
                return tableColumnRelation;
            }
        }
        return null;
    }

    /**
     * 根据表名取得数据源
     *
     * @param tableName
     * @return
     */
    @Transient
    public TableInfo findTableByName(String tableName) {
        if (CommonUtils.isEmpty(tableName)) {
            return null;
        }
        if (this.lstTable == null || lstTable.isEmpty()) {
            return null;
        }
        tableName = tableName.toLowerCase();
        for (TableInfo info : lstTable) {
            if (tableName.equals(info.getTableDto().getTableName().toLowerCase())) {
                return info;
            }
        }
        return null;
    }

    /**
     * 根据表Id取得表信息
     *
     * @param tableId
     * @return
     */
    @Transient
    public TableInfo findTableById(Long tableId) {
        if (tableId == null) {
            return null;
        }
        if (this.lstTable == null || lstTable.isEmpty()) {
            return null;
        }
        for (TableInfo info : lstTable) {
            if (tableId.equals(info.getTableDto().getTableId())) {
                return info;
            }
        }
        return null;
    }

    /**
     * 查找与一个表相关的所有约束
     *
     * @param tableId
     * @return
     */
    public List<Constraint> findTableRefConstraints(Long tableId) {
        if (this.lstConstraint == null || lstConstraint.isEmpty()) {
            return null;
        }
        List<Constraint> lstResult = new ArrayList<>();
        for (Constraint constraint : lstConstraint) {
            List<Long> lstRefTable = constraint.getLstRefTable();
            if (lstRefTable.indexOf(tableId) != -1) {
                lstResult.add(constraint);
            }
        }
        return lstResult;
    }

    /**
     * 根据表名取得数据源
     *
     * @return
     */
    @Transient
    public TableInfo findTableByTitle(String tableTitle) {
        if (CommonUtils.isEmpty(tableTitle)) {
            return null;
        }
        if (this.lstTable == null || lstTable.isEmpty()) {
            return null;
        }
        for (TableInfo info : lstTable) {
            if (tableTitle.equals(info.getTableDto().getTitle())) {
                return info;
            }
        }
        return null;
    }

    /**
     * 取得指定列的约束
     *
     * @param colId
     */
    @Transient
    public List<Constraint> getColumnConstraints(long colId) {
        if (this.mapColumnConstraint == null) {
            this.initMapConstraints();
        }
        return this.mapColumnConstraint.get(colId + "");
    }

    /**
     * 只初始化表内的约束
     */
    private void initMapConstraints() {
        this.mapTableConstraint = new HashMap<>();
        this.mapColumnConstraint = new HashMap<>();
        if (this.lstConstraint != null) {
            for (Constraint constraint : this.lstConstraint) {
                List<Long> lstRefTable = constraint.getLstRefTable();
                List<Long> lstRefColumn = constraint.getLstRefColumn();
                if (lstRefTable != null && !lstRefTable.isEmpty()) {
                    for (Long tableId : lstRefTable) {
                        CommonUtils.addMapListValue(this.mapTableConstraint, tableId.toString(), constraint);
                    }
                    for (Long columnId : lstRefColumn) {
                        CommonUtils.addMapListValue(this.mapColumnConstraint, columnId.toString(), constraint);
                    }
                }
            }
        }
    }

    /**
     * 取得列和公式关系信息  key:colid,value:相关的公式
     *
     * @return
     */
    public Map<Long, List<Formula>> findColRelationFormula() {
        if (mapColumnFormula == null) {
            this.initMapFormula();
        }
        return this.mapColumnFormula;
    }

    /**
     * 初始化列和公式的关系数据
     */
    private void initMapFormula() {
        this.mapColumnFormula = new HashMap<>();
        if (this.lstTable == null || this.lstTable.isEmpty()) {
            return;
        }
        for (TableInfo tableInfo : this.lstTable) {
            List<Column> lstCol = tableInfo.getLstColumn();
            if (lstCol == null || lstCol.isEmpty()) {
                continue;
            }
            for (Column col : lstCol) {
                List<Formula> lstFormula = col.getLstFormula();
                if (lstFormula == null || lstFormula.isEmpty()) {
                    continue;
                }
                for (Formula formula : lstFormula) {
                    List<Long> allRelationCol = formula.getAllRelationCol();
                    if (allRelationCol == null || allRelationCol.isEmpty()) {
                        continue;
                    }
                    //添加到缓存中
                    allRelationCol.forEach(colId -> {
                        CommonUtils.addMapListValue(mapColumnFormula, colId, formula);
                    });


                }
            }
        }
    }
}
