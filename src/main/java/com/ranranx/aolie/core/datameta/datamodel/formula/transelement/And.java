package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;


@FormulaElementTranslator
public class And extends Or {
    public And() {
        elementInner = "&&";
        elementCN = "并且";
    }

    @Override
    public int getOrder() {
        return this.getElementType() + 10;
    }

    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.logic;
    }


}
