package com.kmjsoft.cocit.entityengine.service.impl.demsy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.impl.module.EntityAction;
import com.kmjsoft.cocit.entity.impl.module.EntityModule;
import com.kmjsoft.cocit.entity.impl.security.FunMenu;
import com.kmjsoft.cocit.entity.impl.security.Tenant;
import com.kmjsoft.cocit.entity.module.IEntityColumn;
import com.kmjsoft.cocit.entity.security.IFunMenu;
import com.kmjsoft.cocit.entityengine.module.impl.BizEngine;
import com.kmjsoft.cocit.entityengine.module.impl.ModuleEngine;
import com.kmjsoft.cocit.entityengine.service.ModuleService;
import com.kmjsoft.cocit.entityengine.service.OperationService;
import com.kmjsoft.cocit.entityengine.service.ServiceFactory;
import com.kmjsoft.cocit.entityengine.service.SoftService;
import com.kmjsoft.cocit.entityengine.service.TableService;

/**
 * 
 * @author jiongs753
 * 
 */
public class DemsyServiceFactory implements ServiceFactory {

	private BizEngine bizEngine;

	private ModuleEngine moduleEngine;

	private Map<String, SoftService> cacheSoft;

	public DemsyServiceFactory() {
		moduleEngine = (ModuleEngine) Demsy.funMenuManager;
		bizEngine = (BizEngine) Demsy.entityModuleManager;
		cacheSoft = new Hashtable();
	}

	@Override
	public SoftService getSoftService(String domain) {
		if (domain == null)
			domain = "";

		synchronized (cacheSoft) {
			SoftService ret = cacheSoft.get(domain);
			if (ret == null) {
				Tenant ds = (Tenant) moduleEngine.getSoft(domain);
				if (ds != null) {
					ret = new DemsySoftService(ds);
					cacheSoft.put(domain, ret);
				}
			}

			return ret;
		}

	}

	private DemsyEntityTableService makeBizTable(EntityModule system) {
		DemsyEntityTableService ret = new DemsyEntityTableService(system);

		return ret;
	}

	@Override
	public ModuleService getModule(Serializable moduleID) {
		FunMenu funMenu;
		if (moduleID instanceof Long)
			funMenu = (FunMenu) moduleEngine.getModule((Long) moduleID);
		else
			funMenu = (FunMenu) moduleEngine.getModule(moduleID.toString());

		if (funMenu == null)
			return null;

		EntityModule mainSystem = (EntityModule) moduleEngine.getSystem(funMenu);

		//
		TableService mainDataTable = this.makeBizTable(mainSystem);
		DemsyModuleService ret = new DemsyModuleService(funMenu, mainDataTable);

		//
		List<TableService> childrenDataTables = new ArrayList();
		List<IEntityColumn> fkFields = bizEngine.getFieldsOfSlave(mainSystem);
		for (IEntityColumn fkField : fkFields) {
			EntityModule fkSystem = (EntityModule) fkField.getSystem();

			DemsyEntityTableService bizTable = this.makeBizTable(fkSystem);
			childrenDataTables.add(bizTable);

			// 设置该子表通过哪个字段引用了主表？
			bizTable.set("fkfield", fkField.getPropName());

			// TODO:应通过模块表达式来解析数据表对象，目前暂时不支持模块对数据表的引用表达式。

		}

		ret.setChildrenDataTables(childrenDataTables);

		return ret;
	}

	@Override
	public TableService getTable(Serializable tableID) {
		EntityModule system;
		if (tableID == null)
			return null;

		if (tableID instanceof Long)
			system = (EntityModule) bizEngine.getSystem((Long) tableID);
		else
			system = (EntityModule) bizEngine.getSystem(tableID.toString());

		// TODO:应通过模块表达式来解析数据表对象，目前暂时不支持模块对数据表的引用表达式。

		return this.makeBizTable(system);
	}

	@Override
	public OperationService getOperation(TableService table, String opMode) {
		if (opMode == null)
			return null;

		EntityAction action = (EntityAction) bizEngine.getAction(table.getID(), opMode);
		if (action == null)
			return null;

		return new DemsyEntityOperationService(action);
	}

	@Override
	public OperationService getOperation(ModuleService module, String opMode) {
		if (opMode == null)
			return null;

		EntityAction action = (EntityAction) moduleEngine.getAction((IFunMenu) module.getEntity(), opMode);
		if (action == null)
			return null;

		return new DemsyEntityOperationService(action);
	}

	@Override
	public TableService getTable(ModuleService moduleService) {
		FunMenu funMenu = (FunMenu) moduleService.getEntity();

		EntityModule system = (EntityModule) moduleEngine.getSystem(funMenu);

		return this.makeBizTable(system);
	}
}
