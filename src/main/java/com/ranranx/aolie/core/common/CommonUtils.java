package com.ranranx.aolie.core.common;

import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.lang.Nullable;

import javax.persistence.Table;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xxl
 * @Description 工具方法集合
 * @Date 2020/8/7 9:17
 * @Version V0.0.1
 **/
public class CommonUtils {

    private static Pattern UNDERLINE_PATTEN = Pattern.compile("_[a-z]");

    /**
     * 字符串
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(@Nullable Object str) {
        return (str == null || "".equals(str.toString()));
    }

    /**
     * 非空字串
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    /**
     * 把有权重的排序
     *
     * @param lstOrder
     */
    public static void sortOrder(List<? extends Ordered> lstOrder) {
        if (lstOrder == null || lstOrder.isEmpty()) {
            return;
        }
        lstOrder.sort(Comparator.comparingInt(Ordered::getOrder));
    }

    /**
     * 生成Key
     *
     * @param key
     * @param version
     * @return
     */
    public static String makeKey(String key, String version) {
        return key + "__" + version;
    }

    public static <T> List<T> convertToObject(List<Map<String, Object>> lst, Class<T> clazz) {
        List<T> lstResult = new ArrayList<>();
        try {
            if (lst != null && !lst.isEmpty()) {
                for (Map<String, Object> map : lst) {
                    lstResult.add(populateBean(clazz, map));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidException("生成对象失败:" + e.getMessage());
        }
        return lstResult;
    }

    public static <T> List<T> convertCamelAndToObject(List<Map<String, Object>> lst, Class<T> clazz) {
        List<T> lstResult = new ArrayList<>();
        try {
            if (lst != null && !lst.isEmpty()) {
                for (Map<String, Object> map : lst) {
                    map = convertToCamel(map);
                    lstResult.add(populateBean(clazz, map));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidException("生成对象失败:" + e.getMessage());
        }
        return lstResult;
    }

    /**
     * 驼峰转下划线
     */
    public static String convertToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
            }
            //统一都转小写
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    public static List<Map<String, Object>> toMap(List<?> lstObject) {
        List<Map<String, Object>> lstResult = new ArrayList<>();
        for (Object obj : lstObject) {
            lstResult.add(toMap(obj, false));
        }
        return lstResult;
    }

    public static List<Map<String, Object>> toMapAndConvertToUnderLine(List<?> lstObject) {
        List<Map<String, Object>> lstResult = new ArrayList<>();
        for (Object obj : lstObject) {
            lstResult.add(toMap(obj, true));
        }
        return lstResult;
    }

    public static Map<String, Object> toMap(Object obj, boolean isConvertToUnderLine) {
        return toMap(obj, isConvertToUnderLine, false);
    }

    public static Map<String, Object> toMap(Object obj, boolean isConvertToUnderLine, boolean isSelective) {
        try {
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> describe = PropertyUtils.describe(obj);
            if (isConvertToUnderLine) {
                Iterator<Map.Entry<String, Object>> it = describe.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> next = it.next();
                    if (isSelective && CommonUtils.isEmpty(next.getValue())) {
                        continue;
                    }
                    result.put(CommonUtils.convertToUnderline(next.getKey()), next.getValue());
                }
            } else {
                result = isSelective ? removeEmptyValues(describe) : describe;
            }
            return result;
        } catch (Exception e) {
            throw new InvalidException(e.getMessage());
        }
    }


    private static Map<String, Object> removeEmptyValues(Map<String, Object> map) {

        if (map == null || map.isEmpty()) {
            return map;
        }
        Map<String, Object> result = new HashMap();
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            if (CommonUtils.isNotEmpty(next.getValue())) {
                result.put(next.getKey(), next.getValue());
            }
        }
        return result;
    }


    /**
     * Map的KEY由下划线转成驼峰
     *
     * @param map
     * @return
     */
    public static Map<String, Object> convertToCamel(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return map;
        }
        Map<String, Object> mapResult = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> next = it.next();
            mapResult.put(toCamelStr(next.getKey()), next.getValue());
        }
        return mapResult;
    }

    /**
     * 将下划线风格替换为驼峰风格
     */
    public static String toCamelStr(String str) {
        Matcher matcher = UNDERLINE_PATTEN.matcher(str);
        StringBuilder builder = new StringBuilder(str);
        for (int i = 0; matcher.find(); i++) {
            builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
        }
        if (Character.isUpperCase(builder.charAt(0))) {
            builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
        }
        return builder.toString();
    }


    public static String stuff(String oraStr, char c, int totalLen) {
        if (oraStr == null) {
            oraStr = "";
        }
        if (oraStr.length() > totalLen) {
            return oraStr;
        }
        int addNum = totalLen - oraStr.length();
        for (int i = 0; i < addNum; i++) {
            oraStr = c + oraStr;
        }
        return oraStr;
    }

    public static String appendChar(String oraStr, char c, int totalLen) {
        if (oraStr == null) {
            oraStr = "";
        }
        if (oraStr.length() > totalLen) {
            return oraStr;
        }
        int addNum = totalLen - oraStr.length();
        for (int i = 0; i < addNum; i++) {
            oraStr = oraStr + c;
        }
        return oraStr;
    }

    /**
     * 生成单个Bean
     *
     * @param clazz
     * @param map
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public static <T> T populateBean(Class<T> clazz, Map<String, Object> map)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        T obj = clazz.newInstance();
        BeanUtils.populate(obj, map);
        return obj;

    }

    public static void main(String[] args) {
//        Criteria criteria = new Criteria();
//        criteria.andEqualTo("eqField", "value1")
//                .andBetween("beField", "value2_1", "value2_2");
//        ArrayList list = new ArrayList();
//        list.add("inValue1");
//        list.add("inValue2");
//        criteria.andIn("inField", list);
//        criteria.andCondition("1=1");
//        Criteria subAndCriteria = criteria.createSubAndCriteria();
//        subAndCriteria.andEqualTo("bizYear", "2019");
//        subAndCriteria.orEqualTo("regCode", "22020");
//        Map<String, Object> paramValue = new HashMap<>();
//        criteria.andEqualTo("name", "xxl");
//        System.out.println(criteria.getSqlWhere(paramValue, "a", 1, false));

        Map<String, Object> mapResult = new HashMap<>();
        List<Object> lstValue = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            lstValue.add(i);
        }
        String sql = SqlTools.genInClause("field1", lstValue, 1, mapResult);
        System.out.println(sql);
    }

    /**
     * 取得类注解上的表名
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null || CommonUtils.isEmpty(table.name())) {
            throw new InvalidParamException("查询操作没有找到指定的表信息:" + clazz.getName());
        }
        return table.name();
    }

    public static String getStringField(Map<String, Object> map, String fieldName) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Object obj = map.get(fieldName);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Integer getIntegerField(Map<String, Object> map, String fieldName) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Object obj = map.get(fieldName);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        return Integer.parseInt(obj.toString());
    }
}
