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

    public FormulaDto getFormulaDto() {
        return formulaDto;
    }

    public void setFormulaDto(FormulaDto formulaDto) {
        this.formulaDto = formulaDto;
    }

    /**
     * 这里主要是为了更新公式
     *
     * @param columnIds
     */
    public void columnIdChanged(List<Long[]> columnIds) {
        for (int i = 0; i < columnIds.size(); i++) {
            Long[] ids = columnIds.get(i);
            if (ids[0].equals(this.formulaDto.getColumnId())) {
                this.formulaDto.setColumnId(ids[1]);
            }
            //TODO 公式和过滤条件也需要相应的变化
        }
    }
}
