package com.kmjsoft.cocit.entityengine.service.impl.demsy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.jiongsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.definition.IEntityField;
import com.kmjsoft.cocit.entity.impl.entitydef.BizAction;
import com.kmjsoft.cocit.entity.impl.entitydef.SFTSystem;
import com.kmjsoft.cocit.entity.impl.security.Module;
import com.kmjsoft.cocit.entity.impl.security.Tenant;
import com.kmjsoft.cocit.entity.security.IModule;
import com.kmjsoft.cocit.entityengine.definition.impl.BizEngine;
import com.kmjsoft.cocit.entityengine.definition.impl.ModuleEngine;
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
		moduleEngine = (ModuleEngine) Demsy.moduleManager;
		bizEngine = (BizEngine) Demsy.entityDefManager;
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

	private DemsyEntityTableService makeBizTable(SFTSystem system) {
		DemsyEntityTableService ret = new DemsyEntityTableService(system);

		return ret;
	}

	@Override
	public ModuleService getModule(Serializable moduleID) {
		Module module;
		if (moduleID instanceof Long)
			module = (Module) moduleEngine.getModule((Long) moduleID);
		else
			module = (Module) moduleEngine.getModule(moduleID.toString());

		if (module == null)
			return null;

		SFTSystem mainSystem = (SFTSystem) moduleEngine.getSystem(module);

		//
		TableService mainDataTable = this.makeBizTable(mainSystem);
		DemsyModuleService ret = new DemsyModuleService(module, mainDataTable);

		//
		List<TableService> childrenDataTables = new ArrayList();
		List<IEntityField> fkFields = bizEngine.getFieldsOfSlave(mainSystem);
		for (IEntityField fkField : fkFields) {
			SFTSystem fkSystem = (SFTSystem) fkField.getSystem();

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
		SFTSystem system;
		if (tableID == null)
			return null;

		if (tableID instanceof Long)
			system = (SFTSystem) bizEngine.getSystem((Long) tableID);
		else
			system = (SFTSystem) bizEngine.getSystem(tableID.toString());

		// TODO:应通过模块表达式来解析数据表对象，目前暂时不支持模块对数据表的引用表达式。

		return this.makeBizTable(system);
	}

	@Override
	public OperationService getOperation(TableService table, String opMode) {
		if (opMode == null)
			return null;

		BizAction action = (BizAction) bizEngine.getAction(table.getID(), opMode);
		if (action == null)
			return null;

		return new DemsyEntityOperationService(action);
	}

	@Override
	public OperationService getOperation(ModuleService module, String opMode) {
		if (opMode == null)
			return null;

		BizAction action = (BizAction) moduleEngine.getAction((IModule) module.getEntity(), opMode);
		if (action == null)
			return null;

		return new DemsyEntityOperationService(action);
	}

	@Override
	public TableService getTable(ModuleService moduleService) {
		Module module = (Module) moduleService.getEntity();

		SFTSystem system = (SFTSystem) moduleEngine.getSystem(module);

		return this.makeBizTable(system);
	}
}
