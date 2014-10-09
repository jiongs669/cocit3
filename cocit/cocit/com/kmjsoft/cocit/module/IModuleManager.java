package com.kmjsoft.cocit.module;

import java.io.Serializable;
import java.util.List;

import com.jiongsoft.cocit.config.IDataSourceConfig;
import com.jiongsoft.cocit.lang.Nodes;
import com.kmjsoft.cocit.entity.actionplugin.IActionPlugin;
import com.kmjsoft.cocit.entity.config.ITenantPreference;
import com.kmjsoft.cocit.entity.definition.IEntityAction;
import com.kmjsoft.cocit.entity.definition.IEntityCatalog;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.entity.security.IModule;
import com.kmjsoft.cocit.entity.security.ISystem;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.orm.ExtOrm;

/**
 * 组件库：用于获取经过安全认证后的组件。
 * 
 * @author yongshan.ji
 * 
 */
public interface IModuleManager {
	public void clearCache();

	Nodes makeNodesByCurrentSoft();

	/**
	 * 获取平台默认企业用户
	 * 
	 * @return
	 */
	ISystem getCorpByDefault();

	ISystem getCorp(String code);

	/**
	 * 获取平台默认应用软件
	 * 
	 * @return
	 */
	ITenant getSoftByDefault();

	ITenant getSoft(String domainOrCode);

	ITenant getSoft(Long id);

	List<? extends IModule> getModules(ITenant soft);

	/**
	 * 根据功能模块ID获取功能模块
	 */
	IModule getModule(Long moduleID);

	/**
	 * 根据功能模块GUID获取功能模块
	 * 
	 * @param moduleGuid
	 * @return
	 */
	IModule getModule(String moduleGuid);

	/**
	 * 获取软件业务模块
	 * 
	 * @param soft
	 * @param system
	 * @return
	 */
	IModule getModule(ITenant soft, IEntityDefinition system);

	/**
	 * 获取从属模块
	 * 
	 * @param moduleID
	 * @return
	 */
	List<IModule> getSubModules(IModule module);

	/**
	 * 获取模块操作
	 * 
	 * @param moduleID
	 * @param opID
	 * @return
	 */
	IEntityAction getAction(IModule module, Serializable opID);

	IActionPlugin[] getPlugins(IEntityAction entityAction);

	/**
	 * 创建软件功能模块菜单
	 * 
	 * @param soft
	 * @param optimize
	 * @return
	 */
	Nodes makeNodesByModule(ITenant soft);

	Nodes makeNodesByModule(ITenant soft, byte role);

	/**
	 * 创建模块操作菜单
	 * 
	 * @param moduleID
	 * @return
	 */
	Nodes makeNodesByAction(IModule module);

	/**
	 * 获取模块业务系统
	 * 
	 * @param moduleID
	 * @return
	 */
	IEntityDefinition getSystem(IModule module);

	/**
	 * 升级软件功能模块
	 * 
	 * @param soft
	 */
	void upgradeModules(ITenant soft);

	void upgradeWebContent(ITenant soft);

	/**
	 * 安装软件功能模块
	 * 
	 * @param soft
	 */
	void setupDemsy();

	ITenantPreference getSoftConfig(String key);

	// Nodes makeNodesByRealm(ITenant soft);

	// List<? extends IRealm> getRealms(ITenant softObj);

	// IRealm getRealm(ITenant soft, String realmCode);

	IModule getModule(String tenantGuid, IEntityDefinition refrenceSystem);

	IEntityAction getActionComponent(Long actionLib);

	IDataSourceConfig getDataSource(Long dataSource);

	IModule makeModule(ExtOrm orm, ITenant soft, IEntityCatalog catalog);

	IModule makeModule(ExtOrm orm, ITenant soft, IEntityDefinition system);

	void increase(ExtOrm orm, Object obj, String field);

	void decrease(ExtOrm orm, Object obj, String field);

	void increase(ExtOrm orm, Object obj, String field, int value);

	void decrease(ExtOrm orm, Object obj, String field, int value);
}
