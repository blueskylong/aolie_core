package com.ranranx.aolie.core.datameta.datamodel.expression;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.runtime.GlobalParam;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 *  过滤条件表达式
 * 支持二种表达式,一是服务端调用表达式,如用在引用数据上的过滤条件,形如:{{service1(param1,param2)}}
 * 一种是普通的表达式,如: ${1}==${3} and ${1} > 0 and ${4} =='#{-1}',其中1和3都是列ID,-1为系统参数,需要直接替换的
 * @date 2020/12/22 17:14
 * @version V0.0.1
 **/
public class FilterExpression {
    private String version;
    private String filter;
    private boolean isServiceFilter = false;
    /**
     * 条件时涉及到的字段
     */
    private List<Column> lstCols = new ArrayList<>();

    public static FilterExpression getInstance(String filter, String version) {
        FilterExpression expression = new FilterExpression();
        expression.parseExpression(filter);
        return expression;
    }

    private FilterExpression() {

    }

    public boolean isServiceFilter() {
        return isServiceFilter;
    }

    /**
     * 解析表达式
     *
     * @param filter
     * @return
     */
    public FilterExpression parseExpression(String filter) {
        this.filter = filter;
        if (CommonUtils.isEmpty(filter)) {
            return this;
        }
        if (filter.startsWith("{{") && filter.endsWith("}}")) {
            this.isServiceFilter = true;
        }
        return this;
    }

    /**
     * 取得查询语句
     *
     * @param mapAlias 别名
     * @return
     */
    public String getSqlWhere(Map<String, String> mapAlias) {
        //先取得所有的参数,将提供值的字段都替换成常量,也将系统参数也替换成常量
        return null;
    }

    public List<String> getServiceNameAndParams() {
        if (!this.isServiceFilter) {
            return null;
        }
        filter = filter.trim();
        List<String> lst = new ArrayList<>();
        String exp = filter.substring(2, filter.length() - 2);
        String serviceName = exp.substring(0, exp.indexOf("("));
        lst.add(serviceName);
        if (exp.indexOf("(") != -1) {
            String params = exp.substring(exp.indexOf("(") + 1, exp.length() - 1);
            String[] split = params.split(",");
            for (int i = 0; i < split.length; i++) {
                if (CommonUtils.isNotEmpty(split[i])) {
                    lst.add(split[i]);
                }
            }
        }
        return lst;
    }

    public String getServiceName() {
        if (!this.isServiceFilter) {
            return null;
        }
        List<String> serviceNameAndParams = this.getServiceNameAndParams();
        return serviceNameAndParams.get(0);
    }

    /**
     * 取得过滤对象，
     * 这里只做参数的替换，然后作为一个整体表达式进行处理，不再分解成条件
     *
     * @param mapValues        字段对应的值
     * @param fullReplacedDsId 需要完全替代的表ID，如果为空则表示不要求
     * @param mapAlias         表ID对应的别名，如果没有提供，则直接使用字段名，不加前缀
     * @return
     */
    public Criteria getSqlCriteria(Map<String, Object> mapValues, Map<Long, GlobalParam> globalValues,
                                   Map<Long, String> mapAlias, List<Long> fullReplacedDsId) {
        Map<String, Object> values = new HashMap<>();

        String filter = replaceSysparams(this.filter, globalValues, values);
        if (CommonUtils.isEmpty(filter)) {
            return Criteria.getFalseExpression();
        }
        filter = this.replaceColumns(filter, mapValues, mapAlias, fullReplacedDsId, values);
        if (CommonUtils.isEmpty(filter)) {
            return Criteria.getFalseExpression();
        }
        Criteria criteria = new Criteria();
        criteria.andCondition(filter, values);
        return criteria;
    }

    /**
     * 替换所有列参数,
     *
     * @param filter
     * @param mapValues
     * @param mapAlias
     * @param fullReplacedDsId 如果指定,则需要提供所有此表内的此条件引用的字段的值
     * @param outParam         用于输出参数值
     * @return 如果返回为空, 则表示有不能替换的值
     */
    private String replaceColumns(String filter, Map<String, Object> mapValues,
                                  Map<Long, String> mapAlias, List<Long> fullReplacedDsId,
                                  Map<String, Object> outParam) {
        List<Long> columnIds = FormulaTools.getColumnIds(filter);
        if (mapAlias == null) {
            mapAlias = new HashMap<>();
        }
        if (columnIds != null && !columnIds.isEmpty()) {
            Column col;
            String version = SessionUtils.getLoginVersion();
            for (Long colId : columnIds) {
                col = SchemaHolder.getColumn(colId, version);
                //如果存在此字段的值,则直接替换
                if (mapValues.containsKey(colId.toString())) {
                    Object value = mapValues.get(colId.toString());
                    if (CommonUtils.isEmpty(value)) {
                        if (fullReplacedDsId != null
                                && fullReplacedDsId.indexOf(col.getColumnDto().getTableId()) != -1) {
                            //这里不能实现此表的条件替代,所以返回否定条件
                            return null;
                        }
                        continue;
                    }
                    String paramKey = FormulaTools.genParamName();
                    filter = StringUtils.replace(filter, toParam(colId), "#{" + paramKey + "}");
                    outParam.put(paramKey, value);
                } else {
                    //如果需要全替换的没有提供值
                    if (fullReplacedDsId != null
                            && fullReplacedDsId.indexOf(col.getColumnDto().getTableId()) != -1) {
                        //这里不能实现此表的条件替代,所以返回否定条件
                        return null;
                    }
                    //如果没有提供值 ,则要替换成字段名
                    String fieldName = col.getColumnDto().getFieldName();
                    if (mapAlias.containsKey(col.getColumnDto().getTableId())) {
                        fieldName = mapAlias.get(col.getColumnDto().getTableId()) + "." + fieldName;
                    }
                    filter = StringUtils.replace(filter, toParam(colId),
                            fieldName);
                }

            }
            return filter;
        }
        return this.filter;
    }

    /**
     * 替换所有系统参数
     *
     * @param filter
     * @return
     */
    private String replaceSysparams(String filter, Map<Long, GlobalParam> params, Map<String, Object> values) {
        List<Long> paramIds = FormulaTools.getSysParams(filter);
        if (paramIds != null && !paramIds.isEmpty()) {
            return filter;
        }
        for (Long paramID : paramIds) {
            //如果系统没有提供相应的值,则不能查询出来数据
            if (!params.containsKey(paramID)) {
                return null;
            }
            Object obj = params.get(paramID);
            if (obj == null) {
                obj = "";
            }
            String paramKey = FormulaTools.genParamName();
            filter = StringUtils.replace(filter, toParam(paramID), "#{" + paramKey + "}");
            values.put(paramKey, obj);
        }
        return filter;
    }


    private String toParam(Long colId) {
        return "${" + colId + "}";
    }

    public boolean filter(Map<String, Object> mapValues) {
        return true;
    }
}
