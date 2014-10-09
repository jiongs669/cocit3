package com.kmjsoft.cocit.entity;

import javax.persistence.Column;

/**
 * 
 * “租户拥有的树形实体”基类：
 * 
 * @author Ji Yongshan
 * 
 */
public abstract class TenantOwnerTreeEntity extends TreeEntity implements ITenantOwnerEntity {

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
