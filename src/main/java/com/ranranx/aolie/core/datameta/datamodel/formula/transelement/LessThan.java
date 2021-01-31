package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

@FormulaElementTranslator
public class LessThan extends AbstractTransElement {


    public LessThan() {
        elementInner = "<";
        elementCN = "小于";
    }

    @Override
    public int getOrder() {
        return this.getElementType() + 67;
    }


}
