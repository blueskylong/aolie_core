package com.ranranx.aolie.core.service;

import com.ranranx.aolie.application.menu.service.MenuService;
import com.ranranx.aolie.application.page.service.PageService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.interfaces.IReferenceDataFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/12/23 0023 14:22
 * @Version V0.0.1
 **/
@Component("SchemaTableRefFilter")
public class SchemaTableRefFilter implements IReferenceDataFilter {
    /**
     * 菜单ID的字段
     */
    public static final String schemaIDField = "schema_id";


    @Autowired
    private MenuService menuService;

    @Autowired
    private PageService pageService;

    public SchemaTableRefFilter() {
        System.out.println("err");
    }


    @Override
    public Criteria getExtFilter(Long refId, Long colId, Map<String, Object> values) throws InvalidException {
        //values里必须要有MenuId字段.
        try {
            Long menuColId = Long.parseLong(values.keySet().iterator().next());
            if (!SchemaHolder.getColumn(menuColId, SessionUtils.getLoginVersion()).getColumnDto().getFieldName().equals(schemaIDField)) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        Object schemaId = values.values().iterator().next();
        //查询菜单对应的页面信息
        if (CommonUtils.isEmpty(schemaId)) {
            return null;
        }

        Criteria criteria = new Criteria();
        criteria.andEqualTo("schema_id", schemaId);
        return criteria;
    }

}
