package com.kmjsoft.cocit.entityengine.definition.impl;

import static com.kmjsoft.cocit.Demsy.entityDefManager;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_ADMIN_MODULE;
import static com.kmjsoft.cocit.entity.EntityConst.F_CODE;
import static com.kmjsoft.cocit.entity.EntityConst.F_GUID;

import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.security.IModule;
import com.kmjsoft.cocit.orm.expr.CndExpr;
import com.kmjsoft.cocit.orm.expr.Expr;

class CacheMdl {

	private IModule object;

	CacheMdl(CacheSoft soft, IModule mdl) {
		object = mdl;
		cache(soft);
	}

	CacheMdl(CacheSoft soft, Long moduleID) {
		if (moduleID != null) {
			object = (IModule) Demsy.orm().load(entityDefManager.getStaticType(BIZSYS_ADMIN_MODULE), moduleID);

			cache(soft);
		}
	}

	CacheMdl(CacheSoft soft, String moduleGuid) {
		if (!Str.isEmpty(moduleGuid)) {
			object = (IModule) Demsy.orm().load(entityDefManager.getStaticType(BIZSYS_ADMIN_MODULE), Expr.eq(F_GUID, moduleGuid).or(Expr.eq(F_CODE, moduleGuid)));

			cache(soft);
		}
	}

	CacheMdl(CacheSoft soft, CndExpr expr) {
		object = (IModule) Demsy.orm().load(entityDefManager.getStaticType(BIZSYS_ADMIN_MODULE), expr);

		cache(soft);
	}

	private void cache(CacheSoft soft) {
		if (object != null && Demsy.appconfig.isProductMode()) {
			soft.mdlIdMap.put(object.getId(), this);
			soft.mdlGuidMap.put(object.getDataGuid(), this);
			if (object.getType() == IModule.TYPE_ENTITY) {
				soft.mdlBizIdMap.put(object.getRefID(), this);
			}
		}
	}

	IModule get() {
		return object;
	}
}
