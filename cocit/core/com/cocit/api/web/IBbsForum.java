package com.cocit.api.web;

public interface IBbsForum {
	public static final String SYS_CODE = "LybbsDb";

	public Number getId();

	public byte getCheckPostStatus();

	public String getAdminUsers();
}
