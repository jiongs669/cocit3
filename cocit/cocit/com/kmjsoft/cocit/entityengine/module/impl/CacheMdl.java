package com.kmjsoft.cocit.entityengine.module.impl;

import static com.kmjsoft.cocit.Demsy.entityModuleManager;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_ADMIN_MODULE;
import static com.kmjsoft.cocit.entity.EntityConst.F_CODE;
import static com.kmjsoft.cocit.entity.EntityConst.F_DATA_GUID;

import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.security.IFunMenu;
import com.kmjsoft.cocit.orm.expr.CndExpr;
import com.kmjsoft.cocit.orm.expr.Expr;

class CacheMdl {

	private IFunMenu object;

	CacheMdl(CacheSoft soft, IFunMenu mdl) {
		object = mdl;
		cache(soft);
	}

	CacheMdl(CacheSoft soft, Long moduleID) {
		if (moduleID != null) {
			object = (IFunMenu) Demsy.orm().load(entityModuleManager.getStaticType(BIZSYS_ADMIN_MODULE), moduleID);

			cache(soft);
		}
	}

	CacheMdl(CacheSoft soft, String moduleGuid) {
		if (!Str.isEmpty(moduleGuid)) {
			object = (IFunMenu) Demsy.orm().load(entityModuleManager.getStaticType(BIZSYS_ADMIN_MODULE), Expr.eq(F_DATA_GUID, moduleGuid).or(Expr.eq(F_CODE, moduleGuid)));

			cache(soft);
		}
	}

	CacheMdl(CacheSoft soft, CndExpr expr) {
		object = (IFunMenu) Demsy.orm().load(entityModuleManager.getStaticType(BIZSYS_ADMIN_MODULE), expr);

		cache(soft);
	}

	private void cache(CacheSoft soft) {
		if (object != null && Demsy.appconfig.isProductMode()) {
			soft.mdlIdMap.put(object.getId(), this);
			soft.mdlGuidMap.put(object.getDataGuid(), this);
			if (object.getType() == IFunMenu.TYPE_ENTITY) {
				soft.mdlBizIdMap.put(object.getRefID(), this);
			}
		}
	}

	IFunMenu get() {
		return object;
	}
}
