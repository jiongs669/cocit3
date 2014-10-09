package com.kmjsoft.cocit.entity;

/**
 * 命名实体：即可以被命名的，有名字的实体。
 * 
 * @author yongshan.ji
 */
public interface INamedEntity extends IDataEntity {

	String getName();
	
	void setName(String name);

	/**
	 * 序号：
	 * 
	 * @return
	 */
	Integer getSerialNumber();

	void setSerialNumber(Integer serialNumber);
}
