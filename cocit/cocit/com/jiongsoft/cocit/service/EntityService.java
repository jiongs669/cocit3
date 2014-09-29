package com.jiongsoft.cocit.service;

import java.util.Date;
import java.util.Properties;

import com.jiongsoft.cocit.entity.CoEntity;

/**
 * 实体服务类：服务于某个特定的实体对象
 * 
 * @author jiongs753
 * 
 */
interface EntityService<Entity extends CoEntity> {

	Entity getEntity();

	/**
	 * 获取自定义对象ID。
	 * 
	 * @return
	 */
	Long getID();

	/**
	 * 获取自定义对象名称或标题。
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 判断自定义对象是否已被停用？
	 * 
	 * @return
	 */
	boolean isDisabled();

	/**
	 * 获取自定对象的描述信息。
	 * 
	 * @return
	 */
	String getInfo();

	/**
	 * 获取自定对象的创建时间。
	 * 
	 * @return
	 */
	Date getCreatedDate();

	/**
	 * 获取自定对象的创建用户（登录帐号）。
	 * 
	 * @return
	 */
	String getCreatedUser();

	/**
	 * 获取自定义对象的最近修改时间。
	 * 
	 * @return
	 */
	Date getLatestModifiedDate();

	/**
	 * 获取自定义对象的修改用户（登录帐号）。
	 * 
	 * @return
	 */
	String getLatestModifiedUser();

	/**
	 * 获取对象顺序
	 * 
	 * @return
	 */
	int getSequence();

	/**
	 * 获取自定义对象的扩展属性，即未在API中定的的其他属性。
	 * 
	 * @param propName
	 * @return
	 */
	public <T> T get(String propName, T defaultReturn);

	public Properties getExtProps();
}
