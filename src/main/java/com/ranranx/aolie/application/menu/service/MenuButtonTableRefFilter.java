package com.ranranx.aolie.application.menu.service;

import com.ranranx.aolie.application.menu.dto.MenuInfo;
import com.ranranx.aolie.application.page.service.PageService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.interfaces.IReferenceDataFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *
 * @date 2020/12/23 0023 14:22
 * @version V0.0.1
 **/
@Component("menuButtonTableRefFilter")
public class MenuButtonTableRefFilter implements IReferenceDataFilter {
    /**
     * 菜单ID的字段
     */
    public static final String menuIDField = "menu_id";


    @Autowired
    private MenuService menuService;

    @Autowired
    private PageService pageService;

    public MenuButtonTableRefFilter() {
        System.out.println("err");
    }


    @Override
    public Criteria getExtFilter(Long refId, Long colId, Map<String, Object> values) throws InvalidException {
        //values里必须要有MenuId字段.
        try {
            Long menuColId = Long.parseLong(values.keySet().iterator().next());
            if (!SchemaHolder.getColumn(menuColId, SessionUtils.getLoginVersion()).getColumnDto().getFieldName().equals(menuIDField)) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        Object menuId = values.values().iterator().next();
        //查询菜单对应的页面信息
        if (CommonUtils.isEmpty(menuId)) {
            return null;
        }
        MenuInfo menuInfo = menuService.findMenuInfo(Long.parseLong(menuId.toString()), SessionUtils.getLoginVersion());
        List<Long> pageRefTables = pageService.findPageRefTables(menuInfo.getMenuDto().getPageId());
        if (pageRefTables == null || pageRefTables.isEmpty()) {
            return null;
        }
        Criteria criteria = new Criteria();
        criteria.andIn(null,"table_id", pageRefTables);
        return criteria;
    }

}
