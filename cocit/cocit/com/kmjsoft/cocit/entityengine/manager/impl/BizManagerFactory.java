package com.kmjsoft.cocit.entityengine.manager.impl;

import static com.kmjsoft.cocit.Demsy.bizSession;
import static com.kmjsoft.cocit.Demsy.funMenuManager;
import static com.kmjsoft.cocit.Demsy.security;

import com.jiongsoft.cocit.config.IDataSourceConfig;
import com.jiongsoft.cocit.lang.DemsyException;
import com.jiongsoft.cocit.log.Log;
import com.jiongsoft.cocit.log.Logs;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.module.IEntityModule;
import com.kmjsoft.cocit.entity.security.IFunMenu;
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

		IFunMenu funMenu = null;
		try {
			funMenu = funMenuManager.getModule(Long.parseLong(moduleID));
		} catch (Throwable e) {
			funMenu = funMenuManager.getModule(moduleID);
		}

		if (funMenu != null && funMenu.getType() == IFunMenu.TYPE_ENTITY) {
			return getManager(funMenu);
		} else
			throw new DemsyException("业务模块不存在! [moduleID=%s]", moduleID);
	}

	@Override
	public <X> IBizManager<X> getManager(IFunMenu funMenu) throws DemsyException {
		if (funMenu == null) {
			return null;
		}
		if (funMenu.getType() != IFunMenu.TYPE_ENTITY)
			throw new DemsyException("业务模块不存在! [moduleID=%s]", funMenu);

		IEntityModule system = funMenuManager.getSystem(funMenu);

		if (!security.allowVisitModule(funMenu, false)) {
			throw new DemsyException("无权执行该操作!");
		}

		IDataSourceConfig ds = funMenu.getDataSource();
		if (ds != null) {// && !module.isBuildin()
			return new BizManagerImpl(bizSession.me(Demsy.orm(ds)), funMenu, system);
		} else {
			return new BizManagerImpl(bizSession, funMenu, system);
		}
	}

	@Override
	public <X> IBizManager<X> getManager(IFunMenu funMenu, IEntityModule system) throws DemsyException {
		if (system == null) {
			return null;
		}

		if (funMenu == null) {
			return new BizManagerImpl(bizSession, funMenu, system);
		}

		if (!security.allowVisitModule(funMenu, false)) {
			throw new DemsyException("无权执行该操作!");
		}

		IDataSourceConfig ds = funMenu.getDataSource();
		if (ds != null) {// && !module.isBuildin()
			return new BizManagerImpl(bizSession.me(Demsy.orm(ds)), funMenu, system);
		} else {
			return new BizManagerImpl(bizSession, funMenu, system);
		}
	}

}
