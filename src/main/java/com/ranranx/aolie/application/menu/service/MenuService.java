package com.ranranx.aolie.application.menu.service;

import com.ranranx.aolie.application.menu.dto.MenuButtonDto;
import com.ranranx.aolie.application.menu.dto.MenuDto;
import com.ranranx.aolie.application.menu.dto.MenuInfo;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.FieldOrder;
import com.ranranx.aolie.core.exceptions.NotExistException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/12/10 10:12
 **/
@Service
@Transactional(readOnly = true)
public class MenuService {
    @Autowired
    private DataOperatorFactory factory;

    @Autowired
    private HandlerFactory handlerFactory;

    /**
     * 查询菜单的详细信息
     *
     * @param menuId
     * @param version
     * @return
     */
    public MenuInfo findMenuInfo(Long menuId, String version) {
        QueryParam param = new QueryParam();
        MenuDto menuDto = new MenuDto();
        menuDto.setMenuId(menuId);
        param.setFilterObjectAndTableAndResultType(Constants.DEFAULT_SYS_SCHEMA, version, menuDto);
        HandleResult result = handlerFactory.handleQuery(param);
        menuDto = (MenuDto) result.singleValue();
        if (result == null) {
            throw new NotExistException("指定的菜单不存在");
        }
        param = new QueryParam();
        MenuButtonDto menuButtonDto = new MenuButtonDto();
        menuButtonDto.setMenuId(menuId);
        param.setFilterObjectAndTableAndResultType(Constants.DEFAULT_SYS_SCHEMA, version, menuButtonDto);
        param.addOrder(new FieldOrder(MenuButtonDto.class, "lvl_code", true, 1));
        result = handlerFactory.handleQuery(param);
        MenuInfo info = new MenuInfo();
        info.setMenuDto(menuDto);
        info.setLstBtns((List<MenuButtonDto>) result.getData());
        return info;
    }

    /**
     * 查询权限菜单
     *
     * @return
     */
    public List<MenuDto> findUserMenu() {
        QueryParam definition = new QueryParam();
        definition.setTableDtos(Constants.DEFAULT_SYS_SCHEMA, SessionUtils.getLoginVersion(), MenuDto.class);
        definition.setResultClass(MenuDto.class);
        return (List<MenuDto>) handlerFactory.handleQuery(definition).getData();
    }
}
