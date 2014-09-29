package com.kmetop.demsy.security.impl;

import java.util.Date;

import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.service.SecurityManager;

/**
 * 权限：权限实体被解析成该权限然后缓存。
 * 
 * @author jiongsoft
 * 
 */
class PermissionItem {
	/**
	 * 权限条目有效起始日期
	 */
	Date expiredFrom;

	/**
	 * 权限条目有效截止日期
	 */
	Date expiredTo;

	/**
	 * 权限类型：拒绝还是允许？true 表示拒绝，false 表示允许。
	 */
	boolean denied;

	/**
	 * 用户角色ID：与“用户模块”和“用户过滤器”互斥，用来表示当前授权项对哪些角色的用户有效，该项主要用于动态授权。可选值包括：
	 * <UL>
	 * <LI>{@link SecurityManager#ROLE_ANONYMOUS}
	 * <LI>{@link SecurityManager#ROLE_LOGIN_USER}
	 * <LI>{@link SecurityManager#ROLE_ADMIN_USER}
	 * <LI>{@link SecurityManager#ROLE_ADMIN_ROOT}
	 * <LI>{@link SecurityManager#ROLE_DP_SUPPORT}
	 * </UL>
	 */
	byte userRole = -1;

	/**
	 * 用户模块：与“用户角色ID”互斥，用来标识用户类型，如：后台管理员、网站注册会员等。
	 * 
	 * @deprecated COC平台不再使用该属性
	 */
	long userModuleID;

	/**
	 * 用户实体表ID：与“用户角色{@link #userRole}”互斥，用来表示用户类型，如：后台管理员、网站注册会员等。
	 */
	long userTableID;

	/**
	 * 用户群体：表示该权限被授予哪些“用户”？
	 */
	CndExpr userFilter;

	/**
	 * 模块权限：表示“用户群体”可以访问哪个模块？
	 * <p>
	 * 0：表示所有模块
	 */
	long moduleID;

	/**
	 * 表权限：表示“用户群体”可以访问模块中的那张表？
	 * <p>
	 * 0：表示所有数据表
	 */
	long tableID;

	/**
	 * 操作权限：表示“用户群体”可以对数据表执行那些操作？
	 * <P>
	 * 多操作之间用逗号“,”分隔。
	 */
	String operationMode;

	/**
	 * 数据权限：一个查询表达式，表示“用户群体”可以访问模块中的哪些数据？
	 */
	CndExpr dataFilter;

	// String[] actions;

}
