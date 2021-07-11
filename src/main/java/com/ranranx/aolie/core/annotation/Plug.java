
package com.ranranx.aolie.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 插件服务，没有增加功能,只是区分类别
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Plug {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
