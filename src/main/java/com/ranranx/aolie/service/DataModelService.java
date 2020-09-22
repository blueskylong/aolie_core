package com.ranranx.aolie.service;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.common.Constants;
import com.ranranx.aolie.common.IdGenerator;
import com.ranranx.aolie.datameta.datamodel.*;
import com.ranranx.aolie.datameta.dto.*;
import com.ranranx.aolie.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.ds.definition.*;
import com.ranranx.aolie.handler.param.condition.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/31 16:46
 * @Version V0.0.1
 **/
@Service
public class DataModelService {

    public static final String GROUP_NAME = "SCHEMA_VERSION";
    private static final String KEY_SCHEMA_DTO = "SchemaDto";
    private static final String KEY_TABLE_DTO = "'TableDto_'+#p0+'_'+#p1";
    private static final String KEY_COLUMN_DTO = "'ColumnDto_'+#p0+'_'+#p1";
    private static final String KEY_REFERENCE_DTO = "'ReferenceDto_'+#p0+'_'+#p1";
    private static final String KEY_CONSTRAINT_DTO = "'ConstraintDto_'+#p0+'_'+#p1";
    private static final String KEY_FORMULA_DTO = "'ConstraintDto_'+#p0+'_'+#p1";
    private static final String KEY_VIEWER = "'VIEWER_'+#p0+'_'+#p1";
    private static final String KEY_REFERENCE = "'REFERENCE_'+#p0+'_'+#p1";


    @Autowired
    private DataOperatorFactory factory;

    @Caching(evict = {@CacheEvict(value = GROUP_NAME, key = KEY_TABLE_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_COLUMN_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_REFERENCE_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_CONSTRAINT_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_FORMULA_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_REFERENCE_DTO)})
    public void clearSchemaCache(long schemaId, String version) {

    }

    @Cacheable(value = GROUP_NAME, key = (KEY_TABLE_DTO))
    public List<TableDto> findSchemaTables(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(TableDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, TableDto.class);
    }

    @Cacheable(value = GROUP_NAME, key = (KEY_FORMULA_DTO))
    public List<FormulaDto> findSchemaFormula(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(FormulaDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, FormulaDto.class);
    }

    @Cacheable(value = GROUP_NAME,
            key = KEY_COLUMN_DTO)
    public List<ColumnDto> findSchemaColumns(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ColumnDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        queryParamDefinition.addOrder(new FieldOrder(null, "field_index", true, 0));
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, ColumnDto.class);
    }

    @Cacheable(value = GROUP_NAME,
            key = KEY_REFERENCE_DTO)
    public List<ReferenceDto> findSchemaReferences(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ReferenceDto.class);
        queryParamDefinition.appendCriteria()
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, ReferenceDto.class);
    }

    @Cacheable(value = GROUP_NAME,
            key = KEY_CONSTRAINT_DTO)
    public List<ConstraintDto> findSchemaConstraints(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ConstraintDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, ConstraintDto.class);
    }


    public List<SchemaDto> findAllSchemaDto(boolean isEnabled) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(SchemaDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("enabled", 1);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, SchemaDto.class);
    }

    @Cacheable(value = GROUP_NAME,
            key = KEY_VIEWER)
    public BlockViewer getViewerInfo(Long blockViewId, String version) {
        BlockViewDto viewerDto = findViewerDto(blockViewId, version);
        if (viewerDto == null) {
            return null;
        }
        return new BlockViewer(viewerDto, findViewerComponents(blockViewId, version));
    }


    /**
     * 查询视图主信息
     *
     * @param blockViewId
     * @param version
     * @return
     */
    private BlockViewDto findViewerDto(Long blockViewId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(BlockViewDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("block_view_id", blockViewId)
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator().selectOne(queryParamDefinition, BlockViewDto.class);
    }

    /**
     * 查询一视图的所有组件
     *
     * @param blockViewId
     * @param version
     * @return
     */
    private List<Component> findViewerComponents(Long blockViewId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ComponentDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("block_view_id", blockViewId)
                .andEqualTo("version_code", version);
        queryParamDefinition
                .addOrder(new FieldOrder(CommonUtils.getTableName(ComponentDto.class), "lvl_code", true, 1));
        List<ComponentDto> lstDto = factory.getDefaultDataOperator().select(queryParamDefinition, ComponentDto.class);
        //下面开始组装
        if (lstDto == null || lstDto.isEmpty()) {
            return null;
        }
        List<Component> lstResult = new ArrayList<>(lstDto.size());
        Component component;
        for (ComponentDto dto : lstDto) {
            lstResult.add(new Component(dto, SchemaHolder.getColumn(dto.getColumnId(), dto.getVersionCode())));
        }
        return lstResult;
    }

    /**
     * 查询引用数据
     *
     * @param referenceId
     * @param version
     * @return
     */
    @Cacheable(value = (GROUP_NAME),
            key = KEY_REFERENCE)
    public List<ReferenceData> findReferenceData(long referenceId, String version) {
        Reference reference = SchemaHolder.getReference(referenceId, version);
        if (reference == null) {
            return null;
        }
        String tableName = reference.getReferenceDto().getTableName();
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableNames(tableName);
        List<Field> lstField = new ArrayList<>();
        Field field = new Field();
        field.setTableName(tableName);
        field.setFieldName(reference.getReferenceDto().getIdField() + " as id");
        lstField.add(field);

        field = new Field();
        field.setTableName(tableName);
        field.setFieldName(reference.getReferenceDto().getCodeField() + " as code");
        field.setOrderType(Constants.OrderType.ASC);
        lstField.add(field);

        field = new Field();
        field.setTableName(tableName);
        field.setFieldName(reference.getReferenceDto().getNameField() + " as name");
        lstField.add(field);
        queryParamDefinition.setFields(lstField);
        Criteria criteria = queryParamDefinition.appendCriteria();
        criteria.andEqualTo("version_code", version);
        FieldOrder order = new FieldOrder();
        order.setTableName(tableName);
        order.setAsc(true);
        order.setField(reference.getReferenceDto().getCodeField());
        queryParamDefinition.addOrder(order);
        if (CommonUtils.isNotEmpty(reference.getReferenceDto().getCommonType())) {
            criteria
                    .andEqualTo("common_type", reference.getReferenceDto().getCommonType());
        }
        return factory.getDefaultDataOperator().select(queryParamDefinition, ReferenceData.class);

    }

    /**
     * 查找没有被引用的表信息,这里只针对MYSql做的查询
     *
     * @param schemaId
     * @param version
     * @return
     */
    public List<String> findDefaultDBTablesNotInSchema(Long schemaId, String version) {
        QueryParamDefinition definition = new QueryParamDefinition();
        definition.setTableNames("information_schema.tables");
        List<Field> lstField = new ArrayList<>();
        Field field = new Field();
        field.setFieldName("table_name");
        field.setTableName("information_schema.tables");
        lstField.add(field);
        definition.setFields(lstField);

        QueryParamDefinition definitionSub = new QueryParamDefinition();
        List<Field> lstField1 = new ArrayList<>();
        Field field1 = new Field();
        field1.setFieldName("table_name");
        field1.setTableName("aolie_dm_table");
        lstField1.add(field1);
        definitionSub.setFields(lstField1);
        definitionSub.setTableNames("aolie_dm_table");
        definitionSub.appendCriteria().andEqualTo("version_code", version)
                .andEqualTo("schema_id", schemaId);
        definition.appendCriteria().andNotIn("table_name", definitionSub)
                .andCondition("TABLE_SCHEMA=(select database())");

        List<Map<String, Object>> lstData = factory.getDefaultDataOperator().select(definition);
        if (lstData == null || lstData.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> lst = new ArrayList<>();
        for (Map<String, Object> map : lstData) {
            lst.add(map.get("table_name").toString());
        }
        return lst;
    }

    /**
     * 查询一张表的字段信息,并生成DTO
     *
     * @param tableName
     * @return
     */
    public List<ColumnDto> findTableFieldAsColumnDto(String tableName) {

        QueryParamDefinition definition = new QueryParamDefinition();
        definition.setTableNames("information_schema.COLUMNS");
        definition.appendCriteria().andEqualTo("table_name", tableName)
                .andCondition("TABLE_SCHEMA=(select database())");
        List<Map<String, Object>> lstResult = factory.getDefaultDataOperator().select(definition);
        List<ColumnDto> lstDto = new ArrayList<>();

        if (lstResult != null && !lstResult.isEmpty()) {
            for (Map<String, Object> map : lstResult) {
                lstDto.add(createColumnDto(map));
            }
        }
        return lstDto;
    }

    private ColumnDto createColumnDto(Map<String, Object> map) {
        ColumnDto dto = new ColumnDto();
        dto.setFieldName(CommonUtils.getStringField(map, "COLUMN_NAME"));
        dto.setFieldType(CommonUtils.getStringField(map, "DATA_TYPE"));
        dto.setLength(CommonUtils.getIntegerField(map, "CHARACTER_MAXIMUM_LENGTH"));
        dto.setNullable("YES".equals(CommonUtils.getStringField(map, "IS_NULLABLE")) ? new Byte((byte) 1) : new Byte((byte) 0));
        dto.setDefaultValue(CommonUtils.getStringField(map, "COLUMN_DEFAULT"));
        dto.setPrecisionNum(CommonUtils.getIntegerField(map, "NUMERIC_SCALE"));
        dto.setTitle(dto.getFieldName());
        return dto;
    }

    /**
     * 保存方案
     *
     * @param schema
     * @return
     */
    public String saveSchema(Schema schema) {

        String sErr = validateSchema(schema);
        if (!CommonUtils.isEmpty(sErr)) {
            return sErr;
        }
        deleteSchema(schema.getSchemaDto().getSchemaId(), schema.getSchemaDto().getVersionCode());
        //更新新增加的ID
        updateIds(schema);
        if (schema.getSchemaDto().getSchemaId() == 1) {
            return "系统保留方案不允许修改";
        }
        saveData(schema);
        return "";
    }

    private void saveData(Schema schema) {

        InsertParamDefinition insertParamDefinition = new InsertParamDefinition();
        insertParamDefinition.setNeedConvertToUnderLine(true);
        insertParamDefinition.setObject(schema.getSchemaDto());
        factory.getDefaultDataOperator().insert(insertParamDefinition);

        List<FormulaDto> formulas = schema.getFormulas();
        if (formulas != null && !formulas.isEmpty()) {
            insertParamDefinition.setObjects(formulas);
            insertParamDefinition.setTableDto(FormulaDto.class);
            factory.getDefaultDataOperator().insert(insertParamDefinition);
        }
        List<ColumnDto> lstColumnDto = schema.getColumnDtos();
        if (lstColumnDto != null && !lstColumnDto.isEmpty()) {
            insertParamDefinition.setObjects(lstColumnDto);
            insertParamDefinition.setTableDto(ColumnDto.class);
            factory.getDefaultDataOperator().insert(insertParamDefinition);
        }

        List<ConstraintDto> constraintDtos = schema.getConstraintDtos();
        if (constraintDtos != null && !constraintDtos.isEmpty()) {
            insertParamDefinition.setObjects(constraintDtos);
            insertParamDefinition.setTableDto(ConstraintDto.class);
            factory.getDefaultDataOperator().insert(insertParamDefinition);
        }

        List<TableColumnRelationDto> relations = schema.getRelationDto();
        if (relations != null && !relations.isEmpty()) {
            insertParamDefinition.setObjects(relations);
            insertParamDefinition.setTableDto(TableColumnRelationDto.class);
            factory.getDefaultDataOperator().insert(insertParamDefinition);
        }

        List<TableDto> lstTableDtos = schema.getTableDtos();
        if (lstTableDtos != null && !lstTableDtos.isEmpty()) {
            insertParamDefinition.setTableDto(TableDto.class);
            insertParamDefinition.setObjects(lstTableDtos);
            factory.getDefaultDataOperator().insert(insertParamDefinition);
        }
    }


    private String validateSchema(Schema schema) {
        return null;
    }

    /**
     * 处理临时id
     *
     * @param schema
     */
    private void updateIds(Schema schema) {
        if (schema.getSchemaDto().getSchemaId() < 0) {
            schema.getSchemaDto().setSchemaId(IdGenerator.getNextId(Schema.class.getName()));
        }
        List<Long[]> lstChangedColumn = new ArrayList<>();
        if (schema.getLstTable() != null) {
            for (TableInfo table : schema.getLstTable()) {
                List<Long[]> changedIds = table.updateColId();
                if (changedIds != null) {
                    lstChangedColumn.addAll(changedIds);
                }
            }
            if (!lstChangedColumn.isEmpty()) {
                for (TableInfo table : schema.getLstTable()) {
                    table.columnIdChanged(lstChangedColumn);
                }
            }
        }
        if (schema.getLstRelation() != null) {
            schema.getLstRelation().forEach((relation -> {
                relation.getDto().setSchemaId(schema.getSchemaDto().getSchemaId());
                relation.getDto().setVersionCode(
                        schema.getSchemaDto().getVersionCode());
                if (relation.getDto().getId() < 0) {
                    relation.getDto().setId(IdGenerator.getNextId(TableColumnRelation.class.getName()));
                }
            }));
        }
        if (!lstChangedColumn.isEmpty()) {
            if (schema.getLstConstraint() != null) {
                for (Constraint constraint : schema.getLstConstraint()) {
                    constraint.columnIdChanged(lstChangedColumn);
                }
            }
            List<TableColumnRelation> lstRelation = schema.getLstRelation();
            if (lstRelation != null && !lstRelation.isEmpty()) {
                lstRelation.forEach((relation -> {
                    relation.columnIdChanged(lstChangedColumn);
                }));
            }
        }
    }

    private void deleteSchema(Long schemaId, String version) {
        //方案表
        DeleteParamDefinition deleteParamDefinition = new DeleteParamDefinition();
        deleteParamDefinition.setTableDto(SchemaDto.class);
        deleteParamDefinition.getCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);

        deleteParamDefinition.setTableDto(TableDto.class);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);

        deleteParamDefinition.setTableDto(FormulaDto.class);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);

        deleteParamDefinition.setTableDto(ConstraintDto.class);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);

        deleteParamDefinition.setTableDto(TableColumnRelationDto.class);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);

        deleteParamDefinition.setTableDto(ColumnDto.class);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);
    }

    /**
     * 查询所有的数据操作信息
     *
     * @return
     */
    public List<DataOperatorInfo> findAllOperatorInfo() {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(DataOperatorDto.class);
        List<DataOperatorDto> lstDto = factory.getDefaultDataOperator().select(queryParamDefinition, DataOperatorDto.class);
        if (lstDto != null && !lstDto.isEmpty()) {
            List<DataOperatorInfo> lstInfo = new ArrayList<>();
            lstDto.forEach((dataOperatorDto -> {
                lstInfo.add(new DataOperatorInfo(dataOperatorDto));
            }));
            return lstInfo;
        }
        return null;
    }

    public List<TableColumnRelationDto> findRelationDto(Long schemaId, String version) {
        QueryParamDefinition definition = new QueryParamDefinition();
        definition.setTableDtos(TableColumnRelationDto.class);
        definition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator().select(definition, TableColumnRelationDto.class);
    }

}
