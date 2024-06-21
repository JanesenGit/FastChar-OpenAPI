package com.fastchar.openapi.annotation;


import java.lang.annotation.*;


/**
 * 接口分组
 */
@Repeatable(AFastAPIGroupRepeatable.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AFastAPIGroup {

    /**
     * 分组名称
     * @return string
     */
    String value();

    /**
     * 排序索引
     * @return int
     */
    int index() default 0;

}
