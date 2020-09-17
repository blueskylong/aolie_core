package com.ranranx.aolie.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranranx.aolie.datameta.datamodel.BlockViewer;
import com.ranranx.aolie.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.ds.definition.FieldOrder;
import com.ranranx.aolie.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.handler.param.Page;
import com.ranranx.aolie.handler.param.condition.Criteria;
import com.ranranx.aolie.interfaces.RequestParamHandler;
import com.ranranx.aolie.service.DataModelService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/9/11 14:43
 * @Version V0.0.1
 **/
@Component
public class JQParameter implements RequestParamHandler {

    private String name = "name";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JQParameter(DataModelService dataModelService) {

    }

    @Override
    public QueryParamDefinition getQueryParamDefinition() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map mapParam = request.getParameterMap();
        long blockId = new Long(getParamValue("blockId", mapParam));
        BlockViewer blockViewer = SchemaHolder.getViewerInfo(blockId, SessionUtils.getLoginVersion());

        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        if (getParamValue("_search", mapParam).equals("true")) {
            parseCriteria(getParamValue("filters", mapParam), queryParamDefinition.appendCriteria());
        }
        queryParamDefinition.setFields(blockViewer.getFields());
        //TODO 这里要增加表关系
        FieldOrder order = parseOrder(mapParam, blockViewer);
        if (order != null) {
            queryParamDefinition.addOrder(order);
        }
        queryParamDefinition.setPage(parsePage(mapParam));
        return queryParamDefinition;

    }

    private String getParamValue(String param, Map map) {
        return ((String[]) map.get(param))[0];
    }

    private Page parsePage(Map mapParam) {
        Page page = new Page();
        page.setCurrentPage(Integer.parseInt(getParamValue("page", mapParam)));
        page.setPageSize(Integer.parseInt(getParamValue("rows", mapParam)));
        return page;
    }

    private FieldOrder parseOrder(Map mapParam, BlockViewer blockViewer) {
        String field = getParamValue("sidx", mapParam);
        if (CommonUtils.isEmpty(field)) {
            return null;
        }
        FieldOrder order = new FieldOrder();
        order.setField(field);
        order.setTableName(
                SchemaHolder.getTable(blockViewer.getComponentByFieldName(field).getColumn().getColumnDto().getTableId(),
                        blockViewer.getBlockViewDto().getVersionCode()).getTableDto().getTableName());
        order.setAsc(getParamValue("sord", mapParam).equals("asc"));
        return order;
    }

    private void parseCriteria(String filter, Criteria criteria) {
        if (CommonUtils.isEmpty(filter)) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map map = mapper.readValue(filter, Map.class);
            List<Map<String, Object>> rules = (List<Map<String, Object>>) map.get("rules");
            for (Map mapRule : rules) {
                String field = CommonUtils.getStringField(mapRule, "field");
                String op = CommonUtils.getStringField(mapRule, "op");
                String value = CommonUtils.getStringField(mapRule, "data");
                if (op.equals("bw")) {
                    criteria.andLike(field, value);
                } else if (op.equals("eq")) {
                    criteria.andEqualTo(field, value);
                } else if (op.equals("ne")) {
                    criteria.andNotEqualTo(field, value);
                } else if (op.equals("le")) {
                    criteria.andLessThanOrEqualTo(field, value);
                } else if (op.equals("lt")) {
                    criteria.andLessThan(field, value);
                } else if (op.equals("gt")) {
                    criteria.andGreaterThan(field, value);
                } else if (op.equals("ge")) {
                    criteria.andGreaterThanOrEqualTo(field, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
