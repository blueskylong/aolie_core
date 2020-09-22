package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.datameta.dto.FormulaDto;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/30 21:08
 * @Version V0.0.1
 **/
public class Formula {
    private FormulaDto formulaDto;

    public Formula() {

    }

    public Formula(FormulaDto formulaDto) {
        this.formulaDto = formulaDto;
    }

    /**
     * 这里主要是为了更新公式
     *
     * @param columnIds
     */
    public void columnIdChanged(List<Long[]> columnIds) {
        //TODO update formula
    }

    public FormulaDto getFormulaDto() {
        return formulaDto;
    }

    public void setFormulaDto(FormulaDto formulaDto) {
        this.formulaDto = formulaDto;
    }
}
