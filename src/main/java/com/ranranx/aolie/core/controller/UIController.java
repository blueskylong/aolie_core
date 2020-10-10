package com.ranranx.aolie.core.controller;

import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.BlockViewer;
import com.ranranx.aolie.core.datameta.datamodel.ReferenceData;
import com.ranranx.aolie.core.datameta.dto.BlockViewDto;
import com.ranranx.aolie.core.service.DataModelService;
import com.ranranx.aolie.core.service.UIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/9/1 16:44
 * @Version V0.0.1
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
     * 查询引用数据
     *
     * @param referenceId
     * @return
     */
    @GetMapping("/findReferenceData/{referenceId}")
    public List<ReferenceData> findReferenceData(@PathVariable long referenceId) {
        return service.findReferenceData(referenceId, SessionUtils.getLoginVersion());
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
    public List<BlockViewDto> getBlockViews(@PathVariable String schemaId) {
        return uiService.getBlockViews(schemaId);
    }


    @PostMapping("/saveBlock")
    public String saveBlock(@RequestBody BlockViewer viewer) {
        return uiService.saveBlock(viewer);
    }


}
