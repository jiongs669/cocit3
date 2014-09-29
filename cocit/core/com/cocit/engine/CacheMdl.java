package com.cocit.engine;

import static com.cocit.Demsy.entityDefEngine;
import static com.cocit.api.APIConst.BIZSYS_ADMIN_MODULE;
import static com.cocit.api.APIConst.F_CODE;
import static com.cocit.api.APIConst.F_GUID;

import com.cocit.Demsy;
import com.cocit.api.security.IModule;
import com.cocit.lang.Str;
import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.orm.expr.Expr;

class CacheMdl {

	private IModule object;

	CacheMdl(CacheSoft soft, IModule mdl) {
		object = mdl;
		cache(soft);
	}

	CacheMdl(CacheSoft soft, Long moduleID) {
		if (moduleID != null) {
			object = (IModule) Demsy.orm().load(entityDefEngine.getStaticType(BIZSYS_ADMIN_MODULE), moduleID);

			cache(soft);
		}
	}

	CacheMdl(CacheSoft soft, String moduleGuid) {
		if (!Str.isEmpty(moduleGuid)) {
			object = (IModule) Demsy.orm().load(entityDefEngine.getStaticType(BIZSYS_ADMIN_MODULE), Expr.eq(F_GUID, moduleGuid).or(Expr.eq(F_CODE, moduleGuid)));

			cache(soft);
		}
	}

	CacheMdl(CacheSoft soft, CndExpr expr) {
		object = (IModule) Demsy.orm().load(entityDefEngine.getStaticType(BIZSYS_ADMIN_MODULE), expr);

		cache(soft);
	}

	private void cache(CacheSoft soft) {
		if (object != null && Demsy.appconfig.isProductMode()) {
			soft.mdlIdMap.put(object.getId(), this);
			soft.mdlGuidMap.put(object.getDataGuid(), this);
			if (object.getType() == IModule.TYPE_BIZ) {
				soft.mdlBizIdMap.put(object.getRefID(), this);
			}
		}
	}

	IModule get() {
		return object;
	}
}
