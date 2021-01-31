package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

@FormulaElementTranslator
public class NotIncludeWith extends IncludeWith {


    public NotIncludeWith() {
        elementCN = "不含有(*)";

        startStr = ".indexOf(";
        startStrCN = "不含有(";
        endStr = ")===-1";
        endStrCN = ")";
    }

    @Override
    public int getOrder() {
        return this.getElementType() + 11;
    }

    @Override
    public String getExpressionCN() {
        return "不含有()";
    }
}
