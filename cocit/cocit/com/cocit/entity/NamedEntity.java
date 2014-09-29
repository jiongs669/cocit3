package com.cocit.entity;

import javax.persistence.Column;

import com.cocit.api.entity.INamedEntity;
import com.jiongsoft.cocit.util.StringUtil;

public abstract class NamedEntity extends DataEntity implements INamedEntity {

	@Column(length = 255)
	protected String name;

	@Column(length = 64)
	protected String code;

	protected Integer serialNumber;

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isDisabled() {
		return STATUS_CODE_DISABLED == this.statusCode;
	}

	public boolean isBuildin() {
		return STATUS_CODE_BUILDIN == this.statusCode;
	}

	public void setDisabled(Boolean disabled) {
		statusCode = STATUS_CODE_DISABLED;
	}

	public void setBuildin(Boolean buildin) {
		statusCode = STATUS_CODE_BUILDIN;
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
