package com.kmjsoft.cocit.entityengine.service.impl.demsy;

import java.util.List;

import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.impl.module.EntityModule;
import com.kmjsoft.cocit.entity.impl.security.FunMenu;
import com.kmjsoft.cocit.entityengine.manager.IBizManager;
import com.kmjsoft.cocit.entityengine.service.EntityManager;
import com.kmjsoft.cocit.entityengine.service.ModuleService;
import com.kmjsoft.cocit.entityengine.service.TableService;
import com.kmjsoft.cocit.orm.expr.CndExpr;

public class DemsyEntityManager implements EntityManager {
	private IBizManager bizManager;

	private DemsyModuleService moduleService;

	private DemsyEntityTableService tableService;

	DemsyEntityManager(ModuleService m, TableService t) {
		moduleService = (DemsyModuleService) m;
		tableService = (DemsyEntityTableService) t;

		FunMenu funMenu = null;
		if (moduleService != null) {
			funMenu = moduleService.getEntity();
			if (tableService == null)
				tableService = (DemsyEntityTableService) moduleService.getTable();
		}

		EntityModule system = null;
		if (tableService != null)
			system = tableService.getEntity();

		if (system == null && funMenu != null)
			system = (EntityModule) Demsy.funMenuManager.getSystem(funMenu);

		bizManager = Demsy.bizManagerFactory.getManager(funMenu, system);
	}

	@Override
	public int save(Object entity, String opMode) {
		tableService.validateEntityData(opMode, entity);

		return bizManager.save(entity, opMode);
	}

	@Override
	public int delete(Object entity, String opMode) {
		return bizManager.delete(entity, opMode);
	}

	@Override
	public Object load(Long entityID, String opMode) {
		return bizManager.load(entityID, opMode);
	}

	@Override
	public List query(CndExpr expr, String opMode) {
		return bizManager.query(opMode, expr);
	}

	@Override
	public int count(CndExpr expr, String opMode) {
		return bizManager.count(opMode, expr);
	}

	@Override
	public int delete(Long id, String opMode) {
		return bizManager.delete(id, opMode);
	}

	@Override
	public int delete(Long[] idArray, String opMode) {
		for (Long id : idArray) {
			bizManager.delete(id, opMode);
		}
		return idArray.length;
	}

	@Override
	public Class getType() {
		return bizManager.getType();
	}

	@Override
	public String execTask(Object obj, String opMode) {
		return (String) bizManager.run(obj, opMode);
	}

	@Override
	public String execAsynTask(Object obj, String opMode) {
		return (String) bizManager.asynRun(obj, opMode);
	}

}
