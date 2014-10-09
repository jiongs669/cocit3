package com.kmjsoft.cocit.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import com.jiongsoft.cocit.lang.Cls;

/**
 * “数据实体”基类：该类的所有子类类即为实体类，实体类将被ORM框架映射到数据库表，其实体对象都将被映射到数据库表记录。
 * <p>
 * 
 * @author yongshan.ji
 */
public abstract class DataEntity implements IDataEntity {
	@Id
	@Column(name = "_id")
	protected Long id;

	protected String dataGuid;

	@Column(name = "_data_version")
	protected Integer dataVersion;

	protected Date createdDate;

	@Column(length = 64)
	protected String createdUser;

	@Column(length = 64)
	protected String createdIP;

	protected Date updatedDate;

	@Column(length = 64)
	protected String updatedUser;

	@Column(length = 64)
	protected String updatedIP;

	protected int statusCode;

	public boolean isBuildin() {
		return STATUS_CODE_BUILDIN == this.statusCode;
	}

	public boolean isDisabled() {
		return STATUS_CODE_DISABLED == this.statusCode;
	}

	public boolean isDeleted() {
		return STATUS_CODE_DELETED == this.statusCode;
	}

	public boolean isArchived() {
		return STATUS_CODE_ARCHIVED == this.statusCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public String getDataGuid() {
		return dataGuid;
	}

	public void setDataGuid(String guid) {
		this.dataGuid = guid;
	}

	@Override
	public Integer getDataVersion() {
		return dataVersion;
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public void setDataVersion(Integer id) {
		this.dataVersion = id;
	}

	@Override
	public void setUpdatedDate(Date date) {
		this.updatedDate = date;
	}

	@Override
	public void setStatusCode(int code) {
		statusCode = code;
	}

	public void setUpdatedUser(String operatedUser) {
		this.updatedUser = operatedUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getCreatedIP() {
		return createdIP;
	}

	public void setCreatedIP(String createdIP) {
		this.createdIP = createdIP;
	}

	public String getUpdatedIP() {
		return updatedIP;
	}

	public void setUpdatedIP(String updatedIP) {
		this.updatedIP = updatedIP;
	}

	// ***Other
	@Override
	public int hashCode() {
		if (id == null) {
			return super.hashCode();
		}
		return 37 * 17 + id.hashCode();
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (!Cls.getType(getClass()).equals(Cls.getType(that.getClass()))) {
			return false;
		}

		DataEntity thatEntity = (DataEntity) that;
		if (id == null || id == 0 || thatEntity.id == null || thatEntity.id == 0) {
			return this == that;
		}

		return thatEntity.id.equals(id);
	}

	@Override
	public String toString() {
		if (id == null || id == 0)
			return Cls.getType(getClass()).getSimpleName() + "@" + Integer.toHexString(hashCode());
		else
			return Cls.getType(getClass()).getSimpleName() + "#" + id;
	}

}
