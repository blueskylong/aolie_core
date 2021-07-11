package com.ranranx.aolie.core.annotation;

import java.lang.annotation.*;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/6/10 0010 21:43
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomRightPermission {
    String value();
}
