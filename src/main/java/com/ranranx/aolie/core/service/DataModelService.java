package com.ranranx.aolie.core.service;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;
import com.ranranx.aolie.core.datameta.datamodel.expression.FilterExpression;
import com.ranranx.aolie.core.datameta.datamodel.formula.FormulaParse;
import com.ranranx.aolie.core.datameta.dto.*;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.*;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.fixrow.dto.FixMain;
import com.ranranx.aolie.core.fixrow.service.FixRowService;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.interfaces.IReferenceDataFilter;
import com.ranranx.aolie.core.tools.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/8/31 16:46
 **/
@Service
@Transactional(readOnly = true)
public class DataModelService {

    /**
     * 需要排除的表前缀
     */
    public static final String[] EXCLUDE_TABLE_PREFIX = new String[]{"flw", "act"};

    public static final String KEY_REFERENCE_DATA_PREFIX = "'REFERENCE_DATA_'";
    public static final String GROUP_NAME = "SCHEMA_VERSION";
    public static final String GROUP_TABLEINFO = "TABLE_INFO";
    private static final String KEY_SCHEMA_DTO = "SchemaDto";
    private static final String KEY_TABLE_DTO = "'TableDto_'+#p0+'_'+#p1";
    private static final String KEY_COLUMN_DTO = "'ColumnDto_'+#p0+'_'+#p1";
    private static final String KEY_REFERENCE_DTO = "'ReferenceDto_'+#p0";
    private static final String KEY_CONSTRAINT_DTO = "'ConstraintDto_'+#p0+'_'+#p1";
    private static final String KEY_FORMULA_DTO = "'FormulaDto'+#p0+'_'+#p1";

    private static final String KEY_REFERENCE_DATA = KEY_REFERENCE_DATA_PREFIX + "+#p0+'_'+#p1";


    @Autowired
    private DataOperatorFactory factory;

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private UIService uiService;

    @Autowired
    private FixRowService fixRowService;

    @Caching(evict = {@CacheEvict(value = GROUP_NAME, key = KEY_TABLE_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_COLUMN_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_CONSTRAINT_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_FORMULA_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_REFERENCE_DATA)})
    public void clearSchemaCache(long schemaId, String version) {
        FormulaParse.clearCache();
    }

    @Caching(evict = {
            @CacheEvict(value = GROUP_NAME, key = KEY_REFERENCE_DATA)})
    public void clearReferenceData(long refId, String versionCode) {

    }

    @Caching(evict = {
            @CacheEvict(value = GROUP_NAME, key = KEY_REFERENCE_DTO)})
    public void clearSchemaCache2(String version) {

    }

    @Cacheable(value = GROUP_NAME, key = (KEY_TABLE_DTO))
    public List<TableDto> findSchemaTables(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(TableDto.class);
        queryParamDefinition.appendCriteria().andEqualTo(null,
                "schema_id", schemaId);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, TableDto.class);
    }

    @Cacheable(value = GROUP_NAME, key = (KEY_FORMULA_DTO))
    public List<FormulaDto> findSchemaFormula(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(FormulaDto.class);
        queryParamDefinition.appendCriteria()
                .andEqualTo(null, "schema_id", schemaId)
        ;
        queryParamDefinition.addOrder(new FieldOrder((String) null, "order_num", true, 0));
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, FormulaDto.class);
    }

    @Cacheable(value = GROUP_NAME,
            key = KEY_COLUMN_DTO)
    public List<ColumnDto> findSchemaColumns(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ColumnDto.class);
        queryParamDefinition.appendCriteria()
                .andEqualTo(null, "schema_id", schemaId)
        ;
        queryParamDefinition.addOrder(new FieldOrder((String) null, "field_index", true, 0));
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, ColumnDto.class);
    }

    @Cacheable(value = GROUP_NAME,
            key = KEY_REFERENCE_DTO)
    public List<ReferenceDto> findAllReferences(String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ReferenceDto.class);
        queryParamDefinition.addOrder(new FieldOrder(ReferenceDto.class, "xh", true, 1));
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, ReferenceDto.class);
    }

    @Cacheable(value = GROUP_NAME,
            key = KEY_CONSTRAINT_DTO)
    public List<ConstraintDto> findSchemaConstraints(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ConstraintDto.class);
        queryParamDefinition.appendCriteria()
                .andEqualTo(null, "schema_id", schemaId)
        ;
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
        Criteria criteria = queryParamDefinition.appendCriteria();
        if (isOnlyEnabled) {
            criteria.andEqualTo(null, "enabled", 1);
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
        queryParamDefinition.appendCriteria().andEqualTo(null, "schema_id", schemaId)
        ;
        queryParamDefinition.appendCriteria().andEqualTo(null, "enabled", 1);
        return factory.getDefaultDataOperator().selectOne(queryParamDefinition, SchemaDto.class);
    }

    private void clearPrefixCache(String prefix) {
        Cache cache = cacheManager.getCache(GROUP_NAME);
        Object nativeCache = cache.getNativeCache();
        if (nativeCache instanceof Map) {
            Map map = (Map) nativeCache;
            List lstKey = new ArrayList();
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                if (obj.toString().startsWith(prefix)) {
                    lstKey.add(obj);
                }
            }
            for (Object obj : lstKey) {
                cache.evict(obj);
            }
        }
    }

    /**
     * 查询引用数据
     *
     * @param referenceId
     * @param version
     * @return
     */
    @Cacheable(value = (GROUP_NAME),
            key = KEY_REFERENCE_DATA)
    public List<ReferenceData> findReferenceData(long referenceId, String version) {
        return findReferenceDataByFilter(referenceId, version, null);
    }

    private List<ReferenceData> findReferenceDataByFilter(long referenceId, String version, Criteria extFilter) {
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

        if (reference.getReferenceDto().getCodeField() != null) {
            field = new Field();
            field.setTableName(tableName);
            field.setFieldName(reference.getReferenceDto().getCodeField() + " as code");
            field.setOrderType(Constants.OrderType.ASC);
            lstField.add(field);
        }
        if (reference.getReferenceDto().getParentField() != null) {
            field = new Field();
            field.setTableName(tableName);
            field.setFieldName(reference.getReferenceDto().getParentField() + " as parent_id");
            lstField.add(field);
        }

        field = new Field();
        field.setTableName(tableName);
        field.setFieldName(reference.getReferenceDto().getNameField() + " as name");
        lstField.add(field);
        queryParamDefinition.setFields(lstField);
        Criteria criteria = queryParamDefinition.appendCriteria();
        if (extFilter != null) {
            queryParamDefinition.appendCriteria(extFilter);
        }
        FieldOrder order = new FieldOrder();
        order.setTableName(tableName);
        order.setAsc(true);
        order.setField(reference.getReferenceDto().getCodeField());
        queryParamDefinition.addOrder(order);
        if (CommonUtils.isNotEmpty(reference.getReferenceDto().getCommonType())) {
            criteria
                    .andEqualTo(null, "common_type", reference.getReferenceDto().getCommonType());
        }
        return factory.getDefaultDataOperator().select(queryParamDefinition, ReferenceData.class);
    }

    /**
     * 查询引用数据
     *
     * @param referenceId
     * @param colId
     * @param version
     * @param filterValue
     * @return
     */
    public List<ReferenceData> findColumnReferenceData(long referenceId, long colId,
                                                       String version, Map<String, Object> filterValue) {
        Column column = SchemaHolder.getColumn(colId, version);
        if (column == null) {
            throw new NotExistException("指定字段不存在:" + colId);
        }
        String filter = column.getColumnDto().getRefFilter();
        if (CommonUtils.isEmpty(filter)) {
            return findReferenceData(referenceId, version);
        } else {
            FilterExpression filterExpression = FilterExpression.getInstance(filter, version);
            if (filterExpression.isServiceFilter()) {
                //执行服务过滤
                return findColumnReferenceDataByService(referenceId, colId, version,
                        filterExpression.getServiceName(), filterValue);
            } else {
                return findReferenceDataByFilter(referenceId, version,
                        filterExpression.getSqlCriteria(filterValue,
                                SessionUtils.getAllParams(),
                                null, Arrays.asList(column.getColumnDto().getTableId())));
            }

        }
    }

    private List<ReferenceData> findColumnReferenceDataByService(long referenceId, long colId,
                                                                 String version, String serviceName,
                                                                 Map<String, Object> filterValue) {
        Object service = ApplicationService.getService(serviceName);
        if (service == null) {
            throw new NotExistException("指定的服务名不存在:" + serviceName);
        }
        if (!(service instanceof IReferenceDataFilter)) {
            throw new InvalidParamException("指定的服务没有继承指定接口:" + serviceName);
        }
        IReferenceDataFilter filter = (IReferenceDataFilter) service;
        List<ReferenceData> referenceData = filter.beforeQuery(referenceId, colId, filterValue);
        //如果不为空则说明已执行查询
        if (referenceData != null) {
            return referenceData;
        }
        Criteria extFilter = filter.getExtFilter(referenceId, colId, filterValue);
        referenceData = findReferenceDataByFilter(referenceId, version, extFilter);

        return filter.afterQuery(referenceId, colId, filter, referenceData);

    }

    /**
     * 查找没有被引用的表信息,这里只针对MYSql做的查询
     *
     * @param schemaId
     * @param version
     * @return
     */
    public List<String> findDefaultDBTablesNotInSchema(Long schemaId, String version) {
        if (factory.getDataOperatorBySchema(schemaId, version).getDbType().equals(DmConstants.DbType.ORACLE)) {
            return findDefaultDBTablesNotInSchemaForOracle(schemaId, version);
        }
        return findDefaultDBTablesNotInSchemaMySql(schemaId, version);
    }

    private List<String> findDefaultDBTablesNotInSchemaMySql(Long schemaId, String version) {
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
            definition.getSingleCriteria().andNotIn("information_schema.tables", "table_name", schemaTableNames);
        }
        Criteria criteria = definition.getSingleCriteria()
                .andCustomCondition(null, null, "TABLE_SCHEMA=(select database())");
        if (EXCLUDE_TABLE_PREFIX != null) {
            for (int i = 0; i < EXCLUDE_TABLE_PREFIX.length; i++) {
                criteria.andNotStartWith("information_schema.tables", "table_name", EXCLUDE_TABLE_PREFIX[i]);
            }
        }

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

    /**
     * 查找没有被引用的表信息,这里只针对MYSql做的查询
     *
     * @param schemaId
     * @param version
     * @return
     */
    private List<String> findDefaultDBTablesNotInSchemaForOracle(Long schemaId, String version) {
        QueryParamDefinition definition = new QueryParamDefinition();
        definition.setTableNames("user_tables");
        List<Field> lstField = new ArrayList<>();
        Field field = new Field();
        field.setFieldName("table_name");
        field.setTableName("user_tables");
        lstField.add(field);
        definition.setFields(lstField);
        List<String> schemaTableNames = findSchemaTableNames(schemaId, version);
        if (!schemaTableNames.isEmpty()) {
            for (int i = 0; i < schemaTableNames.size(); i++) {
                schemaTableNames.set(i, schemaTableNames.get(i).toUpperCase());
            }
            definition.getSingleCriteria().andNotIn("user_tables", "table_name", schemaTableNames);
        }
        Criteria criteria = definition.getSingleCriteria();
        if (EXCLUDE_TABLE_PREFIX != null) {
            for (int i = 0; i < EXCLUDE_TABLE_PREFIX.length; i++) {
                criteria.andNotStartWith("user_tables", "table_name", EXCLUDE_TABLE_PREFIX[i].toUpperCase());
            }
        }


        List<Map<String, Object>> lstData = factory.getDataOperatorBySchema(schemaId, version).select(definition);
        if (lstData == null || lstData.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> lst = new ArrayList<>();
        for (Map<String, Object> map : lstData) {
            lst.add(map.get("table_name").toString().toLowerCase());
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
     * <p>
     * TODO 这里只提供了MySql的生成方式
     *
     * @param tableName
     * @return
     */
    public List<ColumnDto> findTableFieldAsColumnDto(String tableName) {

        if(factory.getDefaultDataOperator().getDbType().equals(DmConstants.DbType.ORACLE)){
            return findTableFieldAsColumnDtoOracle(tableName);
        }
        return findTableFieldAsColumnDtoMySql(tableName);
    }
    /**
     * 查询一张表的字段信息,并生成DTO
     * <p>
     * TODO 这里只提供了MySql的生成方式
     *
     * @param tableName
     * @return
     */
    private List<ColumnDto> findTableFieldAsColumnDtoMySql(String tableName) {

        QueryParamDefinition definition = new QueryParamDefinition();
        definition.setTableNames("information_schema.COLUMNS");
        definition.appendCriteria().andEqualTo("information_schema.COLUMNS", "table_name", tableName)
                .andCustomCondition(null, null, "TABLE_SCHEMA=(select database())");
        List<Map<String, Object>> lstResult = factory.getDefaultDataOperator().select(definition);
        List<ColumnDto> lstDto = new ArrayList<>();

        if (lstResult != null && !lstResult.isEmpty()) {
            for (Map<String, Object> map : lstResult) {
                lstDto.add(createColumnDto(map));
            }
        }
        return lstDto;
    }
    /**
     * 查询一张表的字段信息,并生成DTO
     * <p>
     * TODO 这里只提供了MySql的生成方式
     *
     * @param tableName
     * @return
     */
    private List<ColumnDto> findTableFieldAsColumnDtoOracle(String tableName) {

        QueryParamDefinition definition = new QueryParamDefinition();
        definition.setTableNames("cols");
        definition.appendCriteria().andEqualTo("cols", "table_name", tableName.toUpperCase());
        List<Map<String, Object>> lstResult = factory.getDefaultDataOperator().select(definition);
        List<ColumnDto> lstDto = new ArrayList<>();
        if (lstResult != null && !lstResult.isEmpty()) {
            for (Map<String, Object> map : lstResult) {
                lstDto.add(createColumnDtoFromOracle(map));
            }
        }
        return lstDto;
    }

    private ColumnDto createColumnDto(Map<String, Object> map) {
        ColumnDto dto = new ColumnDto();
        dto.setFieldName(CommonUtils.getStringField(map, "COLUMN_NAME").toLowerCase());
        dto.setFieldType(factory.getDefaultDataOperator().convertColType(CommonUtils.getStringField(map, "DATA_TYPE")));
        dto.setLength(CommonUtils.getIntegerField(map, "CHARACTER_MAXIMUM_LENGTH"));
        dto.setNullable("YES".equals(CommonUtils.getStringField(map, "IS_NULLABLE")) ? new Byte((byte) 1) : new Byte((byte) 0));
        dto.setDefaultValue(CommonUtils.getStringField(map, "COLUMN_DEFAULT"));
        dto.setPrecisionNum(CommonUtils.getIntegerField(map, "NUMERIC_SCALE"));
        dto.setTitle(dto.getFieldName());
        return dto;
    }
    private ColumnDto createColumnDtoFromOracle(Map<String, Object> map) {
        ColumnDto dto = new ColumnDto();
        dto.setFieldName(CommonUtils.getStringField(map, "column_name").toLowerCase());
        dto.setFieldType(factory.getDefaultDataOperator().convertColType(CommonUtils.getStringField(map, "data_type")));
        dto.setLength(CommonUtils.getIntegerField(map, "data_length"));
        dto.setNullable("Y".equals(CommonUtils.getStringField(map, "nullable")) ? new Byte((byte) 1) : new Byte((byte) 0));
        dto.setPrecisionNum(CommonUtils.getIntegerField(map, "data_scale"));
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
        updateFixRow(schema.getSchemaDto().getSchemaId(), schema.getSchemaDto().getVersionCode());

        return "";
    }

    private void updateFixRow(Long schemaId, String version) {
        fixRowService.syncFixSet(schemaId, version);
    }

    /**
     * 查询所有固定行设置
     *
     * @param version
     * @return
     */
    public Map<Long, Long> findAllFixMainRelation(String version) {
        List<FixMain> lstMain = fixRowService.findFixMain(version).getData();
        Map<Long, Long> result = new HashMap<>();
        if (lstMain == null || lstMain.isEmpty()) {
            return result;
        }
        for (FixMain fixMain : lstMain) {
            result.put(fixMain.getTableId(), fixMain.getFixId());
        }
        return result;
    }

    private boolean isManager() {
        //TODO 重点检查权限
        return true;
    }

    public String[] getAllVersionCode() {
        List<VersionDto> lstDto = getVersions();
        if (lstDto == null || lstDto.isEmpty()) {
            return null;
        }
        String[] result = new String[lstDto.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = lstDto.get(i).getVersionCode();
        }
        return result;
    }

    public List<VersionDto> getVersions() {
        QueryParamDefinition definition = new QueryParamDefinition();
        definition.setTableDtos(VersionDto.class);
        return factory.getDefaultDataOperator().select(definition, VersionDto.class);
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
        if (schema.getSchemaDto().getSchemaId().equals(Constants.DEFAULT_REFERENCE_SCHEMA)) {
            List<ReferenceDto> lstReference = schema.getReferenceDto();
            if (lstReference != null && !lstReference.isEmpty()) {
                insertParamDefinition.setTableDto(ReferenceDto.class);
                insertParamDefinition.setObjects(lstReference);
                factory.getDefaultDataOperator().insert(insertParamDefinition);
            }
            //清除缓存
            clearPrefixCache(KEY_REFERENCE_DATA_PREFIX.replace("'", ""));
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
        Map<Long, Long> mapColChangedId = new HashMap<>();
        Map<Long, Long> mapTableChangeId = new HashMap<>();
        if (schema.getLstTable() != null) {
            for (TableInfo table : schema.getLstTable()) {
                Map<Long, Long> changedIds = table.updateColId(mapTableChangeId);
                if (changedIds != null) {
                    mapColChangedId.putAll(changedIds);
                }
            }
            if (!mapColChangedId.isEmpty()) {
                for (TableInfo table : schema.getLstTable()) {
                    table.columnIdChanged(mapColChangedId);
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
        if (!mapColChangedId.isEmpty()) {
            //更新约束
            if (schema.getLstConstraint() != null) {
                for (Constraint constraint : schema.getLstConstraint()) {
                    constraint.columnIdChanged(mapColChangedId);
                }
            }
            //更新关系
            List<TableColumnRelation> lstRelation = schema.getLstRelation();
            if (lstRelation != null && !lstRelation.isEmpty()) {
                lstRelation.forEach((relation -> {
                    relation.columnIdChanged(mapColChangedId);
                }));
            }
            //更新公式
            List<FormulaDto> formulas = schema.getFormulaDtos();
            if (formulas != null && !formulas.isEmpty()) {
                formulas.forEach(formulaDto -> {
                    new Formula(formulaDto).columnIdChanged(mapColChangedId);
                });
            }
        }
        if (!mapTableChangeId.isEmpty() && schema.getSchemaDto().getSchemaId().equals(Constants.DEFAULT_REFERENCE_SCHEMA)) {
            //更新引用
            List<ReferenceDto> lstReference = schema.getReferenceDto();
            if (lstReference == null || lstReference.isEmpty()) {
                return;
            }
            for (ReferenceDto dto : lstReference) {
                if (mapTableChangeId.containsKey(dto.getTableId())) {
                    dto.setTableId(mapTableChangeId.get(dto.getTableId()));
                }
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
        deleteParamDefinition.getCriteria().andEqualTo(null, "schema_id", schemaId)
        ;
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

        //删除引用信息
        if (SchemaTools.isReferenceSchema(schemaId)) {
            deleteParamDefinition = new DeleteParamDefinition();
            deleteParamDefinition.setTableDto(ReferenceDto.class);
            factory.getDefaultDataOperator().delete(deleteParamDefinition);
        }
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
        definition.appendCriteria()
                .andEqualTo(null, "schema_id", schemaId)
        ;
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
        definition.getCriteria().andIn(CommonUtils.getTableName(FormulaDto.class), "column_id", lstColId);
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
        factory.getDefaultDataOperator().delete(deleteParamDefinition);
    }


}
