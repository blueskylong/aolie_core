package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

import javax.persistence.Table;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 18:54
 * @Version V0.0.1
 **/
@Table(name = "aolie_dm_formula")
public class FormulaDto extends BaseDto {
    private Long formulaId;
    /**
     * 对应表列的ID,一个列可以有多个公式
     */
    private Long columnId;
    private String formula;
    private String filter;
    private Integer orderNum;
    private Long schemaId;

    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(Long formulaId) {
        this.formulaId = formulaId;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
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

    public Integer getOrderNum() {
        return orderNum;
    }

    public Long getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Long schemaId) {
        this.schemaId = schemaId;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
