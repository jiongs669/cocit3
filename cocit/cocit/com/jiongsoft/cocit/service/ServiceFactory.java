package com.jiongsoft.cocit.service;

import java.io.Serializable;

/**
 * CoC组件工厂：用来“创建、缓存、管理”自定义组件。自定义组件包括“软件、模块、数据表、数据分组、数据字段、数据操作”等。
 * 
 * @author yongshan.ji
 * 
 */
public interface ServiceFactory {

	/**
	 * 根据网址中的域名获取缓存中的自定义软件对象。
	 * 
	 * @param domain
	 * @return
	 */
	SoftService getSoftService(String domain);

	/**
	 * 根据模块ID获取数据模块。
	 * 
	 * @param moduleID
	 *            模块ID
	 * @return CoC数据模块对象
	 */
	ModuleService getModule(Serializable moduleID);

	/**
	 * 获取与模块相关的数据表，如果模块ID为0，则直接获取数据表。该方法获取到的数据表对象不应包含子数据表对象。
	 * 
	 * @param moduleID
	 *            模块ID
	 * @param tableID
	 *            数据表ID
	 * @return CoC数据表对象
	 */
	TableService getTable(Serializable tableID);

	/**
	 * 
	 * @param module
	 * @return
	 */
	TableService getTable(ModuleService module);

	OperationService getOperation(TableService table, String opMode);

	OperationService getOperation(ModuleService module, String opMode);

}
