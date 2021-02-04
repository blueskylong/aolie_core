package com.ranranx.aolie.core.datameta.datamodel.expression;

import java.util.Map;

/**
 * @author xxl
 *
 * @date 2020/12/22 17:14
 * @version V0.0.1
 **/
public class FormulaExpression {
    private String originFormula;
    private String versionCode;

    private FormulaExpression() {

    }

    public FormulaExpression getInstance(String formula, String version) {
        FormulaExpression expression = new FormulaExpression();
        return expression.parseFormula(formula, version);
    }

    /**
     * 解析公式
     *
     * @param formula
     * @param version
     * @return
     */
    public FormulaExpression parseFormula(String formula, String version) {
        this.originFormula = formula;
        this.versionCode = version;
        return this;
    }

    /**
     * 取得查询语句
     *
     * @param mapAlias 别名
     * @return
     */
    public String getSqlWhere(Map<String, String> mapAlias) {
        return null;
    }

    /**
     * 直接计算值
     *
     * @param mapValues
     * @return
     */
    public Object calc(Map<String, Object> mapValues) {
        return null;
    }
}
