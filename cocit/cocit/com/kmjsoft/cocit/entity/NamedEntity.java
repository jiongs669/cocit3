package com.kmjsoft.cocit.entity;

import javax.persistence.Column;

import com.jiongsoft.cocit.lang.Cls;

public abstract class NamedEntity extends DataEntity implements INamedEntity {

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

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer orderBy) {
		this.serialNumber = orderBy;
	}

	@Override
	public String toString() {
		if (id == null || id == 0)
			return Cls.getType(getClass()).getSimpleName() + "@" + Integer.toHexString(hashCode()) + "@" + name;
		else
			return Cls.getType(getClass()).getSimpleName() + "#" + id + "#" + name;
	}
}
