package com.kmjsoft.cocit.entityengine.service;

import java.util.Date;

import com.kmjsoft.cocit.entity.INamedEntity;

/**
 * 实体服务类：服务于某个特定的实体对象
 * 
 * @author jiongs753
 * 
 */
interface EntityService<Entity extends INamedEntity> {

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
	 * 获取自定对象的创建时间。
	 * 
	 * @return
	 */
	Date getOperatedDate();

	/**
	 * 获取自定对象的创建用户（登录帐号）。
	 * 
	 * @return
	 */
	String getOperatedUser();

}
