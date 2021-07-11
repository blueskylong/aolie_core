package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.Schema;

@FormulaElementTranslator
public class IsEmpty extends AbstractTransElement {

    public IsEmpty() {
        elementInner = "==null";
        elementCN = "为空";
    }

    @Override
    public int getOrder() {
        return this.getElementType() + 70;
    }

    @Override
    public boolean isMatchCn(String str) {
        return str.indexOf(" " + this.elementCN) != -1
                && !str.startsWith(this.elementCN)
                && str.trim().endsWith(this.elementCN);
    }

    @Override
    public boolean isMatchInner(String str) {
        return str.indexOf(" " + this.elementInner) != -1
                && !str.startsWith(this.elementInner)
                && str.trim().endsWith(this.elementInner);
    }

    @Override
    public String transToCn(String curElement, TransCenter transcenter) {

        String[] eles = curElement.trim().split(" " + this.elementCN + " ");

        if (eles.length != 1) {
            throw new Error("条件不合法:需要一个元素进行比较");
        }
        return transcenter.transToCn(eles[0], transcenter) + " " + this.elementCN + " ";
    }

    @Override
    public String transToInner(String curElement, Schema schema, TransCenter transcenter) {

        String[] eles = curElement.trim().split(" " + this.elementCN + " ");

        if (eles.length != 1) {
            throw new Error("条件不合法:需要一个元素进行比较");
        }
        return transcenter.transToInner(eles[0], schema, transcenter)
                + " " + this.elementInner + " ";

    }

    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.compare;
    }


}
