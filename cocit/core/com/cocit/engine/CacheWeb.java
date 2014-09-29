package com.cocit.engine;

import static com.cocit.Demsy.entityDefEngine;
import static com.cocit.api.APIConst.F_GUID;

import com.cocit.Demsy;
import com.cocit.api.web.IWebContentCatalog;
import com.cocit.lang.Str;
import com.jiongsoft.cocit.orm.expr.Expr;

class CacheWeb {

	private IWebContentCatalog obj;

	CacheWeb(UiEngine engine, Long id) {
		if (id != null) {
			obj = (IWebContentCatalog) Demsy.orm().load(entityDefEngine.getStaticType(IWebContentCatalog.SYS_CODE), id);

			if (obj != null) {
				if (Demsy.appconfig.isProductMode()) {
					engine.webCache.put(id, this);
					engine.webGuidCache.put(obj.getDataGuid(), this);
				}
			}
		}
	}

	CacheWeb(UiEngine engine, String guid) {
		if (!Str.isEmpty(guid)) {
			obj = (IWebContentCatalog) Demsy.orm().load(entityDefEngine.getStaticType(IWebContentCatalog.SYS_CODE), Expr.eq(F_GUID, guid));

			if (obj != null) {
				if (Demsy.appconfig.isProductMode()) {
					engine.webCache.put(obj.getId(), this);
					engine.webGuidCache.put(guid, this);
				}
			}
		}
	}

	IWebContentCatalog get() {
		return obj;
	}

}
