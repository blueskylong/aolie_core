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
public class AvgExp extends BaseGroupElement {
    public AvgExp() {
        elementCN = "平均值(*)";
        startStr = "avg(";
        startStrCN = "平均值(";
        endStr = ")";
        endStrCN = ")";
    }

    @Override
    String calcGroupField(List<Object> lstObj) {
        double dValue = 0;
        int count = 0;
        for (Object obj : lstObj) {
            if (obj == null) {
                continue;
            }
            count++;
            dValue += CommonUtils.toDouble(obj);
        }
        if (count > 0) {
            return String.valueOf(dValue / count);
        } else {
            return "0";
        }
    }
}
