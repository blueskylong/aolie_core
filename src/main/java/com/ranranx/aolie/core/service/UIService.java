package com.ranranx.aolie.core.service;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;
import com.ranranx.aolie.core.datameta.dto.BlockViewDto;
import com.ranranx.aolie.core.datameta.dto.ComponentDto;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.DeleteParamDefinition;
import com.ranranx.aolie.core.ds.definition.FieldOrder;
import com.ranranx.aolie.core.ds.definition.InsertParamDefinition;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.exceptions.NotExistException;
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
 * @Date 2020/10/7 11:16
 * @Version V0.0.1
 **/
@Service
@Transactional(readOnly = true)
public class UIService {
    public static final String GROUP_NAME = "SCHEMA_VERSION";
    private static final String KEY_VIEWER = "'VIEWER_'+#p0+'_'+#p1";
    @Autowired
    private DataOperatorFactory factory;

    /**
     * 查询表及其字段,用于树状显示
     *
     * @param tableIds
     * @return
     */
    public List<Map<String, Object>> findTablesAndFields(long[] tableIds) {
        if (tableIds == null || tableIds.length == 0) {
            return null;
        }
        List<Map<String, Object>> result = new ArrayList<>();
        String version = SessionUtils.getLoginVersion();
        String id, code, name, parent;
        Map<String, Object> mapResult;
        for (long tableId : tableIds) {
            TableInfo tableInfo = SchemaHolder.getTable(tableId, version);
            if (tableInfo == null) {
                throw new NotExistException("数据表信息不存在:tableId=" + tableId);
            }
            mapResult = new HashMap<>();
            String tableKey = "T" + tableInfo.getTableDto().getTableId();
            mapResult.put(Constants.TreeNodeNames.ID, tableKey);
            mapResult.put(Constants.TreeNodeNames.CODE, tableInfo.getTableDto().getTableId());
            mapResult.put(Constants.TreeNodeNames.NAME, "[" + tableInfo.getTableDto().getTableName() + "]"
                    + tableInfo.getTableDto().getTitle());
            mapResult.put(Constants.TreeNodeNames.IS_LEAF, 0);
            result.add(mapResult);
            addColumnInfo(result, tableInfo.getLstColumn(), tableKey);

        }
        return result;

    }

    private void addColumnInfo(List<Map<String, Object>> datas, List<Column> lstColumn, String parentKey) {
        if (lstColumn == null || lstColumn.isEmpty()) {
            return;
        }
        for (Column column : lstColumn) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.TreeNodeNames.ID, column.getColumnDto().getColumnId());
            map.put(Constants.TreeNodeNames.CODE, column.getColumnDto().getColumnId());
            map.put(Constants.TreeNodeNames.NAME, "[" + column.getColumnDto().getFieldName() + "]" + column.getColumnDto().getTitle());
            map.put(Constants.TreeNodeNames.IS_LEAF, 1);
            map.put(Constants.TreeNodeNames.PARENT, parentKey);
            map.putAll(CommonUtils.toMap(column.getColumnDto(), false));
            datas.add(map);
        }
    }

    /**
     * 查询所有的视图信息
     *
     * @param schemaId
     * @return
     */
    public List<BlockViewDto> getBlockViews(String schemaId) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(BlockViewDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", SessionUtils.getLoginVersion());
        return factory.getDefaultDataOperator().select(queryParamDefinition, BlockViewDto.class);
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

    @Caching(evict = {@CacheEvict(value = GROUP_NAME, key = KEY_VIEWER)})
    @Transactional(readOnly = false)
    public String saveBlock(BlockViewer viewer) {

        String err = checkBlock(viewer);
        if (CommonUtils.isNotEmpty(err)) {
            return err;
        }
        long viewId = viewer.getBlockViewDto().getBlockViewId();
        viewer.updateViewKeys();
        //如果大于0,则需要先删除
        if (viewId > 0) {
            DeleteParamDefinition deleteParam = new DeleteParamDefinition();
            deleteParam.setTableDto(BlockViewDto.class);
            deleteParam.getCriteria().andEqualTo("block_view_id", viewId).andEqualTo("version_code", SessionUtils.getLoginVersion());
            factory.getDefaultDataOperator().delete(deleteParam);
            deleteParam.setTableDto(ComponentDto.class);
            factory.getDefaultDataOperator().delete(deleteParam);
        }
        viewer.getBlockViewDto().setVersionCode(SessionUtils.getLoginVersion());
        saveBlockInfo(viewer.getBlockViewDto());
        saveComponents(viewer.getComponentDtos());
        return null;

    }

    private void saveComponents(List<ComponentDto> lstDto) {
        if (lstDto == null || lstDto.isEmpty()) {
            return;
        }
        InsertParamDefinition insertParamDefinition = new InsertParamDefinition();
        insertParamDefinition.setNeedConvertToUnderLine(true);
        insertParamDefinition.setObjects(lstDto);
        factory.getDefaultDataOperator().insert(insertParamDefinition);
    }

    private void saveBlockInfo(BlockViewDto dto) {
        InsertParamDefinition insertParamDefinition = new InsertParamDefinition();
        insertParamDefinition.setNeedConvertToUnderLine(true);
        insertParamDefinition.setObject(dto);
        factory.getDefaultDataOperator().insert(insertParamDefinition);
    }

    private String checkBlock(BlockViewer viewer) {
        return null;
    }
}
