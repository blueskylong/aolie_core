package com.ranranx.aolie.core.service;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.*;
import com.ranranx.aolie.core.datameta.dto.BlockViewDto;
import com.ranranx.aolie.core.datameta.dto.ComponentDto;
import com.ranranx.aolie.core.datameta.dto.TableDto;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.*;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.tree.LevelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/10/7 11:16
 **/
@Service
@Transactional(readOnly = true)
public class UIService {
    public static final String GROUP_NAME = "SCHEMA_VERSION";
    private static final String KEY_VIEWER = "'VIEWER_'+#p0+'_'+#p1";
    private static final String KEY_VIEWER_REMOVE = "'VIEWER_'+#p0.blockViewDto.blockViewId+'_'+#p0.blockViewDto.versionCode";
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
    public List<BlockViewDto> getBlockViews(Long schemaId) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(BlockViewDto.class);
        queryParamDefinition.appendCriteria()
                .andEqualTo(null, "schema_id", schemaId)
        ;
        return factory.getDefaultDataOperator().select(queryParamDefinition, BlockViewDto.class);
    }

    @Cacheable(value = GROUP_NAME,
            key = KEY_VIEWER)
    public BlockViewer getViewerInfo(Long blockViewId, String version) {
        if (blockViewId == DmConstants.DispType.CUSTOM_UI_ID) {
            return createCustomViewInfo();
        }
        BlockViewDto viewerDto = findViewerDto(blockViewId, version);
        if (viewerDto == null) {
            return null;
        }
        return new BlockViewer(viewerDto, findViewerComponents(blockViewId, version));
    }

    @Cacheable(value = GROUP_NAME,
            key = KEY_VIEWER)
    public BlockViewer getViewerInfoByCode(String blockViewCode, String version) {
        BlockViewDto viewerDto = findViewerDtoByCode(blockViewCode);
        if (viewerDto == null) {
            return null;
        }
        return new BlockViewer(viewerDto, findViewerComponents(viewerDto.getBlockViewId(), version));
    }

    private BlockViewer createCustomViewInfo() {
        BlockViewDto dto = new BlockViewDto();
        dto.setVersionCode(SessionUtils.getLoginVersion());
        dto.setBlockViewId(DmConstants.DispType.CUSTOM_UI_ID);
        dto.setBlockViewName("自定义视图");
        dto.setDefaultShowType((short) DmConstants.DispType.custom);
        BlockViewer viewer = new BlockViewer(dto, null);

        return viewer;
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
        queryParamDefinition.appendCriteria()
                .andEqualTo(null, "block_view_id", blockViewId)
        ;
        return factory.getDefaultDataOperator().selectOne(queryParamDefinition, BlockViewDto.class);
    }

    /**
     * 查询视图主信息
     *
     * @param blockViewCode
     * @return
     */
    private BlockViewDto findViewerDtoByCode(String blockViewCode) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(BlockViewDto.class);
        queryParamDefinition.appendCriteria()
                .andEqualTo(null, "block_code", blockViewCode);
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
        queryParamDefinition.appendCriteria()
                .andEqualTo(null, "block_view_id", blockViewId);
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
            Column column = SchemaHolder.getColumn(dto.getColumnId(), dto.getVersionCode());
            if (column == null) {
                //如果列不存在,则不返回给客户端,直接去掉
                continue;
            }
            lstResult.add(new Component(dto, column));
        }
        return lstResult;
    }

    @Caching(evict = {@CacheEvict(value = GROUP_NAME, key = KEY_VIEWER)})
    public void clearViewCache(Long blockViewId, String version) {

    }

    @Caching(evict = {@CacheEvict(value = GROUP_NAME, key = KEY_VIEWER_REMOVE)})
    @Transactional(readOnly = false)
    public String saveBlock(BlockViewer viewer) {
        String err = checkBlock(viewer);
        if (CommonUtils.isNotEmpty(err)) {
            return err;
        }
        Long viewId = viewer.getBlockViewDto().getBlockViewId();
        if (viewId == null) {
            throw new IllegalArgumentException("没有给出视图ID");
        }
        viewer.updateViewKeys();
        //如果大于0,则需要先删除
        if (viewId > 0) {
            deleteBlockView(viewId);
        }
        viewer.getBlockViewDto().setVersionCode(SessionUtils.getLoginVersion());
        saveBlockInfo(viewer.getBlockViewDto());
        saveComponents(viewer.getComponentDtos());
        return null;

    }

    @Transactional(readOnly = false)
    public int deleteBlockView(long blockViewId) {
        DeleteParamDefinition deleteParam = new DeleteParamDefinition();
        deleteParam.setTableDto(BlockViewDto.class);
        deleteParam.getCriteria().andEqualTo(null,
                "block_view_id", blockViewId);
        int number = factory.getDefaultDataOperator().delete(deleteParam);
        deleteParam.setTableDto(ComponentDto.class);
        factory.getDefaultDataOperator().delete(deleteParam);
        return number;
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

    /**
     * 查询方案中所有表的信息
     *
     * @param schemaId
     * @param versionCode
     * @return
     */
    public List<TableDto> findAllTableInfo(long schemaId, String versionCode) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(TableDto.class);
        queryParamDefinition.appendCriteria().andEqualTo(null, "schema_id", schemaId)
        ;
        queryParamDefinition.addOrder(new FieldOrder(TableDto.class, "table_id", true, 0));
        Schema schema = SchemaHolder.getInstance().getSchema(schemaId, versionCode);
        return factory.getDataOperatorByKey(schema.getDsKey()).select(queryParamDefinition, TableDto.class);
    }

    @Transactional(readOnly = false)
    public Long genNewBlockViewer(String viewName, Long schemaId, String parentId) {

        if (parentId != null && parentId.equals("null")) {
            parentId = null;
        }
        if (CommonUtils.isEmpty(viewName)) {
            throw new IllegalArgumentException("视图名称不可以为空");
        }
        BlockViewDto dto = new BlockViewDto();
        dto.setBlockViewId(IdGenerator.getNextId(BlockViewDto.class.getName()));
        dto.setBlockViewName(viewName);
        dto.setSchemaId(schemaId);
        dto.setColSpan(12);
        dto.setVersionCode(SessionUtils.getLoginVersion());
        //如果指定了父亲,则要生成相应的编码
        String curCode = "";
        if (parentId != null) {
            BlockViewer viewerInfo = this.getViewerInfo(new Long(parentId), SessionUtils.getLoginVersion());
            if (viewerInfo != null) {
                //TODO 需要添加级次管理
                curCode = viewerInfo.getBlockViewDto().getLvlCode();
            }
        }
        String maxCode = getMaxLvlCode(schemaId, curCode);
        String nextCode = new LevelProvider(maxCode).getNextCode();
        dto.setLvlCode(nextCode);
        InsertParamDefinition insertParamDefinition = new InsertParamDefinition();
        insertParamDefinition.setNeedConvertToUnderLine(true);
        insertParamDefinition.setObject(dto);
        factory.getDefaultDataOperator().insert(insertParamDefinition);
        return dto.getBlockViewId();
    }

    private String getMaxLvlCode(Long schemaId, String curCode) {
        List<BlockViewDto> blockViews = this.getBlockViews(schemaId);
        if (blockViews == null) {
            return "000";
        }

        int len = CommonUtils.isEmpty(curCode) ? 3 : curCode.length() + 3;
        String maxCode = CommonUtils.appendChar(curCode, '0', len);
        for (BlockViewDto dto : blockViews) {
            if (dto.getLvlCode().length() == len
                    && (CommonUtils.isEmpty(curCode) || dto.getLvlCode().startsWith(curCode))) {
                maxCode = maxCode.compareTo(dto.getLvlCode()) > 0 ? maxCode : dto.getLvlCode();
            }
        }
        return maxCode;
    }

    /**
     * 删除指定列对应的控件
     *
     * @param lstColId
     * @param version
     */
    public void deleteColComponent(List<Object> lstColId, String version) {
        DeleteParamDefinition definition = new DeleteParamDefinition();
        definition.setTableDto(ComponentDto.class).getCriteria().andIn(null, "column_id", lstColId)
        ;
        factory.getDefaultDataOperator().delete(definition);
    }

    /**
     * 更新页面的层次设置
     *
     * @param mapIdToCode
     */
    @Transactional(readOnly = false)
    public void updateBlockLevel(Map<Long, String> mapIdToCode, long schemaId) {
        if (mapIdToCode == null || mapIdToCode.isEmpty()) {
            return;
        }
        UpdateParamDefinition updateParamDefinition;
        updateParamDefinition = new UpdateParamDefinition();
        updateParamDefinition.setSelective(true);
        updateParamDefinition.setTableNameByDto(BlockViewDto.class);
        updateParamDefinition.setIdField("block_view_id,schema_id,version_code");

        updateParamDefinition.setLstRows(toMap(mapIdToCode, schemaId));
        factory.getDefaultDataOperator().update(updateParamDefinition);
    }

    private List<Map<String, Object>> toMap(Map<Long, String> mapIdToCode, long schemaId) {
        Iterator<Map.Entry<Long, String>> iterator = mapIdToCode.entrySet().iterator();

        List<Map<String, Object>> lstResult = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Long, String> next = iterator.next();
            Map<String, Object> map = new HashMap<>();
            map.put("version_code", SessionUtils.getLoginVersion());
            map.put("block_view_id", next.getKey());
            map.put("schema_id", schemaId);
            map.put("lvl_code", next.getValue());
            lstResult.add(map);
        }
        return lstResult;
    }


}
