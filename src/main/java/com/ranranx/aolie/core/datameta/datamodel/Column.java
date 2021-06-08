package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.datameta.dto.ColumnDto;

import java.beans.Transient;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/8/5 17:34
 **/
public class Column {
    private ColumnDto columnDto;
    private Reference reference;

    private List<Formula> lstFormula;


    public Column(ColumnDto columnDto, Reference reference) {
        this.columnDto = columnDto;
        this.reference = reference;
    }

    public void updateTableId(long tableId) {
        this.columnDto.setTableId(tableId);
    }

    public void updateColumnId(Long colId) {
        this.columnDto.setColumnId(colId);
        if (this.lstFormula != null) {
            for (Formula formula : this.lstFormula) {
                formula.getFormulaDto().setColumnId(colId);
            }
        }

    }

    public void columnIdChanged(Map<Long, Long> columnIds) {
        if (this.lstFormula != null) {
            for (Formula formula : this.lstFormula) {
                formula.columnIdChanged(columnIds);
            }
        }
    }

    public Column() {
    }

    public ColumnDto getColumnDto() {
        return columnDto;
    }

    public void setColumnDto(ColumnDto columnDto) {
        this.columnDto = columnDto;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public List<Formula> getLstFormula() {
        return lstFormula;
    }

    public void setLstFormula(List<Formula> lstFormula) {
        this.lstFormula = lstFormula;
    }

    @Transient
    public boolean isNumberColumn() {
        String fieldType = this.columnDto.getFieldType();
        return fieldType.equals(DmConstants.FieldType.DECIMAL)
                || fieldType.equals(DmConstants.FieldType.INT);

    }

    @Transient
    public boolean isDateColumn() {
        String fieldType = this.columnDto.getFieldType();
        return fieldType.equals(DmConstants.FieldType.DATETIME);
    }

}
