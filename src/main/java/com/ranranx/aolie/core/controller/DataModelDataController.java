package com.ranranx.aolie.core.controller;

import com.ranranx.aolie.core.common.JQParameter;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.service.DmDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/9/11 10:03
 **/
@RestController
@RequestMapping("/dmdata")
public class DataModelDataController {
    @Autowired
    private DataOperatorFactory factory;

    @Autowired
    private DmDataService dmDataService;

    @RequestMapping("/findBlockData/{viewId}")
    public HandleResult findBlockDataForPage(@PathVariable Long viewId, JQParameter queryParams) throws Exception {
        return dmDataService.findBlockDataForPage(viewId, queryParams);
    }

    @RequestMapping("/findBlockDataNoPage/{viewId}")
    public List<Map<String, Object>> findBlockDataNoPage(@PathVariable Long viewId, JQParameter queryParams) throws Exception {
        return dmDataService.findBlockDataNoPage(viewId, queryParams);
    }

    /**
     * 保存增加的数据
     * 检查
     *
     * @param rows
     * @param dsId
     * @return
     */
    @PostMapping("/saveRows/{dsId}")
    public HandleResult saveRows(@RequestBody List<Map<String, Object>> rows, @PathVariable Long dsId) throws Exception {

        try {
            HandleResult result = dmDataService.saveRows(rows, dsId);
            return result;
        } catch (Exception e) {
            return HandleResult.failure(e.getMessage());
        }

    }

    /**
     * 删除指定ID数据
     *
     * @param ids
     * @param dsId
     * @return
     */
    @PostMapping("/deleteRowByIds/{dsId}")
    public HandleResult deleteRowByIds(@RequestBody List<Object> ids, @PathVariable Long dsId) {
        return dmDataService.deleteRowByIds(ids, dsId);
    }

    /**
     * 更新层次编码
     *
     * @param mapIdToCode
     */
    @PostMapping("/updateLevel/{viewId}")
    public HandleResult updateLevel(@RequestBody Map<Long, String> mapIdToCode, @PathVariable long viewId) {
        return dmDataService.updateLevel(mapIdToCode, viewId);
    }

    /**
     * 查询表的单行
     *
     * @param dsId
     * @param id
     * @return
     */
    @RequestMapping("/findTableRow/{dsId}/{id}")
    public HandleResult findTableRow(@PathVariable Long dsId, @PathVariable Long id) {
        return dmDataService.findTableRow(dsId, id, SessionUtils.getLoginVersion());
    }

    /**
     * 查询表的单行
     *
     * @param dsId
     * @return
     */
    @RequestMapping("/findTableRows/{dsId}")
    public HandleResult findTableRows(@PathVariable Long dsId, JQParameter queryParams) {
        return dmDataService.findTableRows(dsId, queryParams);
    }

    /**
     * 查询表数据
     *
     * @param dsId
     * @param fieldId
     * @param filter
     * @return
     */
    @RequestMapping("/findTableFieldRows/{dsId}/{fieldId}")
    public HandleResult findTableFieldRows(@PathVariable Long dsId, @PathVariable Long fieldId,
                                           @RequestBody Map<String, Object> filter) {
        return dmDataService.findTableFieldRows(dsId, fieldId, filter, SessionUtils.getLoginVersion());
    }
}
