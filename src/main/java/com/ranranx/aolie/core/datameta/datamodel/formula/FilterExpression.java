package com.ranranx.aolie.core.datameta.datamodel.formula;

import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.Column;
import com.ranranx.aolie.core.datameta.datamodel.SchemaHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤条件表达式
 * 支持二种表达式,一是服务端调用表达式,如用在引用数据上的过滤条件,形如:{{service1(param1,param2)}}
 * 一种是普通的表达式,如: ${1}==${3} and ${1} > 0 and ${4} =='#{-1}',
 * 其中1和3都是列ID,-1为系统参数,需要直接替换的
 * 主要功能已迁到FormulaParse中了
 */

/**
 * @author xxl
 *  过滤条件表达式
 * 支持二种表达式,一是服务端调用表达式,如用在引用数据上的过滤条件,形如:{{service1(param1,param2)}}
 * 一种是普通的表达式,如: ${1}==${3} and ${1} > 0 and ${4} =='#{-1}',
 * 其中1和3都是列ID,-1为系统参数,需要直接替换的
 * 主要功能已迁到FormulaParse中了
 * @date 2020/8/13 20:10
 * @version V0.0.1
 **/

public class FilterExpression {
    private String version;
    private String filter;
    private boolean serviceFilter = false;

    /**
     * 条件时涉及到的字段
     */
    private List<Column> lstCols = new ArrayList<Column>();

    public static FilterExpression getInstance(String filter, String version) {
        FilterExpression expression = new FilterExpression();
        expression.parseExpression(filter);
        expression.version = version;
        return expression;
    }

    private FilterExpression() {

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
        this.serviceFilter = FilterExpression.isServerExpress(filter);
        return this;
    }

    public static boolean isServerExpress(String str) {
        if (CommonUtils.isEmpty(str)) {
            return true;
        }
        return str.startsWith("{{") && str.endsWith("}}");
    }


    /**
     * 这里分析所有的字段,返回字段列表
     * 如果指定了表ID ,则只返回对应的列信息
     */
    public List<Column> getColParams(Long dsId) {
        if (this.isServiceFilter()) {
            return null;
        }
        List<String> columnParams = FormulaTools.getColumnParams(this.filter);
        if (columnParams == null) {
            return null;
        }
        List<Column> cols = new ArrayList<>();
        Column col;
        for (String colParam : columnParams) {
            col = SchemaHolder.getColumn(Long.parseLong(colParam), this.version);
            if (dsId != null) {
                if (col.getColumnDto().getTableId() == dsId.longValue()) {
                    cols.add(col);
                }
            } else {
                cols.add(col);
            }
        }
        return cols;
    }

    public boolean isServiceFilter() {
        return this.serviceFilter;
    }


    /**
     * 如果是服务服务过滤,则取得服务名及参数名{{service1(param1,param2)}}
     */
    public List<Object> getServiceNameAndParams() {
        if (!this.isServiceFilter()) {
            return null;
        }
        this.filter = this.filter.trim();
        List<Object> lst = new ArrayList<>();
        String exp = this.filter.substring(2, this.filter.length() - 2);
        String serviceName = exp.substring(0, exp.indexOf("("));
        lst.add(serviceName);
        if (exp.indexOf("(") != -1) {
            String params = exp.substring(exp.indexOf("(") + 1, exp.length() - 1);
            String[] split = params.split(",");
            for (int i = 0; i < split.length; i++) {
                if (!CommonUtils.isEmpty(split[i])) {
                    lst.add(SchemaHolder.getColumn(Long.parseLong(split[i]), this.version));
                }
            }
        }
        return lst;
    }
}
