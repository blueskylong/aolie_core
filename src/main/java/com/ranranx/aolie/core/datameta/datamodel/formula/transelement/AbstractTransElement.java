package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.Formula;
import com.ranranx.aolie.core.datameta.datamodel.Schema;

import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * 翻译元素通用基类
 * @version V0.0.1
 * @date 2021/1/28 15:10
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
    public String transToValue(String curElement, long rowTableId, Map<String, Object> rowData,
                               Schema schema, TransCenter transcenter, Formula formula, Map<String, List<Object>> mapGroup) {
        String[] eles = curElement.split(" " + this.elementInner + " ");
        if (eles.length != 2) {
            throw new Error(this.getName() + "条件不合法:需要二个元素进行比较");
        }
        return
                transcenter.transToValue(eles[0], rowTableId, rowData, schema, transcenter, formula, mapGroup) + " "
                        + this.getValue(rowData) + " " +
                        transcenter.transToValue(eles[1], rowTableId, rowData, schema, transcenter, formula, mapGroup);
    }

    @Override
    public boolean isOnlyForFilter() {
        return true;
    }

}
