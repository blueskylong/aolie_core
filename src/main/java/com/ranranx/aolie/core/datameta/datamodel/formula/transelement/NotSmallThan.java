package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

@FormulaElementTranslator
public class NotSmallThan extends AbstractTransElement {

    public NotSmallThan() {
        elementInner = ">=";
        elementCN = "大于等于";

    }

    @Override
    public int getOrder() {
        return this.getElementType() + 14;
    }
}
