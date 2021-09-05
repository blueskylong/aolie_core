package com.ranranx.aolie.core.handler;

import com.ranranx.aolie.core.handler.param.Page;
import org.springframework.http.HttpStatus;

import java.beans.Transient;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/8/8 19:58
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

    private int code = HttpStatus.OK.value();

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
     * 创建默认的成功返回值
     *
     * @param result
     * @return
     */
    public static HandleResult success(Object result) {
        HandleResult handleResult = new HandleResult();
        handleResult.setSuccess(true);
        handleResult.setChangeNum(1);
        handleResult.setData(result);
        return handleResult;
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

    public <T> T getData() {
        return (T) data;
    }

    @Transient
    public List<Map<String, Object>> getLstData() {
        if (this.data instanceof List) {
            return (List<Map<String, Object>>) this.data;
        }
        return null;
    }

    public void setData(Object data) {
        this.data = data;
        if (this.data instanceof com.github.pagehelper.Page) {
            com.github.pagehelper.Page resultPage = (com.github.pagehelper.Page) this.data;
            this.page = new Page();
            page.setTotalRecord(resultPage.getTotal());
            page.setCurrentPage(resultPage.getPageNum());
            page.setPageSize(resultPage.getPageSize());
            page.setTotalPage(resultPage.getPages());
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 取得单个值
     *
     * @return
     */
    public Object singleValue() {
        if (this.data == null) {
            return null;
        }

        if (!(this.data instanceof List)) {
            return null;
        }
        if (((List) this.data).isEmpty()) {
            return null;
        }
        return ((List) this.data).get(0);
    }


}
