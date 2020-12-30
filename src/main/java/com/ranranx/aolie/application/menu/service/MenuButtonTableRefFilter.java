package com.ranranx.aolie.application.menu.service;

import com.ranranx.aolie.application.menu.dto.MenuInfo;
import com.ranranx.aolie.application.page.service.PageService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.interfaces.IReferenceDataFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/12/23 0023 14:22
 * @Version V0.0.1
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
    public MenuButtonTableRefFilter(){
        System.out.println("err");
    }


    @Override
    public Criteria getExtFilter(Long refId, Long colId, Map<String, Object> values) throws InvalidException {
        if (values == null || !values.containsKey(menuIDField)) {
            return null;
        }
        String menuId = CommonUtils.getStringField(values, menuIDField);
        //查询菜单对应的页面信息
        if (CommonUtils.isEmpty(menuId)) {
            return null;
        }
        MenuInfo menuInfo = menuService.findMenuInfo(Long.parseLong(menuId), SessionUtils.getLoginVersion());
        List<Long> pageRefTables = pageService.findPageRefTables(menuInfo.getMenuDto().getPageId());
        Criteria criteria = new Criteria();
        criteria.andIn("table_id", pageRefTables);
        return criteria;
    }

}
