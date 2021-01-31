package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;

/**
 * @Author xxl
 * @Description 或者翻译
 * @Date 2021/1/28 15:10
 * @Version V0.0.1
 **/
@FormulaElementTranslator
public class Or extends AbstractTransElement {

    public Or() {
        elementInner = "||";
        elementCN = "或者";

    }

    @Override
    public int getOrder() {
        return this.getElementType() + 15;
    }

    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.logic;
    }

}
