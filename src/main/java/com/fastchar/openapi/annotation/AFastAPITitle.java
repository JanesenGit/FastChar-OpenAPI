package com.fastchar.openapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口标题
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AFastAPITitle {

    /**
     * 接口名称
     * @return string
     */
    String value();

    /**
     * 接口描述
     * @return string
     */
    String desc() default "";

}
