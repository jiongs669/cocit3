package com.kmjsoft.cocit.entityengine.definition.impl;

import static com.jiongsoft.cocit.Demsy.entityDefManager;
import static com.kmjsoft.cocit.entity.EntityConst.F_GUID;

import com.jiongsoft.cocit.Demsy;
import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.entity.web.IWebContentCatalog;
import com.kmjsoft.cocit.orm.expr.Expr;

class CacheWeb {

	private IWebContentCatalog obj;

	CacheWeb(UiEngine engine, Long id) {
		if (id != null) {
			obj = (IWebContentCatalog) Demsy.orm().load(entityDefManager.getStaticType(IWebContentCatalog.SYS_CODE), id);

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
			obj = (IWebContentCatalog) Demsy.orm().load(entityDefManager.getStaticType(IWebContentCatalog.SYS_CODE), Expr.eq(F_GUID, guid));

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
