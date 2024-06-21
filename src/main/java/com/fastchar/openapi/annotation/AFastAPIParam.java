package com.fastchar.openapi.annotation;


import java.lang.annotation.*;


/**
 * 接口参数
 */
@Repeatable(AFastAPIParamRepeatable.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AFastAPIParam {

    /**
     * 参数内容，用于快速编辑参数，格式：name type desc required example 注意使用空格分割
     * @return string
     */
    String value() default "";

    /**
     * 参数名称
     * @return string
     */
    String name() default "";

    /**
     * 参数类型
     *
     * @return string
     */
    String type() default "string";


    /**
     * 参数描述
     *
     * @return string
     */
    String desc() default "";


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

    /**
     * 枚举值
     *
     * @return 枚举的class
     */
    Class<?> enumClass() default Object.class;

    /**
     * 指定枚举值
     *
     * @return String[]
     */
    String[] enums() default {};

}
