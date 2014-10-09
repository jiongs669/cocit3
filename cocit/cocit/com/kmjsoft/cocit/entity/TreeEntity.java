package com.kmjsoft.cocit.entity;

import javax.persistence.Column;

/**
 * “树形结构的数据实体”基类：
 * 
 * @author Ji Yongshan
 * 
 */
public abstract class TreeEntity extends NamedEntity implements ITreeEntity {

	protected String parentGuid;

	@Column(length = 128)
	protected String parentName;

	public String getParentGuid() {
		return parentGuid;
	}

	public void setParentGuid(String parentGuid) {
		this.parentGuid = parentGuid;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}
