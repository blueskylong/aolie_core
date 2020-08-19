package com.ranranx.aolie.common;

import java.util.List;
import java.util.Map;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/18 14:12
 * @Version V0.0.1
 **/
public class SqlTools {

    /**
     * 生成in的条件语句
     *
     * @param field
     * @param values
     * @param parIndex
     * @param paramValues
     * @return
     */
    public static String genInClause(String field,
                                     List<Object> values, int parIndex, Map<String, Object> paramValues) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        //每500生成一个in
        int total = values.size();
        int times = new Double(Math.ceil((0.0 + total) / 500)).intValue();
        StringBuilder sb = new StringBuilder();
        String key = null;
        int index = 0;
        parIndex = parIndex * 1000;
        for (int i = 0; i < times; i++) {
            sb.append(field).append(" in (");
            for (int j = 0; j < 500 && i * 500 + j < total; j++) {
                index = i * 500 + j;
                key = getParamKey(parIndex + index);
                sb.append("#{").append(key).append("},");
                paramValues.put(key, values.get(index));
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append(") or ");
        }
        return "(" + sb.substring(0, sb.length() - 3) + ")";
    }

    public static String getParamKey(int index) {
        return "P" + index;
    }

    /**
     * 字符串二边加上空格
     *
     * @param str
     * @return
     */
    public static String roundSpace(String str) {
        return " " + str + " ";
    }
}
