package com.ranranx.aolie.controller;

import com.ranranx.aolie.common.SessionUtils;
import com.ranranx.aolie.datameta.datamodel.BlockViewer;
import com.ranranx.aolie.datameta.datamodel.ReferenceData;
import com.ranranx.aolie.service.DataModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
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


    /**
     * 取得方案视图信息,只包含自身及控件信息(含列),不包含公式及约束,用于显示
     *
     * @param blockViewId
     * @return
     */
    @GetMapping("/getSchemaViewer/{blockViewId}")
    public BlockViewer getSchemaViewer(@PathVariable Long blockViewId) {
        return service.getViewerInfo(blockViewId, SessionUtils.getLoginVersion());
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

    @GetMapping("/getData")
    public List<Map<String, Object>> getData(HttpServletRequest req) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        System.out.println(parameterMap);
        List<Map<String, Object>> lst = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id22", 1);
        map.put("invdate", "xdddd");
        map.put("name", "dsfs");
        map.put("amount", "3");
        map.put("tax", "3");
        map.put("total", "3");
        map.put("note", "3");
        lst.add(map);
        map = new HashMap<>();
        map.put("id22", 3);
        map.put("invdate", "wer");
        map.put("name", "d");
        map.put("amount", "w");
        map.put("tax", "3e");
        map.put("total", "e3");
        map.put("note", "3r");
        lst.add(map);
        return lst;
    }
}
