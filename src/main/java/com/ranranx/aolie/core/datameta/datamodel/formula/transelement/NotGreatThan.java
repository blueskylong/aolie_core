package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

@FormulaElementTranslator
public class NotGreatThan extends AbstractTransElement {


    public NotGreatThan() {
        elementInner = "<=";
        elementCN = "小于等于";
    }

    @Override
    public int getOrder() {
        return this.getElementType() + 9;
    }
}
