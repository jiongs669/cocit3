package com.cocit.api.entity;

/**
 * “租户的实体”接口：实现该接口的所有实体类，其数据对象只属于指定的租户。
 * 
 * @author Ji Yongshan
 * 
 */
public interface IEntityOfTenant {

	/**
	 * 租户GUID：数据所属的租户GUID。是业务数据表的一个“逻辑外键”字段，该“逻辑外键”关联到“租户表”的“物理主键(dataGuid)”。
	 * 
	 * @return
	 */
	public String getTenantGuid();

	/**
	 * 
	 * 租户GUID：数据所属的租户GUID。是业务数据表的一个“逻辑外键”字段，该“逻辑外键”关联到“租户表”的“物理主键(dataGuid)”。
	 * 
	 * @param id
	 */
	public void setTenantGuid(String id);

}
