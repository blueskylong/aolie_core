package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.Formula;
import com.ranranx.aolie.core.datameta.datamodel.Schema;

import java.util.Map;
import java.util.regex.Pattern;

@FormulaElementTranslator
public class Operator implements TransElement {
    static final String sPattern = "[\\+\\-\\*\\/]";
    static Pattern patten = Pattern.compile("[\\+\\-\\*\\/]");

    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.mathOperator;
    }

    @Override
    public String getName() {
        return "数学操作符";
    }

    @Override
    public String getExpressionCN() {
        return this.getName();
    }

    @Override
    public int getOrder() {
        return this.getElementType() + 10;
    }

    @Override
    public boolean isMatchCn(String str) {
        return Operator.patten.matcher(str).find();
    }

    @Override
    public boolean isMatchInner(String str) {
        return Operator.patten.matcher(str).find();
    }

    @Override
    public String transToCn(String curElement, TransCenter transcenter) {

        String[] strs = curElement.split("[\\+\\-\\*\\/]");
        if (strs.length < 2) {
            throw new Error(this.getName() + "数学运算符,需要二个操作数");
        }
        String oper = curElement.substring(strs[0].length(), 1);
        String rightExp = curElement.substring(strs[0].length() + 1);
        //仅将式子分隔成二块,由中心处理其它
        return transcenter.transToCn(strs[0], null)
                + oper + transcenter.transToCn(rightExp, null);
    }

    @Override
    public String transToInner(String curElement, Schema schema, TransCenter transcenter) {

        String[] strs = curElement.split("[\\+\\-\\*\\/]");
        if (strs.length < 2) {
            throw new Error(this.getName() + "数学运算符,需要二个操作数");
        }
        String oper = curElement.substring(strs[0].length(), 1);
        String rightExp = curElement.substring(strs[0].length() + 1);
        //仅将式子分隔成二块,由中心处理其它
        return transcenter.transToInner(strs[0], schema, transcenter)
                + oper + transcenter.transToInner(rightExp, schema, transcenter);
    }

    @Override
    public String transToValue(String curElement, long tableId, Map<String, Object> rowData,
                               Schema schema, TransCenter transcenter, Formula formula) {

        String[] strs = curElement.split(sPattern);
        if (strs.length < 2) {
            throw new Error(this.getName() + "数学运算符,需要二个操作数");
        }
        String oper = curElement.substring(strs[0].length(), strs[0].length()+1);
        String rightExp = curElement.substring(strs[0].length() + 1);
        //仅将式子分隔成二块,由中心处理其它
        return transcenter.transToValue(strs[0], tableId, rowData, schema, transcenter, formula) +
                oper + transcenter.transToValue(rightExp, tableId, rowData, schema, transcenter, formula);
    }

    @Override
    public boolean isOnlyForFilter() {
        return false;
    }

}
