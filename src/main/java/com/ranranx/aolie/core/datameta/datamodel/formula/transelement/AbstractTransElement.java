package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.Schema;

import java.util.Map;

/**
 * @Author xxl
 * @Description 翻译元素通用基类
 * @Date 2021/1/28 15:10
 * @Version V0.0.1
 **/
public abstract class AbstractTransElement implements TransElement {
    protected String elementInner = "";
    protected String elementCN = "";

    @Override
    public String getName() {
        return this.elementCN;
    }

    @Override
    public abstract int getOrder();

    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.compare;
    }

    @Override
    public boolean isMatchCn(String str) {
        return str.indexOf(" " + elementCN + " ") != -1
                && !str.trim().startsWith(elementCN)
                && !str.trim().endsWith(elementCN);
    }

    @Override
    public boolean isMatchInner(String str) {
        //暂时不考虑引号内的问题
        return str.indexOf(elementInner) != -1
                && !str.trim().startsWith(elementInner)
                && !str.trim().endsWith(elementInner);
    }

    @Override
    public String transToCn(String curElement, TransCenter transcenter) {
        System.out.println(this.getName() + "  matched!");
        String[] eles = curElement.split(" " + elementInner + " ");

        if (eles.length != 2) {
            throw new Error(this.getName() + "条件不合法:需要二个元素进行比较");
        }
        return transcenter.transToCn(eles[0], transcenter) + " " + elementCN + " " +
                transcenter.transToCn(eles[1], transcenter);

    }

    @Override
    public String transToInner(String curElement, Schema schema
            , TransCenter transcenter) {
        System.out.println(this.getName() + "  matched!");
        String[] eles = curElement.split(" " + elementCN + " ");

        if (eles.length != 2) {
            throw new Error(this.getName() + "条件不合法:需要二个元素进行比较");
        }
        return transcenter.transToInner(eles[0], schema, transcenter) + " " + elementInner + " " +
                transcenter.transToInner(eles[1], schema, transcenter);
    }

    @Override
    public String getExpressionCN() {
        return this.getName();
    }

    protected String getValue(Map<String, Object> rowData) {
        return elementInner;
    }

    /**
     * 翻译成值表达式,
     *
     * @param curElement
     * @param rowData
     * @param schema
     * @param transcenter
     */
    @Override
    public String transToValue(String curElement, Map<String, Object> rowData,
                               Schema schema, TransCenter transcenter) {
        String[] eles = curElement.split(" " + this.elementInner + " ");

        if (eles.length != 2) {
            throw new Error(this.getName() + "条件不合法:需要二个元素进行比较");
        }
        return transcenter.transToValue(eles[0], rowData, schema, transcenter) + " "
                + this.getValue(rowData) + " " +
                transcenter.transToValue(eles[1], rowData, schema, transcenter);
    }

    @Override
    public boolean isOnlyForFilter() {
        return true;
    }

}
