package com.kmjsoft.cocit.entity.security;

import java.util.Date;

import com.kmjsoft.cocit.entity.INamedEntity;

/**
 * 租户：用来描述使用系统（{@link ISystem}）的企业级用户，如“食品药品管理局、各食品企业、各药品企业等”。
 * 
 * @author yongshan.ji
 * 
 */
public interface ITenant extends INamedEntity {
	/**
	 * 系统GUID：逻辑外键，关联到{@link ISystem#getDataGuid()}字段。
	 * <p>
	 * 平台包含了多套系统，该字段用来描述租户使用的是哪套系统？
	 * 
	 * @return
	 */
	String getSystemGuid();

	/**
	 * 租户可以使用不同的数据库作为其业务系统数据库。
	 * 
	 * @return
	 */
	String getDataSourceGuid();

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

}
