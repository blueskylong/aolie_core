package com.ranranx.aolie.core.datameta.datamodel;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.dto.*;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.interfaces.ISystemIniter;
import com.ranranx.aolie.core.service.DataModelService;
import com.ranranx.aolie.core.service.UIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author xxl
 * 暂时使用这个来手动组装, 并将过程中的数据缓存起来
 * @version V0.0.1
 * @date 2020/8/6 10:53
 **/
@org.springframework.stereotype.Component
public class SchemaHolder {
    Logger logger = LoggerFactory.getLogger(SchemaHolder.class);

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
     * 系统初始化器
     */
    @Autowired(required = false)
    private List<ISystemIniter> lstIniter;

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

    public List<ReferenceDto> getReferenceDtos(String version) {
        return service.findAllReferences(version);
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
                    //如果已尝试过,则直接返回
                    if (triedSchema.containsKey(code)) {
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

    public String[] getAllVersionCode() {
        return service.getAllVersionCode();
    }

    /**
     * 初始化数据.
     */
    @PostConstruct
    public void refresh() {
        logger.info("---初始化方案");
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
        List<String> lstVersion = new ArrayList<>();
        for (SchemaDto dto : allSchemaDto) {
            initSchema(dto);
            if (lstVersion.indexOf(dto.getVersionCode()) == -1) {
                lstVersion.add(dto.getVersionCode());
                initReference(dto.getVersionCode());
            }
        }
        initOperator();
        logger.info("---初始化方案完成");

        if (this.lstIniter != null && !this.lstIniter.isEmpty()) {
            logger.info("---初始化其它服务");
            //先排序
            lstIniter.sort((a, b) -> {
                return a.getOrder() > b.getOrder() ? -1 : 1;
            });
            logger.info("---初始化其它服务完成");
        }
        lstIniter.forEach(el -> {
            el.init();
        });
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
        clearViewChange(dto.getSchemaId(), dto.getVersionCode());
        service.clearSchemaCache(dto.getSchemaId(), dto.getVersionCode());
        service.clearSchemaCache2(dto.getVersionCode());
        clearCache(dto.getSchemaId(), dto.getVersionCode());

        Schema schema = new Schema(dto);
//        initReference(schema);
        setSchemaTable(schema);
        setTableColumn(schema);
        setTableReference(schema);
        setSchemaConstraint(schema);
        setSchemaFormula(schema);
        setSchemaRelation(schema);
        mapSchema.put(CommonUtils.makeKey(dto.getSchemaId().toString(),
                dto.getVersionCode()), schema);
    }

    private void clearViewChange(Long schemaId, String version) {
        List<BlockViewDto> blockViews = uiService.getBlockViews(schemaId);
        if (blockViews != null) {
            for (BlockViewDto blockView : blockViews) {
                uiService.clearViewCache(blockView.getBlockViewId(), version);
            }
        }

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

    private void initReference(String versionCode) {
        List<ReferenceDto> schemaReferences = service.findAllReferences(versionCode);
        if (schemaReferences == null || schemaReferences.isEmpty()) {
            return;
        }
        for (ReferenceDto dto : schemaReferences) {
            Reference reference = new Reference(dto);
            mapReference.put(CommonUtils.makeKey(dto.getRefId().toString(), dto.getVersionCode()), reference);
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
        List<ReferenceDto> schemaReferences = service.findAllReferences(schema.getSchemaDto().getVersionCode());
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

    /**
     * 取得表关系,可以是多个
     *
     * @param versionCode
     * @param tableIds
     * @return
     */
    public static List<TableColumnRelation> getTableRelations(String versionCode, Long... tableIds) {
        if (tableIds == null || tableIds.length < 1) {
            return null;
        }
        long schemaId = SchemaHolder.getTable(tableIds[0], versionCode).getTableDto().getSchemaId();
        return SchemaHolder.getInstance().getSchema(schemaId, versionCode).getTablesRelation(tableIds);
    }


    public static TableInfo findTableByTableName(String tableName, long schemaId, String version) {

        if (schemaId < 1) {
            List<TableInfo> lstTables = findTablesByTableName(tableName, version);
            if (lstTables == null) {
                return null;
            }
            if (lstTables.size() > 1) {
                throw new InvalidParamException("查询到多个表对象");
            }
            return lstTables.get(0);
        }
        Schema schema = getInstance().getSchema(schemaId, version);
        if (schema == null) {
            return null;
        }
        return schema.findTableByName(tableName);
    }

    /**
     * 查询所有方案中包含的表
     *
     * @param tableName
     * @param version
     * @return
     */
    public static List<TableInfo> findTablesByTableName(String tableName, String version) {
        Iterator<Schema> iterator = mapSchema.values().iterator();
        List<TableInfo> lstResult = new ArrayList<>();
        while (iterator.hasNext()) {
            Schema schema = iterator.next();
            if (schema.getSchemaDto().getVersionCode().equals(version)) {
                TableInfo tableInfo = schema.findTableByName(tableName);
                if (tableInfo != null) {
                    lstResult.add(tableInfo);
                }
            }

        }
        return lstResult;
    }

    /**
     * 根据DTO类查询表定义
     *
     * @param clazz
     * @param schemaId
     * @param version
     * @return
     */
    public static TableInfo findTableByDto(Class clazz, long schemaId, String version) {
        String tableName = CommonUtils.getTableName(clazz);
        Schema schema = getInstance().getSchema(schemaId, version);
        if (schema == null) {
            return null;
        }
        return schema.findTableByName(tableName);
    }


//    private void initReference(Schema schema) {
//        List<ReferenceDto> schemaReferences = service.findAllReferences(
//                schema.getSchemaDto().getVersionCode());
//        if (schemaReferences == null || schemaReferences.isEmpty()) {
//            return;
//        }
//        List<Reference> lstReference = new ArrayList<>();
//        for (ReferenceDto dto : schemaReferences) {
//            Reference reference = new Reference(dto);
//            lstReference.add(reference);
//            mapReference.put(CommonUtils.makeKey(dto.getRefId().toString(), dto.getVersionCode()), reference);
//        }
//        schema.setLstReference(lstReference);
//    }

}
