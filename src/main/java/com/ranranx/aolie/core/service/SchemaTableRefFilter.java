package com.ranranx.aolie.core.service;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.interfaces.IReferenceDataFilter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/12/23 0023 14:22
 **/
@Component("SchemaTableRefFilter")
public class SchemaTableRefFilter implements IReferenceDataFilter {
    /**
     * 菜单ID的字段
     */
    public static final String schemaIDField = "schema_id";

    @Override
    public Criteria getExtFilter(Long refId, Long colId, Map<String, Object> values) throws InvalidException {
        //values里必须要有MenuId字段.
        try {
            Long menuColId = Long.parseLong(values.keySet().iterator().next());
            Column column = SchemaHolder.getColumn(menuColId, SessionUtils.getLoginVersion());
            if (!column.getColumnDto().getFieldName().equals(schemaIDField)) {
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
        criteria.andEqualTo(null, "schema_id", schemaId);
        return criteria;
    }

}
