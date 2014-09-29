package com.jiongsoft.cocit.entity.impl;

import java.util.Date;

import javax.persistence.Column;

public abstract class NameEntity extends BaseEntity {
	@Column(name = "_name", length = 255)
	protected String name;

	@Column(name = "_code", length = 64)
	protected String code;

	@Column(name = "_desc", length = 2000)
	protected String desc;

	protected Integer orderby;

	@Column(name = "_created", updatable = false)
	protected Date created;

	@Column(name = "_updated")
	protected Date updated;

	@Column(name = "_created_by", length = 32, updatable = false)
	protected String createdBy;

	@Column(name = "_updated_by", length = 32)
	protected String updatedBy;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
