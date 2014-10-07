package com.kmjsoft.cocit.entity;

/**
 * “租户的数据实体”接口：该接口的实现类所映射的实体表用来存储指定租户的基础业务数据。
 * 
 * @author Ji Yongshan
 * 
 */
public interface ITenantKnown extends IDataEntity {

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
