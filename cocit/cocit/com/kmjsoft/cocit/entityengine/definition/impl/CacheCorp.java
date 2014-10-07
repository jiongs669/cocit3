package com.kmjsoft.cocit.entityengine.definition.impl;

import static com.jiongsoft.cocit.Demsy.entityDefManager;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_CORP;
import static com.kmjsoft.cocit.entity.EntityConst.F_CODE;

import com.jiongsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.security.ISystem;
import com.kmjsoft.cocit.orm.expr.Expr;

class CacheCorp {

	private ISystem corp;

	CacheCorp(ModuleEngine engine, String code) {
		corp = (ISystem) Demsy.orm().load(entityDefManager.getStaticType(BIZSYS_DEMSY_CORP), Expr.eq(F_CODE, code));

		if (corp != null && Demsy.appconfig.isProductMode())
			engine.corpCache.put(code, this);
	}

	ISystem get() {
		return corp;
	}
}
