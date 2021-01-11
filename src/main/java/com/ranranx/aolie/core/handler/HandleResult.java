package com.ranranx.aolie.core.handler;

import com.ranranx.aolie.core.handler.param.Page;

import java.beans.Transient;
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
    private boolean success = false;
    /**
     * 返回的数据
     */
    private Object data;
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
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    /**
     * 创建默认的成功返回值
     *
     * @param number
     * @return
     */
    public static HandleResult success(int number) {
        HandleResult result = new HandleResult();
        result.setSuccess(true);
        result.setChangeNum(number);
        return result;
    }

    /**
     * 创建失败结果
     *
     * @param err
     * @return
     */
    public static HandleResult failure(String err) {
        HandleResult result = new HandleResult();
        result.setErr(err);
        return result;
    }

    public Object getData() {
        return data;
    }

    @Transient
    public List<Map<String, Object>> getlstData() {
        if (this.data instanceof List) {
            return (List<Map<String, Object>>) this.data;
        }
        return null;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
