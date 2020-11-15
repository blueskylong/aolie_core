package com.ranranx.aolie.core.service;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;
import com.ranranx.aolie.core.datameta.dto.*;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.*;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/31 16:46
 * @Version V0.0.1
 **/
@Service
@Transactional(readOnly = true)
public class DataModelService {

    public static final String GROUP_NAME = "SCHEMA_VERSION";
    private static final String KEY_SCHEMA_DTO = "SchemaDto";
    private static final String KEY_TABLE_DTO = "'TableDto_'+#p0+'_'+#p1";
    private static final String KEY_COLUMN_DTO = "'ColumnDto_'+#p0+'_'+#p1";
    private static final String KEY_REFERENCE_DTO = "'ReferenceDto_'+#p0+'_'+#p1";
    private static final String KEY_CONSTRAINT_DTO = "'ConstraintDto_'+#p0+'_'+#p1";
    private static final String KEY_FORMULA_DTO = "'FormulaDto'+#p0+'_'+#p1";

    private static final String KEY_REFERENCE = "'REFERENCE_'+#p0+'_'+#p1";


    @Autowired
    private DataOperatorFactory factory;

    @Autowired
    private UIService uiService;

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
        queryParamDefinition.addOrder(new FieldOrder((String) null, "order_num", true, 0));
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
        queryParamDefinition.addOrder(new FieldOrder((String) null, "field_index", true, 0));
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, ColumnDto.class);
    }

    public List<ReferenceDto> findSchemaReferences(String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ReferenceDto.class);
        queryParamDefinition.appendCriteria()
                .andEqualTo("version_code", version);
        queryParamDefinition.addOrder(new FieldOrder(ReferenceDto.class, "xh", true, 1));
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
        queryParamDefinition.addOrder(new FieldOrder((String) null, "order_num", true, 0));
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, ConstraintDto.class);
    }


    /**
     * 取得所有的方案信息
     *
     * @param isOnlyEnabled
     * @return
     */
    public List<SchemaDto> findAllSchemaDto(boolean isOnlyEnabled) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(SchemaDto.class);
        Criteria criteria = queryParamDefinition.appendCriteria().andEqualTo("version_code",
                SessionUtils.getLoginVersion());
        if (isOnlyEnabled) {
            criteria.andEqualTo("enabled", 1);
        }
        queryParamDefinition.addOrder(new FieldOrder(SchemaDto.class, "schema_id", true, 1));
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, SchemaDto.class);
    }

    /**
     * 查询一个方案的定义
     *
     * @param schemaId
     * @param version
     * @return
     */
    public SchemaDto findSchemaDto(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(SchemaDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        queryParamDefinition.appendCriteria().andEqualTo("enabled", 1);
        return factory.getDefaultDataOperator().selectOne(queryParamDefinition, SchemaDto.class);
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
        List<String> schemaTableNames = findSchemaTableNames(schemaId, version);
        if (!schemaTableNames.isEmpty()) {
            definition.getSingleCriteria().andNotIn("table_name", findSchemaTables(schemaId, version));
        }
        definition.getSingleCriteria()
                .andCondition("TABLE_SCHEMA=(select database())");


        List<Map<String, Object>> lstData = factory.getDataOperatorBySchema(schemaId, version).select(definition);
        if (lstData == null || lstData.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> lst = new ArrayList<>();
        for (Map<String, Object> map : lstData) {
            lst.add(map.get("table_name").toString());
        }
        return lst;
    }

    private List<String> findSchemaTableNames(long schemaId, String version) {
        Schema schema = SchemaHolder.getInstance().getSchema(schemaId, version);
        List<TableDto> tableDtos = schema.getTableDtos();
        if (tableDtos == null || tableDtos.isEmpty()) {
            return new ArrayList();
        }
        int length = tableDtos.size();
        List<String> table = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            table.add(tableDtos.get(i).getTableName());
        }
        return table;

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
    @Transactional(readOnly = false)
    public String saveSchema(Schema schema) {

        String sErr = validateSchema(schema);
        if (!CommonUtils.isEmpty(sErr)) {
            return sErr;
        }
        if (schema.getSchemaDto().getSchemaId() == 1) {
            //如果是管理员才可以修改
            if (!isManager()) {
                return "系统保留方案不允许修改";
            }
        }
        deleteSchema(schema.getSchemaDto().getSchemaId(), schema.getSchemaDto().getVersionCode());
        //更新新增加的ID
        updateIds(schema);
        saveData(schema);
        return "";
    }

    private boolean isManager() {
        //TODO 重点检查权限
        return true;
    }

    private void saveData(Schema schema) {

        InsertParamDefinition insertParamDefinition = new InsertParamDefinition();
        insertParamDefinition.setNeedConvertToUnderLine(true);
        insertParamDefinition.setObject(schema.getSchemaDto());
        factory.getDefaultDataOperator().insert(insertParamDefinition);

        List<FormulaDto> formulas = schema.getFormulaDtos();
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
        if (CommonUtils.isEmpty(schema.getSchemaDto().getSchemaName())) {
            return "方案名称不可以为空";
        }
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
        if (schema.getLstConstraint() != null) {
            schema.getLstConstraint().forEach((constraint -> {
                constraint.getConstraintDto().setSchemaId(schema.getSchemaDto().getSchemaId());
                constraint.getConstraintDto().setVersionCode(
                        schema.getSchemaDto().getVersionCode());
                if (constraint.getConstraintDto().getId() < 0) {
                    constraint.getConstraintDto().setId(IdGenerator.getNextId(TableColumnRelation.class.getName()));
                }
            }));
        }
        if (schema.getFormulaDtos() != null) {
            schema.getFormulaDtos().forEach((formula -> {
                formula.setSchemaId(schema.getSchemaDto().getSchemaId());
                formula.setVersionCode(
                        schema.getSchemaDto().getVersionCode());
                if (formula.getFormulaId() < 0) {
                    formula.setFormulaId(IdGenerator.getNextId(Formula.class.getName()));
                }
            }));
        }
        if (!lstChangedColumn.isEmpty()) {
            //更新约束
            if (schema.getLstConstraint() != null) {
                for (Constraint constraint : schema.getLstConstraint()) {
                    constraint.columnIdChanged(lstChangedColumn);
                }
            }
            //更新关系
            List<TableColumnRelation> lstRelation = schema.getLstRelation();
            if (lstRelation != null && !lstRelation.isEmpty()) {
                lstRelation.forEach((relation -> {
                    relation.columnIdChanged(lstChangedColumn);
                }));
            }
            //更新公式
            List<FormulaDto> formulas = schema.getFormulaDtos();
            if (formulas != null && !formulas.isEmpty()) {
                formulas.forEach(formulaDto -> {
                    new Formula(formulaDto).columnIdChanged(lstChangedColumn);
                });

            }

        }
    }

    /**
     * 用于公开 使用的删除
     *
     * @param schemaId
     * @param version
     */
    @Transactional(readOnly = false)
    public void deleteSchemaForPublic(Long schemaId, String version) {
        if (schemaId == 1 || schemaId == 2) {
            throw new InvalidParamException("内置方案不可以删除");
        }
        deleteSchema(schemaId, version);
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

    /**
     * 取得更新的数据 ,不保存到数据库中
     *
     * @param tableId
     * @param version
     * @return
     */
    public List<ColumnDto> getSyncTableCols(long tableId, String version) {
        return syncTableColumnCommon(tableId, version, false);
    }

    /**
     * 同步更新表列信息,保存到数据库中
     *
     * @param tableId
     * @param version
     */
    public List<ColumnDto> syncTableColumn(long tableId, String version) {
        return syncTableColumnCommon(tableId, version, true);
    }

    /**
     * 同步更新表列信息
     *
     * @param tableId
     * @param version
     */
    private List<ColumnDto> syncTableColumnCommon(long tableId, String version, boolean isNeedCommit) {
        TableInfo table = SchemaHolder.getTable(tableId, version);
        if (table == null) {
            return null;
        }
        List<ColumnDto> columnDtos = findTableFieldAsColumnDto(table.getTableDto().getTableName());
        if (columnDtos == null || columnDtos.isEmpty()) {
            return null;
        }
        Map<String, ColumnDto> mapNewCol = makeMap(columnDtos, table.getTableDto().getTableId(),
                table.getTableDto().getSchemaId());
        List<ColumnDto> lstResult = new ArrayList<>();
        List<ColumnDto> lstToDelete = new ArrayList<>();
        List<ColumnDto> lstToAdd = new ArrayList<>();
        List<ColumnDto[]> lstToUpdate = new ArrayList<>();
        List<ColumnDto> lstColOld = table.getColumnDtos(table.getTableDto().getSchemaId(), table.getTableDto().getVersionCode());
        for (ColumnDto dto : lstColOld) {
            //需要删除的
            if (!mapNewCol.containsKey(dto.getFieldName().toLowerCase())) {
                lstToDelete.add(dto);
                continue;
            }
            lstResult.add(dto);
            if (!compareColumnInfo(dto, mapNewCol.get(dto.getFieldName()))) {
                lstToUpdate.add(new ColumnDto[]{dto, mapNewCol.get(dto.getFieldName())});
            }
            mapNewCol.remove(dto.getFieldName());
        }
        if (!mapNewCol.isEmpty()) {
            lstToAdd.addAll(mapNewCol.values());
            lstResult.addAll(mapNewCol.values());
        }
        if (isNeedCommit) {
            deleteCols(lstToDelete);
            addCols(lstToAdd, (short) lstColOld.get(lstColOld.size() - 1).getFieldIndex(), table.getTableDto());
        }
        updateCol(lstToUpdate, isNeedCommit);
        return lstResult;
    }

    private void updateCol(List<ColumnDto[]> lstColAll, boolean isNeedCommit) {
        if (lstColAll == null || lstColAll.isEmpty()) {
            return;
        }
        List<ColumnDto> lstCol = new ArrayList<>();
        lstColAll.forEach((ColumnDto[] dtos) -> {
            copyDtoValue(dtos[0], dtos[1]);
            lstCol.add(dtos[0]);
        });
        if (isNeedCommit) {
            UpdateParamDefinition definition = new UpdateParamDefinition();
            definition.setIdField("column_id,version_code");
            definition.setObjects(lstCol, true);

            factory.getDefaultDataOperator().update(definition);
        }
    }

    private void copyDtoValue(ColumnDto dto1, ColumnDto dto2) {
        dto1.setDefaultValue(dto2.getDefaultValue());
        dto1.setFieldType(dto2.getFieldType());
        dto1.setLength(dto2.getLength());
        dto1.setNullable(dto2.getNullable());
        dto1.setPrecisionNum(dto2.getPrecisionNum());
    }

    private void addCols(List<ColumnDto> lstCol, short index, TableDto tableDto) {
        if (lstCol.isEmpty()) {
            return;
        }
        for (ColumnDto dto : lstCol) {
            dto.setFieldIndex(++index);
            dto.setSchemaId(tableDto.getSchemaId());
            dto.setColumnId(IdGenerator.getNextId(ColumnDto.class.getName()));
            dto.setVersionCode(tableDto.getVersionCode());
            dto.setTableId(tableDto.getTableId());
        }
        InsertParamDefinition definition = new InsertParamDefinition();
        definition.setObjects(lstCol);
        factory.getDefaultDataOperator().insert(definition);
    }

    private void deleteCols(List<ColumnDto> lstCol) {
        if (lstCol.isEmpty()) {
            return;
        }
        String version = lstCol.get(0).getVersionCode();
        List<Object> lstId = new ArrayList<>();
        lstCol.forEach((ColumnDto dto) -> {
            lstId.add(dto.getColumnId());
        });
        DeleteParamDefinition deleteParamDefinition = new DeleteParamDefinition();
        deleteParamDefinition.setTableDto(ColumnDto.class);
        deleteParamDefinition.setIdField("column_id");
        deleteParamDefinition.setIds(lstId);
        deleteParamDefinition.getCriteria().andEqualTo("version_code", version);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);
        // 删除关联的公式约束等,控件等.
        //删除控件
        uiService.deleteColComponent(lstId, version);
        //删除公式
        deleteColFormula(lstId, version);
        //删除约束 TODO 需要分解后才可以删除


    }

    private void deleteColFormula(List<Object> lstColId, String version) {
        DeleteParamDefinition definition = new DeleteParamDefinition();
        definition.setTableDto(FormulaDto.class);
        definition.getCriteria().andIn("column_id", lstColId).andEqualTo("version_code", version);
        factory.getDefaultDataOperator().delete(definition);
    }

    /**
     * 比较二个列定义是否一样
     *
     * @param dto1
     * @param dto2
     * @return
     */
    private boolean compareColumnInfo(ColumnDto dto1, ColumnDto dto2) {
        //比较类型
        if (!dto1.getFieldType().equals(dto2.getFieldType())) {
            return false;
        }
        //比较长度
        if ((dto1.getLength() == null && dto2.getLength() != null)
                || (dto1.getLength() != null && dto2.getLength() == null)) {
            return false;
        }
        if (dto1.getLength() != null && !dto1.getLength().equals(dto2.getLength())) {
            return false;
        }
        //比较精度
        if ((dto1.getPrecisionNum() != null && dto2.getPrecisionNum() == null)
                || (dto1.getPrecisionNum() == null && dto2.getPrecisionNum() != null)) {
            return false;
        }
        //比较默认
        if (!(dto1.getDefaultValue() + "").equals(dto2.getDefaultValue())) {
            return false;
        }
        return true;

    }

    private Map<String, ColumnDto> makeMap(List<ColumnDto> lstDto, Long tableId, long schemaId) {
        Map<String, ColumnDto> map = new HashMap<>();
        for (ColumnDto dto : lstDto) {
            dto.setTableId(tableId);
            dto.setSchemaId(schemaId);
            map.put(dto.getFieldName().toLowerCase(), dto);
        }
        return map;
    }


    /**
     * 增加方案
     *
     * @param schemaName
     * @return
     */
    @Transactional(readOnly = false)
    public long addSchema(String schemaName) {
        if (CommonUtils.isEmpty(schemaName)) {
            throw new InvalidParamException("方案名称不可以为空!");
        }
        SchemaDto dto = new SchemaDto();
        dto.setEnabled(1);
        dto.setSchemaName(schemaName);
        dto.setSchemaId(IdGenerator.getNextId(SchemaDto.class.getName()));
        dto.setVersionCode(SessionUtils.getLoginVersion());
        InsertParamDefinition definition = new InsertParamDefinition();
        definition.setObject(dto);

        factory.getDefaultDataOperator().insert(definition);
        return dto.getSchemaId();
    }

    /**
     * 保存引用信息
     *
     * @param lstDto
     */
    @Transactional(readOnly = false)
    public void saveReference(List<ReferenceDto> lstDto) {
        String version = SessionUtils.getLoginVersion();
        deleteReference(version);
        if (lstDto == null || lstDto.isEmpty()) {
            return;
        }
        int index = 1;
        for (ReferenceDto dto : lstDto) {
            dto.setXh(index++);
            dto.setVersionCode(version);
            if (dto.getRefId() == null || dto.getRefId() < 1) {
                dto.setRefId(IdGenerator.getNextId(ReferenceDto.class.getName()));
            }
        }
        InsertParamDefinition definition = new InsertParamDefinition();
        definition.setObjects(lstDto);
        factory.getDefaultDataOperator().insert(definition);
        SchemaHolder.getInstance().refresh();
    }

    private void deleteReference(String version) {
        DeleteParamDefinition deleteParamDefinition = new DeleteParamDefinition();
        deleteParamDefinition.setTableDto(ReferenceDto.class);
        deleteParamDefinition.getCriteria().andEqualTo("version_code", version);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);
    }


}
