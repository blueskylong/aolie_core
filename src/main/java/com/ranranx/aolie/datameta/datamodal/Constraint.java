package com.ranranx.aolie.datameta.datamodal;

import com.ranranx.aolie.datameta.dto.ConstraintDto;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:36
 * @Version V0.0.1
 **/
public class Constraint {
    private ConstraintDto constraintDto;
    /**
     * 引用到的表,方便查询
     */
    private List<Double> lstRefTable;
    /**
     * 引用到的列,方便查询
     */
    private List<Double> lstRefColumn;

    public List<Double> getLstRefTable() {
        return lstRefTable;
    }

    public void setLstRefTable(List<Double> lstRefTable) {
        this.lstRefTable = lstRefTable;
    }

    public List<Double> getLstRefColumn() {
        return lstRefColumn;
    }

    public void setLstRefColumn(List<Double> lstRefColumn) {
        this.lstRefColumn = lstRefColumn;
    }


    public ConstraintDto getConstraintDto() {
        return constraintDto;
    }

    public void setConstraintDto(ConstraintDto constraintDto) {
        this.constraintDto = constraintDto;
    }
}
