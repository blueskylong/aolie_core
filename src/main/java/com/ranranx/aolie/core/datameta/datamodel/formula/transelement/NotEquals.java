package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

@FormulaElementTranslator
public class NotEquals extends AbstractTransElement {


    public NotEquals() {
        elementInner = "!=";
        elementCN = "不等于";
    }

    @Override
    public int getOrder() {
        return this.getElementType() + 5;
    }


}
