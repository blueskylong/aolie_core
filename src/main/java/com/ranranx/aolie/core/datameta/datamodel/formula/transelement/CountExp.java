package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/14 0014 16:35
 **/
@FormulaElementTranslator
public class CountExp extends BaseGroupElement {
    public CountExp() {
        elementCN = "计数(*)";
        startStr = "count(";
        startStrCN = "计数(";
        endStr = ")";
        endStrCN = ")";
    }

    @Override
    String calcGroupField(List<Object> lstObj) {
        int count = 0;
        for (Object obj : lstObj) {
            if (obj == null) {
                continue;
            }
            count++;

        }
        return String.valueOf(count);
    }
}
