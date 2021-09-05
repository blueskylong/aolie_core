package com.ranranx.aolie.core.common;

import com.ranranx.aolie.core.exceptions.IllegalOperatorException;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.lang.Nullable;

import javax.persistence.Table;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xxl
 * 工具方法集合
 * @version V0.0.1
 * @date 2020/8/7 9:17
 **/
public class CommonUtils {
    private static Map<String, String> tableNameCache = new HashMap<>();
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)(E\\d+)?");

    private static Pattern UNDERLINE_PATTEN = Pattern.compile("_[a-z]");
    //用于存储系统级参数，包含启动参数，数据库连接可以用此处参数覆盖
    private static final Map<String, String> mapGlobalParam = new HashMap<>();
    private static final Short TRUE_SHORT = new Short((short) 1);


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

        List lstResult = new ArrayList<>();
        if (lst == null || lst.isEmpty()) {
            return lstResult;
        }
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
        lst.clear();
        lst.addAll(lstResult);
        return (List<T>) lst;
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

    /**
     * 深度克隆对象
     *
     * @param source
     * @param <T>
     * @return
     */
    public static <T> T deepClone(T source) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(source);
            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            new IllegalOperatorException("复制出错" + e.getStackTrace());
            e.printStackTrace();
            return null;
        }
    }


    public static Map<String, Object> removeEmptyValues(Map<String, Object> map) {

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

    }

    /**
     * 取得类注解上的表名
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        String className = clazz.getName();
        if (tableNameCache.containsKey(className)) {
            return tableNameCache.get(className);
        }
        Table table = clazz.getAnnotation(Table.class);
        if (table == null || CommonUtils.isEmpty(table.name())) {
            throw new InvalidParamException("查询操作没有找到指定的表信息:" + clazz.getName());
        }
        tableNameCache.put(className, table.name());
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

    public static String getStringField(Map<String, Object> map, String fieldName, String defaultValue) {
        if (map == null || map.isEmpty()) {
            return defaultValue;
        }
        Object obj = map.get(fieldName);
        if (obj == null) {
            return defaultValue;
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

    public static Long getLongField(Map<String, Object> map, String fieldName) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Object obj = map.get(fieldName);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        return Long.parseLong(obj.toString());
    }

    public static boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        return NUMBER_PATTERN.matcher(str).matches();
    }

    public static boolean isNumber(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Number) {
            return true;
        }
        return isNumber(obj.toString());
    }

    /**
     * 结合字串
     *
     * @param arr
     * @param splitChar
     * @return
     */
    public static String join(String[] arr, char splitChar) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : arr) {
            sb.append(str).append(splitChar);
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 分类增加子元素
     *
     * @param map
     * @param key
     * @param subElement
     */
    public static <T, S> void addMapListValue(Map<S, List<T>> map, S key, T subElement) {
        List<T> lstEle = map.get(key);
        if (lstEle == null) {
            lstEle = new ArrayList<>();
            map.put(key, lstEle);
        }
        lstEle.add(subElement);
    }

    public static List toList(Set values) {
        List lst = new ArrayList();
        lst.addAll(values);
        return lst;
    }

    public static void addGlobalParam(String name, String value) {
        mapGlobalParam.put(name, value);
    }

    public static String getGlobalParam(String name) {
        return mapGlobalParam.get(name);
    }

    public static Map<String, String> getAllParams() {
        Map<String, String> map = new HashMap<>();
        map.putAll(mapGlobalParam);
        return map;
    }

    /**
     * 根据字段名取得字段值，需要存在getter方法
     *
     * @param obj
     * @param field
     * @return
     */
    public static Object getObjectValue(Object obj, String field) {
        if (obj instanceof Map) {
            return ((Map) obj).get(field);
        }
        String fieldName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
        try {
            Method method = obj.getClass().getMethod(fieldName, null);
            if (method != null) {
                return method.invoke(obj, null);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 转换成双精度数值
     *
     * @param obj
     * @return
     */
    public static double toDouble(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        return Double.parseDouble(obj.toString());
    }

    public static boolean isTrue(Short value) {
        if (value == null) {
            return false;
        }
        return TRUE_SHORT.equals(value);
    }
}
