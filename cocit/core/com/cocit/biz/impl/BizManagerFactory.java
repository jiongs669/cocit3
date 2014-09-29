package com.cocit.biz.impl;

import static com.cocit.Demsy.bizSession;
import static com.cocit.Demsy.moduleEngine;
import static com.cocit.Demsy.security;

import com.cocit.Demsy;
import com.cocit.api.entitydef.IEntityDefinition;
import com.cocit.api.security.IModule;
import com.cocit.biz.IBizManager;
import com.cocit.biz.IBizManagerFactory;
import com.cocit.config.IDatasourceConfig;
import com.cocit.lang.DemsyException;
import com.cocit.log.Log;
import com.cocit.log.Logs;

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
			module = moduleEngine.getModule(Long.parseLong(moduleID));
		} catch (Throwable e) {
			module = moduleEngine.getModule(moduleID);
		}

		if (module != null && module.getType() == IModule.TYPE_BIZ) {
			return getManager(module);
		} else
			throw new DemsyException("业务模块不存在! [moduleID=%s]", moduleID);
	}

	@Override
	public <X> IBizManager<X> getManager(IModule module) throws DemsyException {
		if (module == null) {
			return null;
		}
		if (module.getType() != IModule.TYPE_BIZ)
			throw new DemsyException("业务模块不存在! [moduleID=%s]", module);

		IEntityDefinition system = moduleEngine.getSystem(module);

		if (!security.allowVisitModule(module, false)) {
			throw new DemsyException("无权执行该操作!");
		}

		IDatasourceConfig ds = module.getDataSource();
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

		IDatasourceConfig ds = module.getDataSource();
		if (ds != null) {// && !module.isBuildin()
			return new BizManagerImpl(bizSession.me(Demsy.orm(ds)), module, system);
		} else {
			return new BizManagerImpl(bizSession, module, system);
		}
	}

}
