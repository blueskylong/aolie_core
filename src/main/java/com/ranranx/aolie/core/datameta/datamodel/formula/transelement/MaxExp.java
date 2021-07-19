package com.ranranx.aolie.core.datameta.datamodel.formula.transelement;

import com.ranranx.aolie.core.annotation.FormulaElementTranslator;

import java.util.List;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/7/14 0014 16:35
 **/
@FormulaElementTranslator
public class MaxExp extends BaseGroupElement {
    public MaxExp() {
        elementCN = "最大(*)";
        startStr = "max(";
        startStrCN = "最大(";
        endStr = ")";
        endStrCN = ")";
    }

    private Object findNotNullObject(List<Object> lstObj){
        for(Object obj :lstObj){
            if(obj != null){
                return obj;
            }
        }
        return null;
    }
    @Override
    String calcGroupField(List<Object> lstObj) {
        Object notNullObj = findNotNullObject(lstObj);
        if (notNullObj instanceof Number) {
            double max = Double.MIN_VALUE;
            for (Object obj : lstObj) {
                if (obj == null) {
                    continue;
                }
                if (((Number) obj).doubleValue() > max) {
                    max = ((Number) obj).doubleValue();
                }
            }
            if (max == Double.MIN_VALUE) {
                return "null";
            }
            return String.valueOf(max);
        } else {//当成字符串
            String max = "";
            for (Object obj : lstObj) {
                if (obj == null) {
                    continue;
                }
                if (obj.toString().compareTo(max) > 0) {
                    max = obj.toString();
                }
            }
            return max;
        }
    }
}
