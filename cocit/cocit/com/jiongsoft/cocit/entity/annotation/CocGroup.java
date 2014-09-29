package com.jiongsoft.cocit.entity.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段分组注释：使用该注释自动对业务字段进行分组。
 * 
 * @author jiongsoft
 * 
 */
@Target(value = {})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CocGroup {

	public long id() default 0;

	public String name() default "";

	public String code() default "";

	public String desc() default "";

	public int order() default 0;

	public CocField[] fields() default {};
}
