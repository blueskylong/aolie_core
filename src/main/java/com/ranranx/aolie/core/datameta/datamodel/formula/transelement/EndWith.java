package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

@FormulaElementTranslator
public class EndWith extends IncludeWith {

    public EndWith() {
        elementCN = "以(*)结尾";

        startStr = ".endsWith(";
        startStrCN = "以(";
        endStr = ")";
        endStrCN = ")结尾";
    }

    @Override
    public int getOrder() {
        return this.getElementType() + 1;
    }

    @Override
    public String getExpressionCN() {
        return "以()结尾";
    }

}


