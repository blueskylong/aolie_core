package com.ranranx.aolie.handler;

import com.ranranx.aolie.handler.param.Page;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/8 19:58
 * @Version V0.0.1
 **/
public class HandleResult {

    /**
     * 是否成功
     */
    private boolean isSuccess = false;
    /**
     * 返回的数据
     */
    private List<Map<String, Object>> lstData;
    /**
     * 错误提供信息
     */
    private String err;

    /**
     * 数据变化记录数
     */
    private int changeNum = 0;
    /**
     * 分页信息
     */
    private Page page;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public List<Map<String, Object>> getLstData() {
        return lstData;
    }

    public void setLstData(List<Map<String, Object>> lstData) {
        this.lstData = lstData;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }
}
