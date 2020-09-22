package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.datameta.dto.ColumnDto;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:34
 * @Version V0.0.1
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

    public void columnIdChanged(List<Long[]> columnIds) {
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
}
