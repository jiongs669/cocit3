package com.kmjsoft.cocit.entity;

import javax.persistence.Column;

import com.kmjsoft.cocit.util.StringUtil;

public abstract class BaseNamedEntity extends BaseEntity implements INamedEntity, ITenantKnown {

	@Column(length = 255)
	protected String name;

	protected Integer serialNumber;

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		if (StringUtil.isNil(name))
			return super.toString();

		return name;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer orderBy) {
		this.serialNumber = orderBy;
	}
}
