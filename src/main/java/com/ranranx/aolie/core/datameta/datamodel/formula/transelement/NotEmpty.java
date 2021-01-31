package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

@FormulaElementTranslator
public class NotEmpty extends IsEmpty {


    public NotEmpty() {
        elementInner = "!=null";
        elementCN = "不为空";

    }

    @Override
    public int getOrder() {
        return this.getElementType() + 1;
    }
}
