package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.common.IdGenerator;
import com.ranranx.aolie.datameta.dto.ColumnDto;
import com.ranranx.aolie.datameta.dto.FormulaDto;
import com.ranranx.aolie.datameta.dto.TableDto;
import com.ranranx.aolie.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.exceptions.NotExistException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:34
 * @Version V0.0.1
 **/
public class TableInfo {

    private TableDto tableDto;

    public TableInfo() {

    }

    public TableInfo(TableDto tableDto) {
        this.tableDto = tableDto;
    }

    /**
     * 表内列表
     */
    private List<Column> lstColumn = new ArrayList<>();

    public String getDsKey() {
        if (tableDto.getDataOperId() == null) {
            return DataSourceUtils.getDefaultDataSourceKey();
        }
        DataOperatorInfo dataOperatorInfo = SchemaHolder.getDataOperatorInfo(tableDto.getDataOperId(), tableDto.getVersionCode());
        if (dataOperatorInfo == null) {
            throw new NotExistException("数据库连接:[" + tableDto.getDataOperId() + "__" + tableDto.getVersionCode() + "]不存在");
        }
        return dataOperatorInfo.getDsKey();
    }

    /**
     * 取得所有列对象
     *
     * @param schemaId
     * @param versionCode
     * @return
     */
    public List<ColumnDto> getColumnDtos(Long schemaId, String versionCode) {
        List<ColumnDto> lstResult = new ArrayList<>();
        if (lstColumn != null && !lstColumn.isEmpty()) {
            lstColumn.forEach((column -> {
                ColumnDto dto = column.getColumnDto();
                dto.setSchemaId(schemaId);
                dto.setVersionCode(versionCode);
                lstResult.add(dto);
            }));
        }
        return lstResult;
    }

    /**
     * 取得所有公式对象
     *
     * @param schemaId
     * @param versionCode
     * @return
     */
    public List<FormulaDto> getFormulaDtos(Long schemaId, String versionCode) {
        List<FormulaDto> lstResult = new ArrayList<>();
        if (lstColumn != null && !lstColumn.isEmpty()) {
            lstColumn.forEach((column -> {
                List<Formula> lstFormula = column.getLstFormula();
                if (lstFormula != null && !lstFormula.isEmpty()) {
                    for (int i = 0; i < lstFormula.size(); i++) {
                        FormulaDto dto = lstFormula.get(i).getFormulaDto();
                        dto.setOrderNum(i + 1);
                        dto.setColumnId(column.getColumnDto().getColumnId());
                        dto.setSchemaId(schemaId);
                        dto.setVersionCode(versionCode);
                        lstResult.add(dto);
                    }
                }
            }));
        }
        return lstResult;
    }

    /**
     * 取得所有公式对象
     *
     * @param schemaId
     * @param versionCode
     * @return
     */
    public List<Formula> getFormulas(Long schemaId, String versionCode) {
        List<Formula> lstResult = new ArrayList<>();
        if (lstColumn != null && !lstColumn.isEmpty()) {
            lstColumn.forEach((column -> {
                List<Formula> lstFormula = column.getLstFormula();
                if (lstFormula != null && !lstFormula.isEmpty()) {
                    for (int i = 0; i < lstFormula.size(); i++) {
                        FormulaDto dto = lstFormula.get(i).getFormulaDto();
                        dto.setOrderNum(i + 1);
                        dto.setColumnId(column.getColumnDto().getColumnId());
                        dto.setSchemaId(schemaId);
                        dto.setVersionCode(versionCode);
                        lstResult.add(lstFormula.get(i));
                    }
                }
            }));
        }
        return lstResult;
    }

    /**
     * 修改列ID,并将变化的列返回
     */
    public List<Long[]> updateColId() {
        if (this.tableDto.getTableId() < 0) {
            long tableId = IdGenerator.getNextId(TableInfo.class.getName());
            this.tableDto.setTableId(tableId);
            if (lstColumn != null && !lstColumn.isEmpty()) {
                this.lstColumn.forEach(column -> column.updateTableId(tableId));
            }
        }
        return validateColumn();

    }

    public void columnIdChanged(List<Long[]> columnIds) {
        if (this.lstColumn != null && !this.lstColumn.isEmpty()) {
            for (Column column : this.lstColumn) {
                column.columnIdChanged(columnIds);
            }
        }
    }

    private List<Long[]> validateColumn() {
        if (this.lstColumn != null && !this.lstColumn.isEmpty()) {
            List<Long[]> lstResult = new ArrayList<>();
            for (Column column : this.lstColumn) {
                if (column.getColumnDto().getColumnId() < 0) {
                    long newId = IdGenerator.getNextId(Column.class.getName());
                    lstResult.add(new Long[]{column.getColumnDto().getColumnId(), newId});
                    column.getColumnDto().setColumnId(newId);
                }
            }
            return lstResult;
        }
        return null;

    }

    /**
     * 表内约束
     */
    private List<Constraint> lstConstrant;

    public List<Column> getLstColumn() {
        return lstColumn;
    }

    public void addColumn(Column column) {
        lstColumn.add(column);
    }

    public void setLstColumn(List<Column> lstColumn) {
        this.lstColumn = lstColumn;
    }

    public TableDto getTableDto() {
        return tableDto;
    }

    public void setTableDto(TableDto tableDto) {
        this.tableDto = tableDto;
    }

    public List<Constraint> getLstConstrant() {
        return lstConstrant;
    }

    public void setLstConstrant(List<Constraint> lstConstrant) {
        this.lstConstrant = lstConstrant;
    }
}
