package com.ranranx.aolie.application.menu.service;

import com.ranranx.aolie.application.menu.dto.MenuButtonDto;
import com.ranranx.aolie.application.menu.dto.MenuDto;
import com.ranranx.aolie.application.menu.dto.MenuInfo;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.exceptions.NotExistException;
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
        QueryParamDefinition definition = new QueryParamDefinition();
        definition.setTableDtos(MenuDto.class);
        definition.appendCriteria().andEqualTo("menu_id", menuId).andEqualTo("version_code", version);
        MenuDto menuDto = factory.getDefaultDataOperator().selectOne(definition, MenuDto.class);
        if (menuDto == null) {
            throw new NotExistException("指定的菜单不存在");
        }
        definition.setTableDtos(MenuButtonDto.class);
        definition.addOrderField("lvl_code");
        List<MenuButtonDto> lstButtons = factory.getDefaultDataOperator().select(definition, MenuButtonDto.class);
        MenuInfo info = new MenuInfo();
        info.setMenuDto(menuDto);
        info.setLstBtns(lstButtons);
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
        definition.appendCriteria().andEqualTo("version_code", SessionUtils.getLoginVersion());
        definition.setResultClass(MenuDto.class);
        return (List<MenuDto>) handlerFactory.handleQuery(definition).getData();
    }
}
