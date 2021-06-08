package com.ranranx.aolie.application.menu.controller;

import com.ranranx.aolie.application.menu.dto.MenuDto;
import com.ranranx.aolie.application.menu.dto.MenuInfo;
import com.ranranx.aolie.application.menu.service.MenuService;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/12/10 10:55
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
    public HandleResult findMenuInfo(@PathVariable Long menuId) {
        try {
            MenuInfo menuInfo = menuService.findMenuInfo(menuId, SessionUtils.getLoginVersion());
            HandleResult success = HandleResult.success(1);
            success.setData(menuInfo);
            return success;

        } catch (Exception e) {
            return HandleResult.failure(e.getMessage());
        }
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
