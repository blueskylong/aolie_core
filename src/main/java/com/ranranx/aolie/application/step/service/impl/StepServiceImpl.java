package com.ranranx.aolie.application.step.service.impl;

import com.ranranx.aolie.application.step.dto.StepDetail;
import com.ranranx.aolie.application.step.dto.StepInfo;
import com.ranranx.aolie.application.step.dto.StepMain;
import com.ranranx.aolie.application.step.service.StepService;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.service.BaseDbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/8/3 0003 7:59
 **/
@Service
@Transactional(readOnly = true)
public class StepServiceImpl extends BaseDbService implements StepService {

    public StepServiceImpl() {
        super();
        this.schemaId = Constants.DEFAULT_SYS_SCHEMA;
    }

    @Override
    public StepInfo findStepInfo(Long stepId) {
        if (stepId == null) {
            return null;
        }
        StepMain main = new StepMain();
        main.setStepId(stepId);
        main = this.queryOne(main, null);
        if (main == null) {
            return null;
        }
        StepDetail detail = new StepDetail();
        detail.setStepId(stepId);
        List<StepDetail> lstDetail = queryList(detail, null);
        StepInfo info = new StepInfo();
        info.setStepMain(main);
        info.setLstDetail(lstDetail);
        return info;
    }
}
