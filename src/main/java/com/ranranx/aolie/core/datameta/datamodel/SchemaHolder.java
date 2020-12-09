package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.dto.*;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.service.DataModelService;
import com.ranranx.aolie.core.service.UIService;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @Author xxl
 * @Description 暂时使用这个来手动组装, 并将过程中的数据缓存起来
 * @Date 2020/8/6 10:53
 * @Version V0.0.1
 **/
@org.springframework.stereotype.Component
public class SchemaHolder {


    private static UIService uiService;
    private static DataModelService service;
    private static SchemaHolder thas;
    /**
     * 记录动态查询的次数,出现在新增的情况下
     */
    private Map<String, Object> triedSchema = new HashMap<>();

    /**
     * 所有表信息 key:SCHEMA_VERSION value:Table
     */
    private static Map<String, TableInfo> mapTables;
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

    public SchemaHolder(DataModelService service, UIService uiService) {
        SchemaHolder.service = service;
        SchemaHolder.uiService = uiService;
        SchemaHolder.thas = this;
    }

    public static SchemaHolder getInstance() {
        return SchemaHolder.thas;
    }


    public static Component getField(Long fieldId, String version) {
        return mapFields.get(CommonUtils.makeKey(fieldId.toString(), version));
    }

    public static TableInfo getTable(Long tableId, String version) {
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

    public List<ReferenceDto> getReferenceDtos() {
        if (mapSchema.isEmpty()) {
            return null;
        }
        List<Reference> lstReference = mapSchema.values().iterator().next().getLstReference();
        List<ReferenceDto> lstDto = new ArrayList<>();
        for (Reference reference : lstReference) {
            lstDto.add(reference.getReferenceDto());
        }
        return lstDto;
    }

    /**
     * 取得并生成
     *
     * @param version
     * @return
     */

    public Schema getSchema(Long schemaId, String version) {
        String code = CommonUtils.makeKey(schemaId.toString(),
                version);
        Schema schema = mapSchema.get(code);
        if (mapSchema == null) {
            synchronized (SchemaHolder.class) {
                if (mapSchema == null) {
                    refresh();
                }
                schema = mapSchema.get(code);
            }
        }
        if (schema == null) {
            synchronized (SchemaHolder.class) {
                if (schema == null) {
                    if (triedSchema.containsKey(code)) {//如果已尝试过,则直接返回
                        return null;
                    }
                    triedSchema.put(code, null);
                    initSchema(schemaId, version);
                    schema = mapSchema.get(code);
                }
            }


        }
        return schema;
    }

    public static BlockViewer getViewerInfo(Long blockId, String version) {
        return uiService.getViewerInfo(blockId, version);
    }

    /**
     * 保存方案
     *
     * @param schema
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
    public String saveSchema(Schema schema) {
        String err = service.saveSchema(schema);
        refresh();
        return err;
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
            initSchema(dto);
        }
        initOperator();


    }


    private void initOperator() {
        List<DataOperatorInfo> lstOperInfo = service.findAllOperatorInfo();
        if (lstOperInfo != null && !lstOperInfo.isEmpty()) {
            lstOperInfo.forEach(info -> {
                mapOperatorInfo.put(CommonUtils.makeKey(info.getOperatorDto().getId().toString(),
                        info.getOperatorDto().getVersionCode()), info);
            });
        }
    }

    private void initSchema(Long schemaId, String version) {
        SchemaDto schemaDto = service.findSchemaDto(schemaId, version);
        if (schemaDto == null) {
            return;
        }
        initSchema(schemaDto);
    }

    /**
     * 初始化单个方案
     *
     * @param dto
     */
    public void initSchema(SchemaDto dto) {
        service.clearSchemaCache(dto.getSchemaId(), dto.getVersionCode());
        clearCache(dto.getSchemaId(), dto.getVersionCode());

        Schema schema = new Schema(dto);
        initReference(schema);
        setSchemaTable(schema);
        setTableColumn(schema);
        setTableReference(schema);
        setSchemaConstraint(schema);
        setSchemaFormula(schema);
        setSchemaRelation(schema);
        mapSchema.put(CommonUtils.makeKey(dto.getSchemaId().toString(),
                dto.getVersionCode()), schema);
    }

    private void clearCache(long schemaId, String version) {
        if (mapTables != null && !mapTables.isEmpty()) {
            Iterator<Map.Entry<String, TableInfo>> iterator = mapTables.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, TableInfo> entry = iterator.next();
                if (entry.getValue().getTableDto().getSchemaId() == schemaId
                        && entry.getValue().getTableDto().getVersionCode().equals(version)) {
                    iterator.remove();
                }
            }
        }
        if (mapColumns != null && !mapColumns.isEmpty()) {
            Iterator<Map.Entry<String, Column>> iterator = mapColumns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Column> entry = iterator.next();
                if (entry.getValue().getColumnDto().getSchemaId() == schemaId
                        && entry.getValue().getColumnDto().getVersionCode().equals(version)) {
                    iterator.remove();
                }
            }
        }
        if (mapFormula != null && !mapFormula.isEmpty()) {
            Iterator<Map.Entry<String, List<Formula>>> iterator = mapFormula.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<Formula>> entry = iterator.next();
                if (entry.getValue().get(0).getFormulaDto().getSchemaId() == schemaId
                        && entry.getValue().get(0).getFormulaDto().getVersionCode().equals(version)) {
                    iterator.remove();
                }
            }
        }
    }

    public void initSchema(long schemaId, String version) {
        if (schemaId < 0 || CommonUtils.isEmpty(version)) {
            throw new InvalidParamException("初始化失败,参数不下正确,schemaId:" + schemaId + "; version:" + version);
        }
        SchemaDto schemaDto = service.findSchemaDto(schemaId, version);
        if (schemaDto == null) {
            throw new NotExistException("指定的方案不存在:schemaId:" + schemaId + "; version:" + version);
        }
        this.initSchema(schemaDto);
    }

    private void setSchemaRelation(Schema schema) {
        List<TableColumnRelationDto> lstDto = service.findRelationDto(schema.getSchemaDto().getSchemaId(),
                schema.getSchemaDto().getVersionCode());
        if (lstDto == null || lstDto.isEmpty()) {
            return;
        }
        List<TableColumnRelation> lstRelation = new ArrayList<>();
        lstDto.forEach(((dto) -> {
            TableColumnRelation re = new TableColumnRelation();
            re.setDto(dto);
            re.setTableFrom(getTable(getColumn(dto.getFieldFrom(), dto.getVersionCode()).getColumnDto().getTableId(), dto.getVersionCode()));
            re.setTableTo(getTable(getColumn(dto.getFieldTo(), dto.getVersionCode()).getColumnDto().getTableId(), dto.getVersionCode()));
            lstRelation.add(re);
        }));
        schema.setLstRelation(lstRelation);
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
//        List<Formula> lstFormula = new ArrayList<>();
        List<Formula> formulas;
        if (mapFormula == null) {
            mapFormula = new HashMap<>();
        }

        for (FormulaDto dto : lstDto) {
            formula = new Formula(dto);
//            lstFormula.add(formula);
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
//        schema.setLstFormula(lstFormula);
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
            List<TableInfo> lstTable = new ArrayList<>();
            for (TableDto tableDto : schemaTables) {
                TableInfo table = new TableInfo(tableDto);
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

    /**
     * 装配引用信息
     *
     * @param schema
     */
    private void setTableReference(Schema schema) {
        //如果是全局引用的方案,才需要配置引用信息
        if (!SchemaTools.isReferenceSchema(schema.getSchemaDto().getSchemaId())) {
            return;
        }
        List<ReferenceDto> schemaReferences = service.findSchemaReferences(schema.getSchemaDto().getVersionCode());
        List<TableInfo> lstTable = schema.getLstTable();
        if (lstTable == null || lstTable.isEmpty()) {
            return;
        }
        Map<Long, List<ReferenceDto>> mapReference = makeReferenceMap(schemaReferences);
        if (mapReference.isEmpty()) {
            return;
        }
        for (TableInfo info : lstTable) {
            info.setLstReference(mapReference.get(info.getTableDto().getTableId()));
        }
    }

    /**
     * 将引用分类组装
     *
     * @param schemaReferences
     * @return
     */
    private Map<Long, List<ReferenceDto>> makeReferenceMap(List<ReferenceDto> schemaReferences) {
        if (schemaReferences == null || schemaReferences.isEmpty()) {
            return new HashMap<>();
        }
        Map<Long, List<ReferenceDto>> result = new HashMap<>();
        for (ReferenceDto dto : schemaReferences) {
            List<ReferenceDto> referenceDtos = result.get(dto.getTableId());
            if (referenceDtos == null) {
                referenceDtos = new ArrayList<>();
                result.put(dto.getTableId(), referenceDtos);
            }
            referenceDtos.add(dto);
        }
        return result;
    }

    public void deleteSchema(long schemaId, String version) {
        service.deleteSchemaForPublic(schemaId, version);
        this.refresh();
    }

    /**
     * 同步更新表列信息,保存到数据库中
     *
     * @param tableId
     * @param version
     */
    public List<ColumnDto> syncTableColumn(long tableId, String version) {
        List<ColumnDto> columnDtos = service.syncTableColumn(tableId, version);
        refresh();
        return columnDtos;
    }


    private void initReference(Schema schema) {
        List<ReferenceDto> schemaReferences = service.findSchemaReferences(
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
