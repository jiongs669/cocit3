package com.kmjsoft.cocit.entity;

import javax.persistence.Column;

/**
 * 
 * “租户的数据实体”基类：该类的子类所映射的实体表用来存储指定租户的基础业务数据。
 * 
 * @author Ji Yongshan
 * 
 */
public abstract class TenantOwnerEntity extends DataEntity implements ITenantOwnerEntity {

	protected String tenantOwnerGuid;

	@Column(length = 128)
	protected String tenantOwnerName;

	public String getTenantOwnerGuid() {
		return tenantOwnerGuid;
	}

	public void setTenantOwnerGuid(String id) {
		this.tenantOwnerGuid = id;
	}

	@Override
	public String getTenantOwnerName() {
		return tenantOwnerName;
	}

	@Override
	public void setTenantOwnerName(String name) {
		tenantOwnerName = name;
	}

}
