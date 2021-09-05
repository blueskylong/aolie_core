package com.ranranx.aolie.core.fixrow.contoller;

import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.fixrow.service.FixRowService;
import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/8/10 0010 20:27
 **/
@RestController
@RequestMapping("/fixrow")
public class FixRowController {

    @Autowired
    private FixRowService fixRowService;

    /**
     * 查询固定行表头
     *
     * @param fixId
     * @return
     */
    @GetMapping("/findFixRowComponents/{fixId}")
    public HandleResult findFixRowComponents(@PathVariable Long fixId) {
        return HandleResult.success(fixRowService.findFixRowComponents(fixId, SessionUtils.getLoginVersion()));
    }

    /**
     * 保存固定行数据,传过来的数据，是业务表的字段，需要先转换成固定行表字段（表fix_data)
     *
     * @param rows
     * @param fixId
     * @return
     */
    @PostMapping("/saveFixData/{fixId}")
    public HandleResult saveFixData(@RequestBody List<Map<String, Object>> rows, @PathVariable Long fixId) {
        return fixRowService.saveFixData(rows, fixId, SessionUtils.getLoginVersion());
    }

    /**
     * 查询一个固定行数据
     *
     * @param fixId
     * @return
     */
    @GetMapping("/findFixData/{fixId}")
    public HandleResult findFixData(@PathVariable Long fixId) {
        return HandleResult.success(fixRowService.findFixData(fixId, SessionUtils.getLoginVersion(), false));
    }

    /**
     * 保存业务的固定数据，不分页，需要向上汇总计算
     *
     * @param lstRowAll 第一行为主键控件信息
     * @param fixId
     * @return
     */
    @PostMapping("/saveBusiFixData/{fixId}")
    public HandleResult saveBusiFixData(@RequestBody List<Map<String, Object>> lstRowAll,
                                        @PathVariable long fixId) {
        //默认第一行为键信息
        if (lstRowAll == null || lstRowAll.isEmpty()) {
            return HandleResult.failure("未指定保存的信息");
        }
        return fixRowService.saveBusiFixData(lstRowAll, lstRowAll.remove(0), fixId, SessionUtils.getLoginVersion());
    }
}
