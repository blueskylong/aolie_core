package com.ranranx.aolie.datameta.datamodel;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.datameta.dto.*;
import com.ranranx.aolie.service.DataModelService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description 暂时使用这个来手动组装, 并将过程中的数据缓存起来
 * @Date 2020/8/6 10:53
 * @Version V0.0.1
 **/
@org.springframework.stereotype.Component
public class SchemaHolder {


    private static DataModelService service;

    /**
     * 所有表信息 key:SCHEMA_VERSION value:Table
     */
    private static Map<String, Table> mapTables;
    /**
     * 所有显示字段信息
     */
    private static Map<String, Component> mapFields;

    /**
     * 所有字段信息
     */
    private static Map<String, Column> mapColumns;
    /**
     * 表和字段对应信息
     */
    private static Map<String, Long> mapFieldToTable;

    /**
     * 公式缓存 ,key:COLUMN_ID_VERSION value:公式列表
     */
    private static Map<String, List<Formula>> mapFormula;

    /**
     * 数据库操作信息列表
     */
    private static Map<String, DataOperatorInfo> mapOperatorInfo;

    /**
     * 引用信息
     */
    private static Map<String, Reference> mapReference;

    /**
     * 版本对库
     */
    private static Map<String, Schema> mapSchema;

    public SchemaHolder(DataModelService service) {
        SchemaHolder.service = service;
    }


    public static Component getField(Long fieldId, String version) {
        return mapFields.get(CommonUtils.makeKey(fieldId.toString(), version));
    }

    public static Table getTable(Long tableId, String version) {
        return mapTables.get(CommonUtils.makeKey(tableId.toString(), version));
    }

    public static DataOperatorInfo getDataOperatorInfo(Long id, String version) {
        return mapOperatorInfo.get(CommonUtils.makeKey(id.toString(), version));
    }


    public static Column getColumn(Long id, String version) {
        return mapColumns.get(CommonUtils.makeKey(id.toString(), version));
    }

    public static Reference getReference(Long id, String version) {
        return mapReference.get(CommonUtils.makeKey(id.toString(), version));
    }

    /**
     * 取得并生成
     *
     * @param version
     * @return
     */

    public Schema getSchema(Long schemaId, String version) {
        Schema schema = mapSchema.get(CommonUtils.makeKey(schemaId.toString(),
                version));
        if (mapSchema == null) {
            synchronized (SchemaHolder.class) {
                if (mapSchema == null) {
                    refresh();
                }
                return mapSchema.get(version);

            }
        }
        return mapSchema.get(version);
    }

    public static BlockViewer getViewerInfo(Long blockId, String version) {
        return service.getViewerInfo(blockId, version);
    }

    /**
     * 初始化数据.
     */
    @PostConstruct
    public void refresh() {
        mapTables = new HashMap<>(20);
        mapFields = new HashMap<>(200);
        mapFieldToTable = new HashMap<>(200);
        mapSchema = new HashMap<>(200);
        mapOperatorInfo = new HashMap<>(200);
        mapColumns = new HashMap<>(200);
        mapReference = new HashMap<>(200);
        List<SchemaDto> allSchemaDto = service.findAllSchemaDto(true);
        if (allSchemaDto == null || allSchemaDto.isEmpty()) {
            return;
        }
        for (SchemaDto dto : allSchemaDto) {
            mapSchema.put(CommonUtils.makeKey(dto.getSchemaId().toString(),
                    dto.getVersionCode()), initSchema(dto));
        }


    }

    /**
     * 初始化单个方案
     *
     * @param schemaDto
     */
    private Schema initSchema(SchemaDto schemaDto) {
        Schema schema = new Schema(schemaDto);
        initReference(schema);
        setSchemaTable(schema);
        setTableColumn(schema);
        setSchemaConstraint(schema);
        setSchemaFormula(schema);
        return schema;
    }

    /**
     * 初始化方案中的约束
     *
     * @param schema
     */
    private void setSchemaConstraint(Schema schema) {
        List<ConstraintDto> lstDto = service.findSchemaConstraints(schema.getSchemaDto().getSchemaId(),
                schema.getSchemaDto().getVersionCode());
        if (lstDto == null || lstDto.isEmpty()) {
            return;
        }
        List<Constraint> lstConstraint = new ArrayList<>();
        for (ConstraintDto dto : lstDto) {
            lstConstraint.add(new Constraint(dto));
        }
        schema.setLstConstraint(lstConstraint);
    }

    /**
     * 初始化公式信息
     *
     * @param schema
     */
    private void setSchemaFormula(Schema schema) {
        List<FormulaDto> lstDto = service.findSchemaFormula(schema.getSchemaDto().getSchemaId(),
                schema.getSchemaDto().getVersionCode());
        if (lstDto == null || lstDto.isEmpty()) {
            return;
        }
        Formula formula;
        List<Formula> lstFormula = new ArrayList<>();
        List<Formula> formulas;

        for (FormulaDto dto : lstDto) {
            formula = new Formula(dto);
            lstFormula.add(formula);
            String columnKey = CommonUtils.makeKey(dto.getColumnId().toString(), dto.getVersionCode());
            formulas = mapFormula.get(columnKey);
            if (formulas == null) {
                formulas = new ArrayList<>();
                mapFormula.put(columnKey, formulas);
            }
            formulas.add(formula);
            Column column = mapColumns.get(columnKey);
            column.setLstFormula(formulas);

        }
        schema.setLstFormula(lstFormula);
    }

    /**
     * 设置方案中的表信息,还没有包含字段信息
     *
     * @param schema
     */
    private void setSchemaTable(Schema schema) {
        List<TableDto> schemaTables = service.findSchemaTables(schema.getSchemaDto().getSchemaId(),
                schema.getSchemaDto().getVersionCode());
        if (schemaTables != null && !schemaTables.isEmpty()) {
            List<Table> lstTable = new ArrayList<>();
            for (TableDto tableDto : schemaTables) {
                Table table = new Table(tableDto);
                lstTable.add(table);
                mapTables.put(CommonUtils.makeKey(String.valueOf(tableDto.getTableId()), tableDto.getVersionCode()), table);
            }
            schema.setLstTable(lstTable);
        }
    }

    private void setTableColumn(Schema schema) {
        List<ColumnDto> lstDto = service.findSchemaColumns(schema.getSchemaDto().getSchemaId(),
                schema.getSchemaDto().getVersionCode());
        if (lstDto == null || lstDto.isEmpty()) {
            return;
        }
        Column column;
        List<Column> lstColumn = new ArrayList<>();
        for (ColumnDto dto : lstDto) {
            column = new Column(dto, mapReference.get(dto.getRefId()));
            lstColumn.add(column);
            mapColumns.put(CommonUtils.makeKey(dto.getColumnId().toString()
                    , dto.getVersionCode()), column);
            //插入到对应的表中
            mapTables.get(CommonUtils.makeKey(dto.getTableId().toString(), dto.getVersionCode())).addColumn(column);
        }


    }

    private void initReference(Schema schema) {
        List<ReferenceDto> schemaReferences = service.findSchemaReferences(schema.getSchemaDto().getSchemaId(),
                schema.getSchemaDto().getVersionCode());
        if (schemaReferences == null || schemaReferences.isEmpty()) {
            return;
        }
        List<Reference> lstReference = new ArrayList<>();
        for (ReferenceDto dto : schemaReferences) {
            Reference reference = new Reference(dto);
            lstReference.add(reference);
            mapReference.put(CommonUtils.makeKey(dto.getRefId().toString(), dto.getVersionCode()), reference);
        }
        schema.setLstReference(lstReference);
    }


}
