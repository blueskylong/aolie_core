package com.ranranx.aolie.common;

import com.ranranx.aolie.exceptions.InvalidException;
import com.ranranx.aolie.handler.param.condition.Criteria;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.lang.Nullable;

import javax.servlet.ServletOutputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Author xxl
 * @Description 工具方法集合
 * @Date 2020/8/7 9:17
 * @Version V0.0.1
 **/
public class CommonUtils {
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
        Criteria criteria = new Criteria();
        criteria.andEqualTo("eqField", "value1")
                .andBetween("beField", "value2_1", "value2_2");
        ArrayList list = new ArrayList();
        list.add("inValue1");
        list.add("inValue2");
        criteria.andIn("inField", list);
        criteria.andCondition("1=1");
        Criteria subAndCriteria = criteria.createSubAndCriteria();
        subAndCriteria.andEqualTo("bizYear", "2019");
        subAndCriteria.orEqualTo("regCode", "22020");
        Map<String, Object> paramValue = new HashMap<>();
        criteria.andEqualTo("name", "xxl");
        System.out.println(criteria.getSqlWhere(paramValue, "a", 1, false));

    }
}
