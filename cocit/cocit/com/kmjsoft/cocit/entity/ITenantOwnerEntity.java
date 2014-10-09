package com.kmjsoft.cocit.entity;

import com.kmjsoft.cocit.entity.security.ITenant;

/**
 * “租户拥有者数据实体”接口：该接口的实现类所映射的实体表用来存储指定租户的基础业务数据。
 * 
 * @author Ji Yongshan
 * 
 */
public interface ITenantOwnerEntity extends IDataEntity {

	/**
	 * 租户GUID：逻辑外键，关联到{@link ITenant#getDataGuid()}字段。
	 * <p>
	 * 用来描述实体数据属于哪个租户（企业）所拥有？
	 * 
	 * @return
	 */
	public String getTenantOwnerGuid();

	public void setTenantOwnerGuid(String tenantGuid);

	/**
	 * 租户名称：冗余字段，关联到{@link ITenant#getName()}字段。
	 * 
	 * @return
	 */
	String getTenantOwnerName();

	void setTenantOwnerName(String tenantName);
}
