package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.Schema;

import java.util.Map;
@FormulaElementTranslator
public class ConstParam implements TransElement {
    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.constant;
    }

    @Override
    public String getName() {
        return "常量";
    }

    @Override
    public String getExpressionCN() {
        return this.getName();
    }

    @Override
    public int getOrder() {
        return this.getElementType();
    }

    //所有的其它不匹配的,都算做常量
    @Override
    public boolean isMatchCn(String str) {
        String s = str.trim();
        return (s.startsWith("'") && s.endsWith("'")) ||
                (s.startsWith("\"") && s.endsWith("\"")) || CommonUtils.isNumber(s);
    }

    @Override
    public boolean isMatchInner(String str) {
        String s = str.trim();
        return (s.startsWith("'") && s.endsWith("'")) ||
                (s.startsWith("\"") && s.endsWith("\"")) || CommonUtils.isNumber(s);
    }

    @Override
    public String transToCn(String curElement, TransCenter transcenter) {
        System.out.println(this.getName() + "  matched!");
        return curElement;
    }

    @Override
    public String transToInner(String curElement, Schema schema, TransCenter transcenter) {
        System.out.println(this.getName() + "  matched!");
        curElement = curElement.trim();
        if ((curElement.startsWith("'") && curElement.endsWith("'"))
                || (curElement.startsWith("\"") && curElement.endsWith("\""))) {
            return curElement;
        }

        if (CommonUtils.isNumber(curElement)) {
            return curElement;
        }
        return "'" + curElement + "'";
    }

    @Override
    public String transToValue(String curElement, Map<String, Object> rowData, Schema schema, TransCenter transcenter) {
        return this.transToInner(curElement, schema, transcenter);
    }

    @Override
    public boolean isOnlyForFilter() {
        return false;
    }
}
