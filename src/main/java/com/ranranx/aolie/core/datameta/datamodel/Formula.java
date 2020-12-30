package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.datameta.dto.FormulaDto;

import java.util.Map;

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
    public void columnIdChanged(Map<Long, Long> columnIds) {
        for (int i = 0; i < columnIds.size(); i++) {
            if (columnIds.containsKey(this.formulaDto.getColumnId())) {
                this.formulaDto.setColumnId(columnIds.get(this.formulaDto.getColumnId()));
            }
            //TODO 公式和过滤条件也需要相应的变化
        }
    }
}
