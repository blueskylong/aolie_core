package com.ranranx.aolie.core.plugs.controller;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.plugs.dto.PlugDto;
import com.ranranx.aolie.core.plugs.service.PlugsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/7 0007 10:28
 **/
@RestController
@RequestMapping("/plug")
public class PlugController {
    protected final PlugsService service;

    public PlugController(PlugsService service) {
        this.service = service;
    }

    /**
     * 取得插件信息
     *
     * @return 是否成功
     */
    @GetMapping("/getPlugInfos")
    public List<PlugDto> getPlugInfos() {
        return service.getPlugInfos();
    }

    /**
     * 安装
     *
     * @return 是否成功
     */
    @RequestMapping("/install/{plugId}")
    public HandleResult install(@PathVariable Long plugId) {
        if (service.install(plugId)) {
            return HandleResult.success(0);
        } else {
            return HandleResult.failure("更新失败");
        }
    }

    /**
     * 卸载
     *
     * @return 是否成功
     */
    @RequestMapping("/uninstall/{plugId}")
    public HandleResult uninstall(@PathVariable Long plugId) {

        if (service.uninstall(plugId)) {
            return HandleResult.success(0);
        } else {
            return HandleResult.failure("更新失败");
        }
    }

    /**
     * 升级
     *
     * @return 是否成功
     */
    @RequestMapping("/update/{plugId}")
    public HandleResult update(@PathVariable Long plugId) {

        if (service.update(plugId)) {
            return HandleResult.success(0);
        } else {
            return HandleResult.failure("更新失败");
        }
    }


    /**
     * 检查当前系统配置问题
     *
     * @return 错误信息
     */
    @RequestMapping("/check/{plugId}")
    public String check(@PathVariable Long plugId) {
        return service.check(plugId);
    }

    /**
     * 修复
     *
     * @return
     */
    @RequestMapping("/repair/{plugId}")
    public HandleResult repair(@PathVariable Long plugId) {
        String err = service.repair(plugId);

        if (CommonUtils.isEmpty(err)) {
            return HandleResult.success(0);
        } else {
            return HandleResult.failure(err);
        }
    }
}
