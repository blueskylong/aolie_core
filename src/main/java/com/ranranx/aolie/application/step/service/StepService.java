package com.ranranx.aolie.application.step.service;

import com.ranranx.aolie.application.step.dto.StepInfo;
import com.ranranx.aolie.core.interfaces.IBaseDbService;

/**
 * @author xxl
 */
public interface StepService extends IBaseDbService {
    /**
     * 查找步骤信息
     *
     * @param stepId
     * @return
     */
    StepInfo findStepInfo(Long stepId);
}
