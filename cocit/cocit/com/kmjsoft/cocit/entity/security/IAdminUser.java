package com.kmjsoft.cocit.entity.security;

public interface IAdminUser extends IUser {
	IRole getRole();

	IGroup getGroup();

	String getLatestUrl();

	void setLatestUrl(String url);
}
