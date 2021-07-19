package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;
import com.ranranx.aolie.core.common.CommonUtils;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/14 0014 16:35
 **/
@FormulaElementTranslator
public class SumExp extends BaseGroupElement {
    public SumExp() {
        elementCN = "加和(*)";
        startStr = "sum(";
        startStrCN = "加和(";
        endStr = ")";
        endStrCN = ")";
    }


    @Override
    String calcGroupField(List<Object> lstObj) {
        double dValue = 0;
        for (Object obj : lstObj) {
            if (obj == null) {
                continue;
            }

            dValue += CommonUtils.toDouble(obj);
        }
        return String.valueOf(dValue);
    }
}
