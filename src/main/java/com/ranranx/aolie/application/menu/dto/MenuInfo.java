package com.ranranx.aolie.application.menu.dto;

import java.util.List;

/**
 * @author xxl
 *
 * @date 2020/12/10 10:10
 * @version V0.0.1
 **/
public class MenuInfo {
    private MenuDto menuDto;
    private List<MenuButtonDto> lstBtns;

    public MenuDto getMenuDto() {
        return menuDto;
    }

    public void setMenuDto(MenuDto menuDto) {
        this.menuDto = menuDto;
    }

    public List<MenuButtonDto> getLstBtns() {
        return lstBtns;
    }

    public void setLstBtns(List<MenuButtonDto> lstBtns) {
        this.lstBtns = lstBtns;
    }
}
