
package com.ranranx.aolie.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 公式或过滤条件的翻译器,一个翻译器,一般只负责一个类型的翻译
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface FormulaElementTranslator {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
