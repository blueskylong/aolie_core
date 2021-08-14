package com.ranranx.aolie.core.fixrow.contoller;

import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.fixrow.service.FixRowService;
import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
