package com.jiongsoft.cocit.service;

import com.jiongsoft.cocit.entity.SoftEntity;
import com.jiongsoft.cocit.orm.Orm;
import com.jiongsoft.cocit.sms.SmsClient;

/**
 * 软件服务对象：为平台中的每个软件提供一对一的服务。
 * <UL>
 * <LI>代表一个运行时的自定义软件对象，通常由定义在数据库中的数据实体解析而来；
 * <LI>“Componentization of custom software[it]”，“it = software”；
 * <LI>CoC软件是在Cocit平台上定制出来的软件；
 * <LI>全称“组件化自定义软件”；简称“SoftService”、“Cocit软件”、“CoC软件”、“平台软件”等；
 * <LI>Cocit平台中可以定制并运行多套软件，该接口的实例即代表一套软件；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public interface SoftService extends EntityService<SoftEntity> {
	public Orm getOrm();

	public String getCode();

	/**
	 * 获取短信客户端API接口，用于收发短信。
	 * 
	 * @return 短信客户端API接口
	 */
	public SmsClient getSmsClient();

	/**
	 * 获取软件配置项
	 * 
	 * @param configKey
	 *            配置项KEY
	 * @param defaultReturn
	 *            默认返回值
	 * @return 配置项数据，如果指定的配置项不存在，则返回指定默认值。
	 */
	public <T> T getConfig(String configKey, T defaultReturn);

	/**
	 * 获取实体管理器
	 * 
	 * @param moduleID
	 * @param tableID
	 * @return
	 */
	public EntityManager getEntityManager(ModuleService module, TableService table);

	public SecurityManager getSecurityManager();
}
