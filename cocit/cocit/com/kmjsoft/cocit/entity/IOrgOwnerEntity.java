package com.kmjsoft.cocit.entity;

import com.kmjsoft.cocit.entity.org.IOrg;

/**
 * “组织机构的数据实体”接口：该接口的实现类所映射的数据表用来存储组织机构相关的基础业务数据。
 * 
 * @author Ji Yongshan
 * 
 */
public interface IOrgOwnerEntity extends ITenantOwnerEntity {

	/**
	 * 组织机构GUID：逻辑外键，关联到{@link IOrg#getDataGuid()}字段。
	 * <p>
	 * 用来描述实体数据属于租户（企业）内部的哪个部门、事业部所拥有？
	 * 
	 * @return
	 */
	public String getOrgOwnerGuid();

	public void setOrgOwnerGuid(String orgGuid);

	/**
	 * 冗余字段：机构名称
	 * 
	 * @return
	 */
	String getOrgOwnerName();

	void setOrgOwnerName(String orgName);
}
