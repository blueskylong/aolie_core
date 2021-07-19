package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/14 0014 16:35
 **/
@FormulaElementTranslator
public class MinExp extends BaseGroupElement {
    public MinExp() {
        elementCN = "最小(*)";
        startStr = "min(";
        startStrCN = "最小(";
        endStr = ")";
        endStrCN = ")";
    }

    private Object findNotNullObject(List<Object> lstObj) {
        for (Object obj : lstObj) {
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    @Override
    String calcGroupField(List<Object> lstObj) {
        Object notNullObj = findNotNullObject(lstObj);
        if (notNullObj instanceof Number) {
            double min = Double.MAX_VALUE;
            for (Object obj : lstObj) {
                if (obj == null) {
                    continue;
                }
                if (((Number) obj).doubleValue() < min) {
                    min = ((Number) obj).doubleValue();
                }
            }
            if (min == Double.MAX_VALUE) {
                return "null";
            }
            return String.valueOf(min);
        } else {//当成字符串
            String min = "";
            for (Object obj : lstObj) {
                if (obj == null) {
                    continue;
                }
                if (obj.toString().compareTo(min) < 0) {
                    min = obj.toString();
                }
            }
            return min;
        }
    }
}
