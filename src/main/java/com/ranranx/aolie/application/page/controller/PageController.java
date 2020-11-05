package com.ranranx.aolie.application.page.controller;

import com.ranranx.aolie.application.page.dto.PageDetailDto;
import com.ranranx.aolie.application.page.dto.PageInfo;
import com.ranranx.aolie.application.page.dto.PageInfoDto;
import com.ranranx.aolie.application.page.service.PageService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.handler.HandleResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/10/31 22:27
 *
 *
 *
 * @Version V0.0.1
 **/
@RestController
@RequestMapping("/page")
public class PageController {
    public PageController() {
        System.out.println("cc");
    }

    @Autowired
    private PageService pageService;

    @RequestMapping("/findPageInfos/{schemaId}")
    public List<PageInfoDto> findPageInfos(@PathVariable Long schemaId) {
        return pageService.findPageInfos(schemaId);
    }

    /**
     * 取得一页面的详细配置信息
     *
     * @param pageId
     * @return
     */
    @RequestMapping("/findPageDetail/{pageId}")
    public List<PageDetailDto> findPageDetail(@PathVariable Long pageId) {
        return pageService.findPageDetail(pageId);
    }

    @PostMapping("/addPage/{schemaId}")
    public long addPage(@RequestBody Map<String, Object> map, @PathVariable Long schemaId) {
        String pageName = CommonUtils.getStringField(map, "pageName");
        Long parentId = null;
        if (map.containsKey("parentId")) {
            parentId = Long.parseLong(CommonUtils.getStringField(map, "parentId"));
        }
        return pageService.addPage(pageName, schemaId, parentId);
    }

    @RequestMapping("/deletePage/{pageId}")
    public String deletePage(@PathVariable long pageId) {
        return pageService.deletePage(pageId);
    }

    @PostMapping("/savePageFullInfo}")
    public HandleResult savePageFullInfo(@RequestBody PageInfo pageInfo) {
        pageService.savePageFullInfo(pageInfo);
        return HandleResult.success(1);
    }
}
