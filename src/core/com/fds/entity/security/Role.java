package com.fds.entity.security;

import com.fds.entity.BaseEntity;

public class Role extends BaseEntity implements IPrincipal {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
