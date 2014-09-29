package com.cocit.engine;

import static com.cocit.Demsy.entityDefEngine;
import static com.cocit.api.APIConst.BIZSYS_DEMSY_CORP;
import static com.cocit.api.APIConst.F_CODE;

import com.cocit.Demsy;
import com.cocit.api.security.ISoftEnv;
import com.jiongsoft.cocit.orm.expr.Expr;

class CacheCorp {

	private ISoftEnv corp;

	CacheCorp(ModuleEngine engine, String code) {
		corp = (ISoftEnv) Demsy.orm().load(entityDefEngine.getStaticType(BIZSYS_DEMSY_CORP), Expr.eq(F_CODE, code));

		if (corp != null && Demsy.appconfig.isProductMode())
			engine.corpCache.put(code, this);
	}

	ISoftEnv get() {
		return corp;
	}
}
