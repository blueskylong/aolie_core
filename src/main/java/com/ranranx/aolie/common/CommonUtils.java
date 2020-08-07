package com.ranranx.aolie.common;

import org.springframework.lang.Nullable;

/**
 * @Author xxl
 * @Description 工具方法集合
 * @Date 2020/8/7 9:17
 * @Version V0.0.1
 **/
public class CommonUtils {
    public static boolean isEmptyString(@Nullable Object str) {
        return (str == null || "".equals(str));
    }
}
