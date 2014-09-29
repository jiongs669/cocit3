package com.cocit.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.cocit.api.entity.IDataEntity;
import com.cocit.lang.Cls;

/**
 * ID数据实体： 有唯一标识(ID)的数据实体
 * <p>
 * 
 * @author yongshan.ji
 */
public class DataEntity implements IDataEntity {
	@Id
	@Column(name = "_id")
	@GeneratedValue(generator = "SftIdGen", strategy = GenerationType.TABLE)
	@TableGenerator(name = "SftIdGen", table = "DEMSY_00000000", pkColumnName = "id_key", valueColumnName = "next_hi", allocationSize = 1, initialValue = 20)
	protected Long id;

	protected Integer dataVersion;

	protected String tenantGuid;

	protected String tenantName;

	protected Date operatedDate;

	@Column(length = 32)
	protected String operatedUser;

	@Column(updatable = false)
	protected String dataGuid;

	protected int statusCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTenantGuid() {
		return tenantGuid;
	}

	public void setTenantGuid(String id) {
		this.tenantGuid = id;
	}

	public Date getOperatedDate() {
		return operatedDate;
	}

	public String getOperatedUser() {
		return operatedUser;
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
	public String getTenantName() {
		return tenantName;
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
	public void setTenantName(String name) {
		tenantName = name;
	}

	@Override
	public void setOperatedDate(Date date) {
		this.operatedDate = date;
	}

	@Override
	public void setStatusCode(int code) {
		statusCode = code;
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

	public void setOperatedUser(String operatedUser) {
		this.operatedUser = operatedUser;
	}

}
