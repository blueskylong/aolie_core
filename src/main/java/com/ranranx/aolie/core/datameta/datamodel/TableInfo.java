package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.datameta.datamodel.validator.IValidator;
import com.ranranx.aolie.core.datameta.datamodel.validator.ValidatorCenter;
import com.ranranx.aolie.core.datameta.dto.ColumnDto;
import com.ranranx.aolie.core.datameta.dto.FormulaDto;
import com.ranranx.aolie.core.datameta.dto.ReferenceDto;
import com.ranranx.aolie.core.datameta.dto.TableDto;
import com.ranranx.aolie.core.ds.dataoperator.DataSourceUtils;
import com.ranranx.aolie.core.ds.definition.FieldOrder;
import com.ranranx.aolie.core.exceptions.InvalidConfigException;
import com.ranranx.aolie.core.exceptions.NotExistException;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/5 17:34
 * @Version V0.0.1
 **/
public class TableInfo {

    private TableDto tableDto;

    private List<ReferenceDto> lstReference;

    private List<Column> lstKeyColumn;

    private String keyFieldName;
    /**
     * 验证器
     */
    private ValidatorCenter validatorCenter;

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
            Schema schema = SchemaHolder.getInstance().getSchema(tableDto.getSchemaId(), tableDto.getVersionCode());
            if (schema != null) {
                return schema.getDsKey();
            }
            return DataSourceUtils.getDefaultDataSourceKey();
        }
        DataOperatorInfo dataOperatorInfo = SchemaHolder.getDataOperatorInfo(tableDto.getDataOperId(), tableDto.getVersionCode());
        if (dataOperatorInfo == null) {
            throw new NotExistException("数据库连接:[" + tableDto.getDataOperId() + "__" + tableDto.getVersionCode() + "]不存在");
        }
        return dataOperatorInfo.getDsKey();
    }

    /**
     * 查询主键列
     *
     * @return
     */
    @Transient
    public List<Column> getKeyColumn() {
        if (this.lstKeyColumn != null) {
            return this.lstKeyColumn;
        }
        this.lstKeyColumn = new ArrayList<>();
        if (this.lstColumn == null || this.lstColumn.isEmpty()) {
            return null;
        }
        this.lstColumn.forEach(column -> {
            if (column.getColumnDto().getIsKey() != null && column.getColumnDto().getIsKey() == 1) {
                //去掉版本字段
                if (column.getColumnDto().getFieldName().equalsIgnoreCase(Constants.FixColumnName.VERSION_CODE)) {
                    return;
                }
                lstKeyColumn.add(column);
            }
        });
        return lstKeyColumn;
    }

    /**
     * 根据字段标题查询字段信息,这里注意的是,字段标题不一定唯一
     *
     * @return
     */
    @Transient
    public Column findColumnByColTitle(String title) {
        if (CommonUtils.isEmpty(title)) {
            return null;
        }
        if (this.lstColumn == null || this.lstColumn.isEmpty()) {
            return null;
        }
        for (Column column : this.lstColumn) {
            if (column.getColumnDto().getTitle().equals(title)) {
                return column;
            }
        }

        return null;
    }

    /**
     * 根据字段名查询字段信息
     *
     * @return
     */
    @Transient
    public Column findColumnByName(String fieldName) {
        if (CommonUtils.isEmpty(fieldName)) {
            return null;
        }
        if (this.lstColumn == null || this.lstColumn.isEmpty()) {
            return null;
        }
        for (Column column : this.lstColumn) {
            if (column.getColumnDto().getFieldName().equals(fieldName)) {
                return column;
            }
        }

        return null;
    }

    /**
     * 查询主键列,包含版本. 含有版本字段就默认是主键之一
     *
     * @return
     */
    @Transient
    public String getIdFieldIncludeVersionCode() {
        if (this.lstColumn == null || this.lstColumn.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        this.lstColumn.forEach(column -> {
            if (column.getColumnDto().getIsKey() != null && column.getColumnDto().getIsKey() == 1
                    || column.getColumnDto().getFieldName().equalsIgnoreCase(Constants.FixColumnName.VERSION_CODE)) {
                sb.append(column.getColumnDto().getFieldName()).append(",");
            }
        });
        return sb.substring(0, sb.length() - 1);
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
    public Map<Long, Long> updateColId(Map<Long, Long> tableChangeId) {
        if (this.tableDto.getTableId() < 0) {
            long tableId = IdGenerator.getNextId(TableInfo.class.getName());
            tableChangeId.put(this.tableDto.getTableId(), tableId);
            this.tableDto.setTableId(tableId);
            if (lstColumn != null && !lstColumn.isEmpty()) {
                this.lstColumn.forEach(column -> column.updateTableId(tableId));
            }
            //更新本表的ID
            if (this.lstReference != null) {
                for (ReferenceDto dto : this.lstReference) {
                    dto.setTableId(tableId);
                }
            }
        }
        return validateColumn();

    }

    public void columnIdChanged(Map<Long, Long> columnIds) {
        if (this.lstColumn != null && !this.lstColumn.isEmpty()) {
            for (Column column : this.lstColumn) {
                column.columnIdChanged(columnIds);
            }
        }
    }

    private Map<Long, Long> validateColumn() {
        if (this.lstColumn != null && !this.lstColumn.isEmpty()) {
            Map<Long, Long> lstResult = new HashMap<>();
            for (Column column : this.lstColumn) {
                if (column.getColumnDto().getColumnId() < 0) {
                    long newId = IdGenerator.getNextId(Column.class.getName());
                    lstResult.put(column.getColumnDto().getColumnId(), newId);
                    column.getColumnDto().setColumnId(newId);
                }
            }
            return lstResult;
        }
        return null;

    }

    /**
     * 取得主键字段
     *
     * @return
     */
    @Transient
    public String getKeyField() {
        if (CommonUtils.isNotEmpty(keyFieldName)) {
            return keyFieldName;
        }
        List<Column> keyColumn = getKeyColumn();
        if (keyColumn == null || keyColumn.size() != 1) {
            throw new InvalidConfigException("表主键定义不正确:" + this.getTableDto().getTableId());
        }
        this.keyFieldName = keyColumn.get(0).getColumnDto().getFieldName();
        return this.keyFieldName;
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

    public List<ReferenceDto> getLstReference() {
        return lstReference;
    }

    public void setLstReference(List<ReferenceDto> lstReference) {
        this.lstReference = lstReference;
    }

    @Transient
    public List<FieldOrder> getDefaultOrder() {
        if (this.lstColumn == null || this.lstColumn.isEmpty()) {
            return null;
        }
        List<FieldOrder> lstOrder = new ArrayList<>();
        for (Column column : this.lstColumn) {
            if (Constants.FixColumnName.XH.equals(column.getColumnDto().getFieldName())) {
                FieldOrder order = new FieldOrder();
                order.setAsc(true);
                order.setField(Constants.FixColumnName.XH);
                order.setTableName(this.getTableDto().getTableName());
                lstOrder.add(order);
                break;
            }
            if (Constants.FixColumnName.LVL_CODE.equals(column.getColumnDto().getFieldName())) {
                FieldOrder order = new FieldOrder();
                order.setAsc(true);
                order.setField(Constants.FixColumnName.LVL_CODE);
                order.setTableName(this.getTableDto().getTableName());
                lstOrder.add(order);
                break;
            }
        }
        return lstOrder;
    }

    @Transient
    public ValidatorCenter getValidatorCenter(List<IValidator> lstValidator) {
        if (this.validatorCenter != null) {
            return this.validatorCenter;
        }
        this.validatorCenter = new ValidatorCenter(this);
        this.validatorCenter.setValidators(lstValidator);
        return this.validatorCenter;
    }

}
