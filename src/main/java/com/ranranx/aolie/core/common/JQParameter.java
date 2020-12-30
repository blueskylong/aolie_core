package com.ranranx.aolie.core.common;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranranx.aolie.core.datameta.datamodel.BlockViewer;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.ds.definition.FieldOrder;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.handler.param.Page;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.interfaces.RequestParamHandler;
import com.ranranx.aolie.core.service.DataModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
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
    private static Logger logger = LoggerFactory.getLogger(JQParameter.class);

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
        if (request.getMethod().equalsIgnoreCase("POST")) {
            mapParam = (Map) JSONUtils.parse(getPostData(request));
        }
        String value = getParamValue("blockId", mapParam);
        if (CommonUtils.isEmpty(value)) {
            throw new InvalidParamException("没有指定视图ID");
        }
        long blockId = new Long(value);
        BlockViewer blockViewer = SchemaHolder.getViewerInfo(blockId, SessionUtils.getLoginVersion());

        boolean needConvert = blockViewer.getBlockViewDto().getFieldToCamel() != null && blockViewer.getBlockViewDto().getFieldToCamel() == 1;
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        String search = getParamValue("_search", mapParam);
        if (CommonUtils.isEmpty(search) || search.equals("true")) {
            parseCriteria(getParamValue("filters", mapParam), queryParamDefinition.appendCriteria(),
                    needConvert);
        }
        String extFilter = getParamValue("extFilter", mapParam);
        if (extFilter != null) {
            System.out.println("---->" + getParamValue("extFilter", mapParam));
            parseExtCriteria(extFilter, queryParamDefinition.appendCriteria(), needConvert);
        }
        queryParamDefinition.setFields(blockViewer.getFields());
        //TODO 这里要增加表关系
        FieldOrder order = parseOrder(mapParam, blockViewer, needConvert);
        if (order != null) {
            queryParamDefinition.addOrder(order);
        }
        queryParamDefinition.setPage(parsePage(mapParam));
        return queryParamDefinition;

    }

    public QueryParam getQueryParam() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map mapParam = request.getParameterMap();
        if (request.getMethod().equalsIgnoreCase("POST")) {
            mapParam = (Map) JSONUtils.parse(getPostData(request));
        }
        String value = getParamValue("blockId", mapParam);
        if (CommonUtils.isEmpty(value)) {
            throw new InvalidParamException("没有指定视图ID");
        }
        long blockId = new Long(value);
        BlockViewer blockViewer = SchemaHolder.getViewerInfo(blockId, SessionUtils.getLoginVersion());

        boolean needConvert = blockViewer.getBlockViewDto().getFieldToCamel() != null && blockViewer.getBlockViewDto().getFieldToCamel() == 1;
        QueryParam queryParam = new QueryParam();
        String search = getParamValue("_search", mapParam);
        if (CommonUtils.isEmpty(search) || search.equals("true")) {
            parseCriteria(getParamValue("filters", mapParam), queryParam.appendCriteria(),
                    needConvert);
        }
        String extFilter = getParamValue("extFilter", mapParam);
        if (extFilter != null) {
            System.out.println("---->" + getParamValue("extFilter", mapParam));
            parseExtCriteria(extFilter, queryParam.appendCriteria(), needConvert);
        }
        queryParam.setFields(blockViewer.getFields());
        queryParam.setTable(blockViewer.getViewTables());
        //TODO 这里要增加表关系
        FieldOrder order = parseOrder(mapParam, blockViewer, needConvert);
        if (order != null) {
            queryParam.addOrder(order);
        }
        queryParam.setPage(parsePage(mapParam));
        return queryParam;

    }

    private static String getPostData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine())) {
                data.append(line);
            }
        } catch (IOException e) {
        } finally {
        }
        return data.toString();
    }

    private String getParamValue(String param, Map map) {
        if (map.get(param) == null) {
            return null;
        }
        Object obj = map.get(param);
        if (obj == null) {
            return null;
        } else if (obj.getClass().isArray()) {
            return ((String[]) obj)[0];
        } else {
            return obj.toString();
        }
    }

    private Page parsePage(Map mapParam) {
        Page page = new Page();
        String pageParam = getParamValue("page", mapParam);
        if (CommonUtils.isEmpty(pageParam)) {
            return null;
        }
        page.setCurrentPage(Integer.parseInt(pageParam));
        String rowParam = getParamValue("rows", mapParam);
        if (CommonUtils.isEmpty(rowParam)) {
            return null;
        }
        page.setPageSize(Integer.parseInt(rowParam));
        return page;
    }

    private FieldOrder parseOrder(Map mapParam, BlockViewer blockViewer, boolean convertToUnderLine) {
        String field = getParamValue("sidx", mapParam);
        if (CommonUtils.isEmpty(field)) {
            return null;
        }
        if (blockViewer.getBlockViewDto().getFieldToCamel() != null
                && blockViewer.getBlockViewDto().getFieldToCamel() == 1) {
            field = CommonUtils.convertToUnderline(field);
        }

        FieldOrder order = new FieldOrder();
        order.setField(field);
        order.setTableName(
                SchemaHolder.getTable(blockViewer.getComponentByFieldName(field).getColumn().getColumnDto().getTableId(),
                        blockViewer.getBlockViewDto().getVersionCode()).getTableDto().getTableName());
        order.setAsc(getParamValue("sord", mapParam).equals("asc"));
        return order;
    }


    private void parseCriteria(String filter, Criteria criteria, boolean convertToUnderLine) {
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
                if (convertToUnderLine) {
                    field = CommonUtils.convertToUnderline(field);
                }
                addCriteria(criteria, field, op, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 扩展条件 类型于{field1#eq:"dd"}
     *
     * @param filter
     * @param criteria
     */
    private void parseExtCriteria(String filter, Criteria criteria, boolean convertToUnderLine) {
        if (CommonUtils.isEmpty(filter)) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map map = mapper.readValue(filter, Map.class);
            genFilter(criteria, convertToUnderLine, map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void genFilter(Criteria criteria, boolean convertToUnderLine, Map map) {
        Iterator iterator = map.entrySet().iterator();
        Map.Entry entry;
        String key;
        String field, op;
        Object value;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            key = entry.getKey().toString();
            if (key.indexOf("#") != -1) {
                String[] split = key.split("#");
                field = split[0];
                op = split[1];
            } else {
                op = "eq";
                field = key;
            }
            if (convertToUnderLine) {
                field = CommonUtils.convertToUnderline(field);
            }
            addCriteria(criteria, field, op, entry.getValue() == null ? null : entry.getValue().toString());

        }
    }

    private static void addCriteria(Criteria criteria, String field, String op, String value) {
        //如果字段是数字,表示colId,需要通过模型转换
        if (CommonUtils.isNumber(field)) {
            long fieldId = Long.parseLong(field);
            Column column = SchemaHolder.getColumn(fieldId, SessionUtils.getLoginVersion());
            if (column == null) {
                logger.error("没有找到字段信息:" + field);
                return;
            }
            field = column.getColumnDto().getFieldName();
        }
        if (value == null) {
            criteria.andIsNull(field);
        } else if (op.equals("bw")) {
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
}
