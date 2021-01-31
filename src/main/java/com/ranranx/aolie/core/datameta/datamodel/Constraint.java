package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaTools;
import com.ranranx.aolie.core.datameta.dto.ConstraintDto;
import com.ranranx.aolie.core.exceptions.NotExistException;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:36
 * @Version V0.0.1
 **/
public class Constraint {
    private ConstraintDto constraintDto;

    public Constraint() {

    }

    public Constraint(ConstraintDto constraintDto) {
        this.constraintDto = constraintDto;
    }

    /**
     * 引用到的表,方便查询
     */

    private List<Long> lstRefTable;
    /**
     * 引用到的列,方便查询
     */

    private List<Long> lstRefColumn;

    /**
     * 这里主要是为了更新公式
     *
     * @param columnIds
     */
    public void columnIdChanged(Map<Long, Long> columnIds) {
        //TODO update formula
    }

    @Transient
    public List<Long> getLstRefTable() {
        if (this.lstRefTable == null) {
            this.initRef();
        }
        return this.lstRefTable;
    }

    private void initRef() {
        this.lstRefTable = new ArrayList<>();
        this.lstRefColumn = new ArrayList<>();
        List<String> columnParams =
                FormulaTools.getColumnParams(this.constraintDto.getExpression() + this.constraintDto.getFilter());
        if (columnParams == null) {
            return;
        }
        Long tableId;
        for (String colId : columnParams) {
            Column column = SchemaHolder.getColumn(Long.parseLong(colId), this.constraintDto.getVersionCode());
            if (column == null) {
                throw new NotExistException("列不存在[" + colId + "]");
            }
            tableId = column.getColumnDto().getTableId();
            if (this.lstRefTable.indexOf(tableId) == -1) {
                this.lstRefTable.add(tableId);
            }
            if (this.lstRefColumn.indexOf(colId) == -1) {
                this.lstRefColumn.add(Long.parseLong(colId));
            }
        }
    }

    public void setLstRefTable(List<Long> lstRefTable) {
        this.lstRefTable = lstRefTable;
    }

    @Transient
    public List<Long> getLstRefColumn() {
        return lstRefColumn;
    }

    public void setLstRefColumn(List<Long> lstRefColumn) {
        this.lstRefColumn = lstRefColumn;
    }


    public ConstraintDto getConstraintDto() {
        return constraintDto;
    }

    public void setConstraintDto(ConstraintDto constraintDto) {
        this.constraintDto = constraintDto;
    }
}
