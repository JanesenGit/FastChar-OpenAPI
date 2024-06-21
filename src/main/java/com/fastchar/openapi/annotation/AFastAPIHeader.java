package com.fastchar.openapi.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口请求的头部参数要求
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AFastAPIHeader {
    /**
     * 参数名称
     * @return string
     */
    String name();

    /**
     * 参数类型
     *
     * @return string
     */
    String type() default "string";


    /**
     * 参数描述
     * @return string
     */
    String desc();


    /**
     * 参数是否必传
     * @return boolean
     */
    boolean required() default false;


    /**
     * 案例值
     * @return string
     */
    String example() default "";

}
