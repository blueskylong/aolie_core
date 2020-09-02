package com.ranranx.aolie.service;

import com.ranranx.aolie.common.CommonUtils;
import com.ranranx.aolie.datameta.datamodel.BlockViewer;
import com.ranranx.aolie.datameta.datamodel.Component;
import com.ranranx.aolie.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.datameta.dto.*;
import com.ranranx.aolie.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.ds.definition.FieldOrder;
import com.ranranx.aolie.ds.definition.QueryParamDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private static final String KEY_TABLE_DTO = "TableDto+'_'+#p0+'_'+#p1";
    private static final String KEY_COLUMN_DTO = "ColumnDto+'_'+#p0+'_'+#p1";
    private static final String KEY_REFERENTCE_DTO = "ReferenceDto+'_'+#p0+'_'+#p1";
    private static final String KEY_CONSTRAINT_DTO = "ConstraintDto+'_'+#p0+'_'+#p1";
    private static final String KEY_FORMULA_DTO = "ConstraintDto+'_'+#p0+'_'+#p1";
    private static final String KEY_VIEWER = "VIEWER+'_'+#p0+'_'+#p1";


    @Autowired
    private DataOperatorFactory factory;

    @Caching(evict = {@CacheEvict(value = GROUP_NAME, key = KEY_TABLE_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_COLUMN_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_REFERENTCE_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_CONSTRAINT_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_FORMULA_DTO),
            @CacheEvict(value = GROUP_NAME, key = KEY_REFERENTCE_DTO)})
    public void clearSchemaCache(long schemaId, String version) {

    }

    // @Cacheable(value = GROUP_NAME, key = (KEY_TABLE_DTO))
    public List<TableDto> findSchemaTables(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(TableDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, TableDto.class);
    }

    // @Cacheable(value = GROUP_NAME, key = (KEY_FORMULA_DTO))
    public List<FormulaDto> findSchemaFormula(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(FormulaDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, FormulaDto.class);
    }

    //    @Cacheable(value = (GROUP_NAME),
//            key = KEY_COLUMN_DTO)
    public List<ColumnDto> findSchemaColumns(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ColumnDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, ColumnDto.class);
    }

    //    @Cacheable(value = (GROUP_NAME),
//            key = KEY_REFERENTCE_DTO)
    public List<ReferenceDto> findSchemaReferences(Long schemaId, String version) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(ReferenceDto.class);
        queryParamDefinition.appendCriteria()
                .andEqualTo("version_code", version);
        return factory.getDefaultDataOperator()
                .select(queryParamDefinition, ReferenceDto.class);
    }

    //    @Cacheable(value = (GROUP_NAME),
//            key = KEY_CONSTRAINT_DTO)
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

    //    @Cacheable(value = (GROUP_NAME),
//            key = KEY_VIEWER)
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
}
