package com.cocit.api.entity;

import java.util.Date;

/**
 * 数据实体接口：该接口的所有实现类都将被映射到数据库表，其实体对象都将被映射到数据库表记录。
 * 
 * @author Ji Yongshan
 * 
 */
public interface IDataEntity {
	public static final int STATUS_CODE_BUILDIN = -1;

	public static final int STATUS_CODE_DISABLED = -9;

	public Long getId();

	public void setId(Long id);

	public String getDataGuid();

	public void setDataGuid(String uid);

	public Integer getDataVersion();

	public void setDataVersion(Integer id);

	public String getTenantGuid();

	public void setTenantGuid(String id);

	/**
	 * 冗余字段：租户名称
	 * 
	 * @return
	 */
	public String getTenantName();

	public void setTenantName(String name);

	public Date getOperatedDate();

	public void setOperatedDate(Date date);

	public String getOperatedUser();

	public void setOperatedUser(String user);

	/**
	 * 数据状态码：用来描述数据的当前状态，主要用来描述流程处理过程中的数据状态。
	 * <p>
	 * <UL>
	 * <LI>-1——系统预置数据（预置数据不允许编辑、删除等，只用许查看）；
	 * <LI>-9——数据已被禁用；
	 * <LI>0——新增；
	 * <LI>1——已被修改；
	 * <LI>99——已被删除（非永久删除）
	 * </UL>
	 * 
	 * @return
	 */
	public int getStatusCode();

	public void setStatusCode(int code);
}
