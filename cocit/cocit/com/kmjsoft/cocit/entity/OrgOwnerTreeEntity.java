package com.kmjsoft.cocit.entity;

import javax.persistence.Column;

/**
 * 可命名的“组织机构的数据实体”基类：该类的子类所映射的数据表用来存储组织机构相关的基础业务数据。
 * 
 * @author Ji Yongshan
 * 
 */
public abstract class OrgOwnerTreeEntity extends TenantOwnerTreeEntity implements IOrgOwnerEntity {

	protected String orgOwnerGuid;

	@Column(length = 128)
	protected String orgOwnerName;

	public String getOrgOwnerGuid() {
		return orgOwnerGuid;
	}

	public void setOrgOwnerGuid(String orgGuid) {
		this.orgOwnerGuid = orgGuid;
	}

	public String getOrgOwnerName() {
		return orgOwnerName;
	}

	public void setOrgOwnerName(String orgName) {
		this.orgOwnerName = orgName;
	}

}
