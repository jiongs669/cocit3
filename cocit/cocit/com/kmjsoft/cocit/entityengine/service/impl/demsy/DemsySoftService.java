package com.kmjsoft.cocit.entityengine.service.impl.demsy;

import java.util.Date;

import com.jiongsoft.cocit.config.TenantPreferenceManager;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.impl.security.Tenant;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.entityengine.service.ConfigManager;
import com.kmjsoft.cocit.entityengine.service.EntityManager;
import com.kmjsoft.cocit.entityengine.service.ModuleService;
import com.kmjsoft.cocit.entityengine.service.TableService;
import com.kmjsoft.cocit.entityengine.service.impl.BaseSoftService;
import com.kmjsoft.cocit.orm.Orm;

public class DemsySoftService extends BaseSoftService {
	private Tenant entity;

	private DemsyConfigService config;

	DemsySoftService(Tenant tenant) {
		this.entity = tenant;
		config = new DemsyConfigService(TenantPreferenceManager.me());
	}

	// @Override
	// public Properties getExtProps() {
	// return entity.getProperties();
	// }

	@Override
	protected ConfigManager getSoftConfig() {
		return config;
	}

	@Override
	public Long getID() {
		return entity.getId();
	}

	@Override
	public String getName() {
		return entity.getName();
	}

	@Override
	public boolean isDisabled() {
		return entity.isDisabled();
	}

	@Override
	public Date getOperatedDate() {
		return entity.getUpdatedDate();
	}

	@Override
	public String getOperatedUser() {
		return entity.getUpdatedUser();
	}

	// @Override
	// public <T> T get(String propName, T defaultReturn) {
	// String value = entity.get(propName);
	//
	// if (value == null)
	// return defaultReturn;
	// if (defaultReturn == null)
	// return (T) value;
	//
	// Class valueType = defaultReturn.getClass();
	//
	// try {
	// return (T) StringUtil.castTo(value, valueType);
	// } catch (Throwable e) {
	// Log.warn("", e);
	// }
	//
	// return defaultReturn;
	// }

	@Override
	public String getCode() {
		return entity.getCode();
	}

	@Override
	public EntityManager getEntityManager(ModuleService module, TableService table) {
		if (module == null && table == null)
			return null;

		return new DemsyEntityManager(module, table);
	}

	@Override
	public Orm getOrm() {
		return Demsy.orm();
	}

	@Override
	public ITenant getEntity() {
		return entity;
	}

}
