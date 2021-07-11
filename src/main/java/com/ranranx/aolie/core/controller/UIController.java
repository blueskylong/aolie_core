package com.ranranx.aolie.core.controller;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.BlockViewer;
import com.ranranx.aolie.core.datameta.datamodel.ReferenceData;
import com.ranranx.aolie.core.datameta.dto.BlockViewDto;
import com.ranranx.aolie.core.datameta.dto.TableDto;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.service.DataModelService;
import com.ranranx.aolie.core.service.UIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/9/1 16:44
 **/
@RestController
@RequestMapping("/ui")
public class UIController {

    @Autowired
    private DataModelService service;

    @Autowired
    private UIService uiService;


    /**
     * 取得方案视图信息,只包含自身及控件信息(含列),不包含公式及约束,用于显示
     *
     * @param blockViewId
     * @return
     */
    @GetMapping("/getSchemaViewer/{blockViewId}")
    public BlockViewer getSchemaViewer(@PathVariable Long blockViewId) {
        return uiService.getViewerInfo(blockViewId, SessionUtils.getLoginVersion());
    }

    /**
     * 取得方案视图信息(通过编码）,只包含自身及控件信息(含列),不包含公式及约束,用于显示
     *
     * @param blockViewCode
     * @return
     */
    @GetMapping("/getSchemaViewerByCode/{blockViewCode}")
    public BlockViewer getSchemaViewerByCode(@PathVariable String blockViewCode) {
        return uiService.getViewerInfoByCode(blockViewCode, SessionUtils.getLoginVersion());
    }


    /**
     * 查询引用数据
     *
     * @param referenceId
     * @return
     */
    @RequestMapping("/findReferenceData/{referenceId}")
    public List<ReferenceData> findReferenceData(@PathVariable long referenceId) {
        return service.findReferenceData(referenceId, SessionUtils.getLoginVersion());
    }

    /**
     * 查询引用数据,带过滤条件的,主要用于选择框
     *
     * @param referenceId
     * @param colId
     * @param filter
     * @return
     */
    @RequestMapping("/findColumnReferenceData/{referenceId}/{colId}")
    public List<ReferenceData> findColumnReferenceData(@PathVariable long referenceId, @PathVariable long colId,
                                                       @RequestBody Map<String, Object> filter) {
        return service.findColumnReferenceData(referenceId, colId, SessionUtils.getLoginVersion(), filter);
    }

    /**
     * 查询引用数据
     *
     * @param tableIds
     * @return
     */
    @PostMapping("/findTablesAndFields")
    public List<Map<String, Object>> findTablesAndFields(@RequestBody long[] tableIds) {
        return uiService.findTablesAndFields(tableIds);
    }

    @GetMapping("/getBlockViews/{schemaId}")
    public List<BlockViewDto> getBlockViews(@PathVariable Long schemaId) {
        return uiService.getBlockViews(schemaId);
    }


    @PostMapping("/saveBlock")
    public String saveBlock(@RequestBody BlockViewer viewer) {
        return uiService.saveBlock(viewer);
    }

    /**
     * 查询方案中所有表的信息
     *
     * @param schemaId
     * @return
     */
    @GetMapping("/findAllTableInfo/{schemaId}")
    public List<TableDto> findAllTableInfo(@PathVariable long schemaId) {
        return uiService.findAllTableInfo(schemaId, SessionUtils.getLoginVersion());
    }

    /**
     * 查询方案中所有表的信息
     *
     * @param viewName
     * @param schemaId
     * @param parentId
     * @return
     */
    @PostMapping("/genNewBlockViewer/{schemaId}/{parentId}")
    public Long genNewBlockViewer(@RequestBody Map viewName,
                                  @PathVariable Long schemaId, @PathVariable String parentId) {
        return uiService.genNewBlockViewer(CommonUtils.getStringField(viewName, "viewName"),
                schemaId, parentId);
    }

    @RequestMapping("/deleteBlockView/{blockViewId}")
    public HandleResult deleteBlockView(@PathVariable long blockViewId) {
        return HandleResult.success(uiService.deleteBlockView(blockViewId));
    }

    /**
     * 更新页面的层次设置
     *
     * @param mapIdToCode
     */
    @PostMapping("/updateBlockLevel/{schemaId}")
    public HandleResult updateBlockLevel(@RequestBody Map<Long, String> mapIdToCode, @PathVariable long schemaId) {
        uiService.updateBlockLevel(mapIdToCode, schemaId);
        return HandleResult.success(1);
    }


}
