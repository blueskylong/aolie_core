package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

@FormulaElementTranslator
public class Equals extends AbstractTransElement {

    public Equals() {
        elementInner = "==";
        elementCN = "等于";
    }

    @Override
    public int getOrder() {
        return getElementType() + 60;
    }


}
