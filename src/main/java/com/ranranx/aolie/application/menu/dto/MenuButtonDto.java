package com.ranranx.aolie.application.menu.dto;

import com.ranranx.aolie.core.common.BaseDto;

import javax.persistence.Table;

/**
 * SMenuButton 数据传输类
 *
 * @version 1.0
 * @date 2020-12-09 22:50:28
 */
@Table(name = "aolie_s_menu_button")
public class MenuButtonDto extends BaseDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long menuId;
    private Long btnId;
    private String params;
    private String rightCodes;
    private String lvlCode;
    private String title;
    private String states;
    private String funcName;
    private Long relationTableid;
    private Short tableOpertype;
    private String memo;
    private String iconClass;

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public void setBtnId(Long btnId) {
        this.btnId = btnId;
    }

    public Long getBtnId() {
        return this.btnId;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return this.params;
    }

    public void setRightCodes(String rightCodes) {
        this.rightCodes = rightCodes;
    }

    public String getRightCodes() {
        return this.rightCodes;
    }

    public void setLvlCode(String lvlCode) {
        this.lvlCode = lvlCode;
    }

    public String getLvlCode() {
        return this.lvlCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getStates() {
        return this.states;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncName() {
        return this.funcName;
    }

    public void setRelationTableid(Long relationTableid) {
        this.relationTableid = relationTableid;
    }

    public Long getRelationTableid() {
        return this.relationTableid;
    }

    public void setTableOpertype(Short tableOpertype) {
        this.tableOpertype = tableOpertype;
    }

    public Short getTableOpertype() {
        return this.tableOpertype;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return this.memo;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
}