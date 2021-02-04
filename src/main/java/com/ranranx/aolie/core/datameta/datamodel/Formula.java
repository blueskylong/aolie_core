package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaTools;
import com.ranranx.aolie.core.datameta.dto.FormulaDto;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *
 * @date 2020/8/30 21:08
 * @version V0.0.1
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

    /**
     * 取得包含的所有字段
     *
     * @return
     */
    @Transient
    public List<Long> getAllRelationCol() {
        List<String> lstColIds = FormulaTools.getColumnParams(this.getFormulaDto().getFormula()
                + this.getFormulaDto().getFilter());
        if (lstColIds == null || lstColIds.isEmpty()) {
            return null;
        }
        List<Long> lstResult = new ArrayList<>(lstColIds.size());
        for (String colId : lstColIds) {
            lstResult.add(Long.parseLong(colId));
        }
        return lstResult;
    }

    /**
     * 检查当前公式,是不是表内部公式
     *
     * @return
     */
    @Transient
    public boolean isInnerFormula() {
        List<Long> allRelationCol = this.getAllRelationCol();
        String version = this.getFormulaDto().getVersionCode();
        Column column = SchemaHolder.getColumn(this.getFormulaDto().getColumnId(), version);
        long tableId = column.getColumnDto().getTableId();
        if (allRelationCol == null || allRelationCol.isEmpty()) {
            return true;
        }
        for (Long colId : allRelationCol) {
            if (!SchemaHolder.getColumn(colId, version).getColumnDto().getTableId().equals(tableId)) {
                return false;
            }
        }
        return true;

    }
}
