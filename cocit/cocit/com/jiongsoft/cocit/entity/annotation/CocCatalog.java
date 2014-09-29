package com.jiongsoft.cocit.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务表分类注释：在package上使用该注释自动将包里面的实体类对应的业务表归类到该注释描述的业务表分类中。
 * 
 * @author jiongsoft
 * 
 */
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CocCatalog {

	public long id() default 0;

	public String name() default "";

	public String code() default "";

	public String desc() default "";

	public int orderby() default 1000;

}
