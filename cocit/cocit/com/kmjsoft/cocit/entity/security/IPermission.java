package com.kmjsoft.cocit.entity.security;

import java.util.Date;

import com.kmjsoft.cocit.entity.ITenantOwnerEntity;

/**
 * <b>权限许可：</b>即“允许”或“拒绝”主体对象在有效期内访问系统资源做什么事情？
 * <p>
 * 
 * @author yongshan.ji
 */
public interface IPermission extends ITenantOwnerEntity {

	/**
	 * 权限主体类型：0——用户（{@link IUser}）；1——角色（{@link IRole}）；2——组（{@link IGroup}）
	 * 
	 * @return
	 */
	byte getPrincipalType();

	/**
	 * 权限主体：逻辑外键
	 * <p>
	 * <UL>
	 * <LI>如果主体类型为用户（{@link IUser}），则该逻辑外键关联到{@link IUser#getDataGuid()}字段；
	 * <LI>如果主体类型为角色（{@link IRole}），则该逻辑外键关联到{@link IRole#getDataGuid()}字段；
	 * <LI>如果主体类型为组（{@link IGroup}），则该逻辑外键关联到{@link IGroup#getDataGuid()}字段；
	 * </UL>
	 * 
	 * @return
	 */
	String getPrincipalGuid();

	/**
	 * 功能模块：逻辑外键，关联到“{@link IModule#getDataGuid()}”字段。
	 * 
	 * @return
	 */
	String getModuleGuid();

	/**
	 * 操作权限：操作权限由模块操作GUID组成，多个操作之间用 | 分隔。
	 * 
	 * @return
	 */
	String getModuleActionsRule();

	/**
	 * 数据行权限：数据查询过滤表达式。
	 * 
	 * @return
	 */
	String getDataRowsRule();

	/**
	 * 字段列权限：多字段之间用 | 分隔。
	 * 
	 * @return
	 */
	String getDataColumnsRule();

	boolean isDenied();

	Date getExpiredFrom();

	Date getExpiredTo();
}
