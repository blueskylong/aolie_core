package com.ranranx.aolie.application.step.controller;

import com.ranranx.aolie.application.step.service.StepService;
import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 步骤控制器
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/8/3 0003 8:00
 **/
@RestController
@RequestMapping("/step")
public class StepController {

    @Autowired
    private StepService stepService;

    @GetMapping("/findStepInfo/{stepId}")
    public HandleResult findStepInfo(@PathVariable Long stepId) {
        return HandleResult.success(stepService.findStepInfo(stepId));
    }
}
