package com.ranranx.aolie.common;

import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
}
