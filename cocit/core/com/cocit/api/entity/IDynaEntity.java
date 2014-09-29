package com.cocit.api.entity;

import java.util.Properties;

/**
 * 动态实体接口：实现该接口的所有实体类支持支持动态属性，其实体对象支持动态字段值。
 * 
 * @author yongshan.ji
 */
public interface IDynaEntity {

	Object get(String extField);

	void set(String extField, Object fieldValue);

	boolean is(byte index);

	public Properties getProperties();
}
