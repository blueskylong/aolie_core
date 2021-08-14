package com.ranranx.aolie.application.menu.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * SMenu 数据传输类
 *
 * @version 1.0
 * @date 2020-12-09 22:50:28
 */
@Table(name = "aolie_s_menu")
public class MenuDto extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long menuId;
    private String menuName;
    private String icon;
    private String lvlCode;
    private Long pageId;
    private String params;
    private Short enabled;
    private Integer defaultState;
    private String funcName;
    private String memo;
    private Short hideMenubar;

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setLvlCode(String lvlCode) {
        this.lvlCode = lvlCode;
    }

    public String getLvlCode() {
        return this.lvlCode;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public Long getPageId() {
        return this.pageId;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return this.params;
    }

    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    public Short getEnabled() {
        return this.enabled;
    }

    public void setDefaultState(Integer defaultState) {
        this.defaultState = defaultState;
    }

    public Integer getDefaultState() {
        return this.defaultState;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncName() {
        return this.funcName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Short getHideMenubar() {
        return hideMenubar;
    }

    public void setHideMenubar(Short hideMenubar) {
        this.hideMenubar = hideMenubar;
    }
}