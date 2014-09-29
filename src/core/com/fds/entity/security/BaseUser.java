package com.fds.entity.security;

import com.fds.entity.BaseEntity;

/**
 * 用户基础类：
 * 
 * @author Ji Yongshan
 * 
 */
public abstract class BaseUser extends BaseEntity implements IPrincipal {
	private String code;

	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
