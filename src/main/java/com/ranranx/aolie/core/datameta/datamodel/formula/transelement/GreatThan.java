package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

@FormulaElementTranslator
public class GreatThan extends AbstractTransElement {
    public GreatThan() {
        elementInner = ">";
        elementCN = "大于";
    }

    @Override
    public int getOrder() {
        return this.getElementType() + 61;
    }


}
