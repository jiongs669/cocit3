package com.cocit.api.security;

import java.util.Date;

import com.cocit.config.IDatasourceConfig;

/**
 * 租户：使用软件的企业级用户。
 * 
 * @author yongshan.ji
 * 
 */
public interface ITenant extends IUser {
	/**
	 * 
	 * @return
	 */
	ISoftEnv getSoftEnv();

	/**
	 * 判断应用系统是否被锁定， 锁定的应用系统不能被访问。
	 * 
	 * @return
	 */
	boolean isLocked();

	/**
	 * 获取应用系统的有效起始时间
	 * 
	 * @return
	 */
	Date getExpiredFrom();

	/**
	 * 获取应用系统的有效截止时间
	 * 
	 * @return
	 */
	Date getExpiredTo();

	/**
	 * 获取应用系统域名， 可以通过域名判断访问的应用系统。
	 * 
	 * @return
	 */
	String getDomain();

	IDatasourceConfig getDataSource();

}
