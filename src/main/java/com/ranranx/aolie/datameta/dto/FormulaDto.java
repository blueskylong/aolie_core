package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 18:54
 * @Version V0.0.1
 **/
public class FormulaDto extends BaseDto {
    private Double formulaId;
    /**
     * 对应表列的ID,一个列可以有多个公式
     */
    private Double columnId;
    private String formula;
    private String filter;
    private Integer index;

    public Double getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(Double formulaId) {
        this.formulaId = formulaId;
    }

    public Double getColumnId() {
        return columnId;
    }

    public void setColumnId(Double columnId) {
        this.columnId = columnId;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
