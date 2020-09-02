package com.ranranx.aolie.controller;

import com.ranranx.aolie.datameta.datamodel.BlockViewer;
import com.ranranx.aolie.service.DataModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 取得方案视图信息,只包含自身及控件信息(含列),不包含公式及约束,用于显示
     *
     * @param blockViewId
     * @param version
     * @return
     */
    @GetMapping("/getSchemaViewer/{blockViewId}/{version}")
    public BlockViewer getSchemaViewer(@PathVariable Long blockViewId, @PathVariable String version) {
        return service.getViewerInfo(blockViewId, version);
    }
}
