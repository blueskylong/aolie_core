package com.ranranx.aolie.core.common;

import com.ranranx.aolie.core.datameta.datamodel.DmConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2020/8/18 14:12
 **/
public class SqlTools {

    private static Map<String, String> mapColTypeRelation;

    static {
        mapColTypeRelation = new HashMap<>();
        mapColTypeRelation.put(DmConstants.FieldType.INT, DmConstants.MyBatisColumnType.INT);
        mapColTypeRelation.put(DmConstants.FieldType.DECIMAL, DmConstants.MyBatisColumnType.DECIMAL);
        mapColTypeRelation.put(DmConstants.FieldType.VARCHAR, DmConstants.MyBatisColumnType.VARCHAR);
        mapColTypeRelation.put(DmConstants.FieldType.DATETIME, DmConstants.MyBatisColumnType.DATETIME);
        mapColTypeRelation.put(DmConstants.FieldType.BINARY, DmConstants.MyBatisColumnType.DATETIME);
        mapColTypeRelation.put(DmConstants.FieldType.TEXT, DmConstants.MyBatisColumnType.TEXT);
    }

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
                                     List<Object> values, int[] parIndex, Map<String, Object> paramValues) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        //每500生成一个in
        int total = values.size();
        int times = new Double(Math.ceil((0.0 + total) / 500)).intValue();
        StringBuilder sb = new StringBuilder();
        String key = null;
        int index = 0;

        for (int i = 0; i < times; i++) {
            sb.append(field).append(" in (");
            for (int j = 0; j < 500 && i * 500 + j < total; j++) {
                index = i * 500 + j;
                parIndex[0] = parIndex[0] + 1;
                key = getParamKey(parIndex[0]);
                sb.append("#{").append(key).append("},");
                paramValues.put(key, values.get(index));
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append(") or ");
        }
        return "(" + sb.substring(0, sb.length() - 3) + ")";
    }

    /**
     * 取得 对应的JDBC的类型
     * @param fieldType
     * @return
     */
    public static String getMyBatisColType(String fieldType) {
        return mapColTypeRelation.get(fieldType);
    }

    /**
     * 生成in的条件语句
     *
     * @param field
     * @param values
     * @param parIndex
     * @param paramValues
     * @return
     */
    public static String genNotInClause(String field,
                                        List<Object> values, int[] parIndex, Map<String, Object> paramValues) {
        if (values == null) {
            return "";
        }
        //每500生成一个in
        int total = values.size();
        int times = new Double(Math.ceil((0.0 + total) / 500)).intValue();
        StringBuilder sb = new StringBuilder();
        String key = null;
        int index = 0;

        for (int i = 0; i < times; i++) {
            sb.append(field).append(" not in (");
            for (int j = 0; j < 500 && i * 500 + j < total; j++) {
                index = i * 500 + j;
                parIndex[0] = parIndex[0] + 1;
                key = getParamKey(parIndex[0]);
                sb.append("#{").append(key).append("},");
                paramValues.put(key, values.get(index));
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append(") and ");
        }
        return "(" + sb.substring(0, sb.length() - 4) + ")";
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
