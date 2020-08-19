package com.ranranx.aolie.datameta.dto;

import com.ranranx.aolie.common.BaseDto;

/**
 * @Author xxl
 * @Description 视图信息, 包含面板 ,列表及树.
 * @Date 2020/8/5 10:16
 * @Version V0.0.1
 **/
public class ViewerDto extends BaseDto {
    private Long viewId;

    private String viewName;
    /**
     * 默认视图类型 列表,树,面板?
     */
    private Short defaultShowType;
    /**
     * 视图编码,主要是用来在客户端生成界面,便于查找和记忆.可以带上有意义的编码
     */
    private String viewCode;

    private String memo;

    public Long getViewId() {
        return viewId;
    }

    public void setViewId(Long viewId) {
        this.viewId = viewId;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Short getDefaultShowType() {
        return defaultShowType;
    }

    public void setDefaultShowType(Short defaultShowType) {
        this.defaultShowType = defaultShowType;
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
