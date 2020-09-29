package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.datameta.dto.*;
import com.ranranx.aolie.interfaces.SchemaMessageReceiveHandler;
import com.ranranx.aolie.interfaces.SchemaMessageSendHandler;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:35
 * @Version V0.0.1
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
     * 外部消息算是器
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
}
