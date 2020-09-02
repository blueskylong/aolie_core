package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.datameta.dto.FormulaDto;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/30 21:08
 * @Version V0.0.1
 **/
public class Formula {
    private FormulaDto formulaDto;

    public Formula(FormulaDto formulaDto) {
        this.formulaDto = formulaDto;
    }

    public FormulaDto getFormulaDto() {
        return formulaDto;
    }

    public void setFormulaDto(FormulaDto formulaDto) {
        this.formulaDto = formulaDto;
    }
}
