package com.jiongsoft.cocit.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段注释：使用该注释自动将CoC实体类中的字段转换成CoC业务表字段。
 * 
 * @author jiongsoft
 * 
 */
@Target(value = { ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CocField {

	public long id() default 0;

	public String name() default "";

	public String code() default "";

	public String property() default "";

	public String desc() default "";

	public String type() default "";

	public int order() default 0;

	public int gridOrder() default 0;

	public boolean isTransient() default false;

	public boolean gridField() default true;

	public String fkTable() default "";

	// 子系统字段，引用的
	public String refrenceFields() default "";

	public boolean isFkChild() default false;

	public boolean password() default false;

	public String options() default "";

	public String regexpMask() default "";

	public String pattern() default "";

	public int gridWidth() default 0;

	public String mode() default "";

	public String uiTemplate() default "";

	public String parent() default "";

	public boolean disabled() default false;

	public boolean disabledNavi() default false;

	String cascadeMode() default "";

	public boolean groupBy() default false;

	String uploadType() default "";

	boolean tostring() default false;

	String columnDefinition() default "";

	String defalutValue() default "";

	int precision() default 0;

	boolean combobox() default false;

	boolean privacy() default false;

	public CocField2[] children() default {};
}
