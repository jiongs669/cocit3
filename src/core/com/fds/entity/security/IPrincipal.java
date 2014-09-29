package com.fds.entity.security;

/**
 * 授权主体：即可以被授予权限的主体对象，包括角色、用户组、用户、租户等。
 * 
 * @author Ji Yongshan
 * 
 */
public interface IPrincipal {

	public static final int TYPE_ROLE = 1;

	public static final int TYPE_GROUP = 2;

	public static final int TYPE_USER = 3;

	public static final int TYPE_TENANCY = 9;

	public Long getId();

	public String getName();
}
