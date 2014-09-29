package com.jiongsoft.cocit.service.impl.demsy;

import java.util.Date;
import java.util.Properties;

import com.jiongsoft.cocit.entity.SoftEntity;
import com.jiongsoft.cocit.orm.Orm;
import com.jiongsoft.cocit.service.ConfigManager;
import com.jiongsoft.cocit.service.EntityManager;
import com.jiongsoft.cocit.service.TableService;
import com.jiongsoft.cocit.service.ModuleService;
import com.jiongsoft.cocit.service.impl.BaseSoftService;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.StringUtil;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.impl.base.lib.DemsySoft;
import com.kmetop.demsy.config.SoftConfigManager;

public class DemsySoftService extends BaseSoftService {
	private DemsySoft entity;

	private DemsyConfigService config;

	DemsySoftService(DemsySoft demsySoft) {
		this.entity = demsySoft;
		config = new DemsyConfigService(SoftConfigManager.me());
	}

	@Override
	public Properties getExtProps() {
		return entity.getDynaProp();
	}

	@Override
	protected ConfigManager getSoftConfig() {
		return config;
	}

	@Override
	public Long getID() {
		return entity.getId();
	}

	@Override
	public int getSequence() {
		return entity.getOrderby();
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
	public String getInfo() {
		return entity.getDesc();
	}

	@Override
	public Date getCreatedDate() {
		return entity.getCreated();
	}

	@Override
	public String getCreatedUser() {
		return entity.getCreatedBy();
	}

	@Override
	public Date getLatestModifiedDate() {
		return entity.getUpdated();
	}

	@Override
	public String getLatestModifiedUser() {
		return entity.getUpdatedBy();
	}

	@Override
	public <T> T get(String propName, T defaultReturn) {
		String value = entity.get(propName);

		if (value == null)
			return defaultReturn;
		if (defaultReturn == null)
			return (T) value;

		Class valueType = defaultReturn.getClass();

		try {
			return (T) StringUtil.castTo(value, valueType);
		} catch (Throwable e) {
			Log.warn("", e);
		}

		return defaultReturn;
	}

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
	public SoftEntity getEntity() {
		return entity;
	}

}
