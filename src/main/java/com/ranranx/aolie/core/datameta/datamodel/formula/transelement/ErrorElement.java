package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.datameta.datamodel.DmConstants;
import com.ranranx.aolie.core.datameta.datamodel.Formula;
import com.ranranx.aolie.core.datameta.datamodel.Schema;

import java.util.List;
import java.util.Map;

/**
 * 这个翻译,是最后一个翻译,到此处时,已表明是没有翻译器处理了
 */
@FormulaElementTranslator
public class ErrorElement implements TransElement {
    @Override
    public int getElementType() {
        return DmConstants.FormulaElementType.error;
    }

    @Override
    public String getExpressionCN() {
        return "无法解析元素";
    }

    @Override
    public String getName() {
        return "无法解析元素";
    }

    @Override
    public int getOrder() {
        return DmConstants.FormulaElementType.error;
    }

    @Override
    public boolean isMatchCn(String str) {
        return true;
    }

    @Override
    public boolean isMatchInner(String str) {
        return true;
    }

    @Override
    public String transToCn(String curElement, TransCenter transcenter) {
        throw new Error("[" + curElement + "]无法解析");
    }

    @Override
    public String transToInner(String curElement, Schema schema, TransCenter transcenter) {
        throw new Error("[" + curElement + "]无法解析");
    }

    @Override
    public String transToValue(String curElement, long tableId, Map<String, Object> rowData,
                               Schema schema, TransCenter transcenter, Formula formula, Map<String, List<Object>> mapGroup) {
        throw new Error("[" + curElement + "]无法解析");
    }

    @Override
    public boolean isOnlyForFilter() {
        return false;
    }
}
