
package com.ranranx.aolie.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 数据验证器注解,没有增加功能,只是区分类别
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Validator {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
