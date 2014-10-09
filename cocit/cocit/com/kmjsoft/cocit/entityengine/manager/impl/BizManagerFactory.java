package com.kmjsoft.cocit.entityengine.manager.impl;

import static com.kmjsoft.cocit.Demsy.bizSession;
import static com.kmjsoft.cocit.Demsy.moduleManager;
import static com.kmjsoft.cocit.Demsy.security;

import com.jiongsoft.cocit.config.IDataSourceConfig;
import com.jiongsoft.cocit.lang.DemsyException;
import com.jiongsoft.cocit.log.Log;
import com.jiongsoft.cocit.log.Logs;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.entity.security.IModule;
import com.kmjsoft.cocit.entityengine.manager.IBizManager;
import com.kmjsoft.cocit.entityengine.manager.IBizManagerFactory;

/**
 * 业务管理器工厂：用来创建业务管理器
 * 
 * @author yongshan.ji
 */
public class BizManagerFactory implements IBizManagerFactory {
	protected static Log log = Logs.getLog(BizManagerFactory.class);

	@Override
	public <X> IBizManager<X> getManager(String moduleID) throws DemsyException {
		log.debugf("获取业务管理器......[moduleID=%s]", moduleID);

		IModule module = null;
		try {
			module = moduleManager.getModule(Long.parseLong(moduleID));
		} catch (Throwable e) {
			module = moduleManager.getModule(moduleID);
		}

		if (module != null && module.getType() == IModule.TYPE_ENTITY) {
			return getManager(module);
		} else
			throw new DemsyException("业务模块不存在! [moduleID=%s]", moduleID);
	}

	@Override
	public <X> IBizManager<X> getManager(IModule module) throws DemsyException {
		if (module == null) {
			return null;
		}
		if (module.getType() != IModule.TYPE_ENTITY)
			throw new DemsyException("业务模块不存在! [moduleID=%s]", module);

		IEntityDefinition system = moduleManager.getSystem(module);

		if (!security.allowVisitModule(module, false)) {
			throw new DemsyException("无权执行该操作!");
		}

		IDataSourceConfig ds = module.getDataSource();
		if (ds != null) {// && !module.isBuildin()
			return new BizManagerImpl(bizSession.me(Demsy.orm(ds)), module, system);
		} else {
			return new BizManagerImpl(bizSession, module, system);
		}
	}

	@Override
	public <X> IBizManager<X> getManager(IModule module, IEntityDefinition system) throws DemsyException {
		if (system == null) {
			return null;
		}

		if (module == null) {
			return new BizManagerImpl(bizSession, module, system);
		}

		if (!security.allowVisitModule(module, false)) {
			throw new DemsyException("无权执行该操作!");
		}

		IDataSourceConfig ds = module.getDataSource();
		if (ds != null) {// && !module.isBuildin()
			return new BizManagerImpl(bizSession.me(Demsy.orm(ds)), module, system);
		} else {
			return new BizManagerImpl(bizSession, module, system);
		}
	}

}
