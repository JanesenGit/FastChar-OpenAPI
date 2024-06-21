package com.fastchar.openapi.annotation;

import java.lang.annotation.*;


/**
 * 接口响应内容
 */
@Repeatable(AFastAPIResponseRepeatable.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AFastAPIResponse {

    /**
     * 响应的内容
     * @return string
     */
    String value() default "";


    /**
     * 响应内容的类型
     * @return string
     */
    String contentType() default "application/json";

    /**
     * 响应的编码
     * @return int
     */
    int statusCode() default 200;

    /**
     * 响应描述
     * @return string
     */
    String desc() default "";


    /**
     * 响应的示例
     * @return String[]
     */
    String[] examples() default "";

}
