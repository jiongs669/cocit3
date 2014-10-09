package com.kmjsoft.cocit.entityengine.module.impl;

import static com.kmjsoft.cocit.Demsy.entityModuleManager;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_ADMIN_CONFIG;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_ADMIN_MODULE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_SOFT;
import static com.kmjsoft.cocit.entity.EntityConst.F_CODE;
import static com.kmjsoft.cocit.entity.EntityConst.F_DOMAIN;
import static com.kmjsoft.cocit.entity.EntityConst.F_ORDER_BY;
import static com.kmjsoft.cocit.entity.EntityConst.F_REF_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.F_TENANT_OWNER_GUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.config.ITenantPreference;
import com.kmjsoft.cocit.entity.security.IFunMenu;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.orm.expr.Expr;

class CacheSoft {

	ITenant object;

	Map<String, ITenantPreference> configs;

	Map<String, CacheMdl> mdlGuidMap = new HashMap();

	Map<Long, CacheMdl> mdlIdMap = new HashMap();

	Map<Long, CacheMdl> mdlBizIdMap = new HashMap();

	List<? extends IFunMenu> funMenus;

	// Map<String, IRealm> realmMap = new HashMap();
	//
	// List<? extends IRealm> realms;

	private void cache(ModuleEngine engine) {
		if (object != null && Demsy.appconfig.isProductMode()) {
			String key = object.getDomain();
			if (Str.isEmpty(key)) {
				key = object.getCode();
			}
			if (!Str.isEmpty(key)) {
				engine.softCache.put(key, this);
			}
			engine.softIdCache.put(object.getId(), this);
		}

	}

	CacheSoft(ModuleEngine engine, String domainOrCode) {
		object = (ITenant) Demsy.orm().load(entityModuleManager.getStaticType(BIZSYS_DEMSY_SOFT), Expr.eq(F_DOMAIN, domainOrCode).or(Expr.eq(F_CODE, domainOrCode)));
		this.cache(engine);
	}

	CacheSoft(ModuleEngine engine, Long id) {
		object = (ITenant) Demsy.orm().load(entityModuleManager.getStaticType(BIZSYS_DEMSY_SOFT), id);
		this.cache(engine);
	}

	Map<String, ITenantPreference> configs() {
		if (configs == null) {
			configs = new HashMap();
			List<ITenantPreference> list = Demsy.orm().query(entityModuleManager.getStaticType(BIZSYS_ADMIN_CONFIG), Expr.eq(F_TENANT_OWNER_GUID, object.getId()));
			for (ITenantPreference c : list) {
				configs.put(c.getCode(), c);
			}
		}

		return configs;
	}

	ITenant get() {
		return object;
	}

	List<? extends IFunMenu> funMenus() {
		if (funMenus == null) {
			funMenus = (List<? extends IFunMenu>) Demsy.orm().query(entityModuleManager.getStaticType(BIZSYS_ADMIN_MODULE), Expr.eq(F_TENANT_OWNER_GUID, object.getId()).addAsc(F_ORDER_BY));
			for (IFunMenu mdl : funMenus) {
				new CacheMdl(this, mdl);
			}
		}
		return funMenus;
	}

	IFunMenu funMenu(Long id) {
		if (id == null) {
			return null;
		}

		CacheMdl mdl = mdlIdMap.get(id);
		if (mdl == null)
			mdl = new CacheMdl(this, id);

		return mdl.get();
	}

	IFunMenu funMenu(String guid) {
		if (Str.isEmpty(guid)) {
			return null;
		}

		CacheMdl mdl = mdlGuidMap.get(guid);
		if (mdl == null)
			mdl = new CacheMdl(this, guid);

		return mdl.get();
	}

	IFunMenu bizModule(Long bizID) {
		CacheMdl mdl = mdlBizIdMap.get(bizID);
		if (mdl == null)
			mdl = new CacheMdl(this, Expr.eq(F_REF_SYSTEM, bizID).and(Expr.eq(F_TENANT_OWNER_GUID, object.getId())));

		return mdl.get();
	}

	// List<? extends IRealm> realms() {
	// if (realms == null || realms.size() == 0) {
	// realms = (List<? extends IRealm>) Demsy.orm().query(entityDefEngine.getStaticType(BIZSYS_ADMIN_REALM), Expr.eq(F_SOFT_ID, object.getId()).addAsc(F_ORDER_BY));
	// for (IRealm realm : realms) {
	// realmMap.put(realm.getCode(), realm);
	// }
	// }
	// return realms;
	// }
	//
	// public IRealm realm(String realmCode) {
	// realms();
	// return realmMap.get(realmCode);
	// }
}
