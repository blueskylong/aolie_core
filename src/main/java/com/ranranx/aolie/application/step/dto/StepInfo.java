package com.ranranx.aolie.application.step.dto;

import java.util.List;

/**
 * 步骤整合信息类，仅包含主从信息
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/8/3 0003 8:01
 **/
public class StepInfo {
    /**
     * 主表信息
     */
    private StepMain stepMain;
    /**
     * 明细信息
     */
    private List<StepDetail> lstDetail;


    public StepMain getStepMain() {
        return stepMain;
    }

    public void setStepMain(StepMain stepMain) {
        this.stepMain = stepMain;
    }

    public List<StepDetail> getLstDetail() {
        return lstDetail;
    }

    public void setLstDetail(List<StepDetail> lstDetail) {
        this.lstDetail = lstDetail;
    }
}
