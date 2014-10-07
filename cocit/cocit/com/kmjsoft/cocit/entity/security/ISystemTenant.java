package com.kmjsoft.cocit.entity.security;

import java.util.Date;

import com.jiongsoft.cocit.config.IDataSourceConfig;

/**
 * 系统租户：用来描述使用系统（{@link ISystem}）的企业级用户，如“食品药品管理局、各食品企业、各药品企业等”。
 * 
 * @author yongshan.ji
 * 
 */
public interface ISystemTenant {
	/**
	 * 平台包含了多套系统，该字段指定租户使用的事是哪套系统？
	 * 
	 * @return
	 */
	String getSystemGuid();

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

	IDataSourceConfig getDataSource();

}
