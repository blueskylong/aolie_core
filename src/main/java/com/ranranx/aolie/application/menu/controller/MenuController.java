package com.ranranx.aolie.application.menu.controller;

import com.ranranx.aolie.application.menu.dto.MenuDto;
import com.ranranx.aolie.application.menu.dto.MenuInfo;
import com.ranranx.aolie.application.menu.service.MenuService;
import com.ranranx.aolie.core.common.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author xxl
 * @Description
 * @Date 2020/12/10 10:55
 * @Version V0.0.1
 **/
@RestController()
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询菜单的详细信息
     *
     * @param menuId
     * @return
     */
    @RequestMapping("/findMenuInfo/{menuId}")
    public MenuInfo findMenuInfo(@PathVariable Long menuId) {
        return menuService.findMenuInfo(menuId, SessionUtils.getLoginVersion());
    }

    /**
     * 查询权限菜单
     *
     * @return
     */
    @RequestMapping("/findUserMenu")
    public List<MenuDto> findUserMenu() {
        return menuService.findUserMenu();
    }
}
