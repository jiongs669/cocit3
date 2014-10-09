package com.kmjsoft.cocit.entityengine.service.impl.demsy;

import com.jiongsoft.cocit.config.TenantPreferenceManager;
import com.kmjsoft.cocit.entityengine.service.impl.BaseConfigService;

public class DemsyConfigService extends BaseConfigService {
	private TenantPreferenceManager demsyConfigManager;

	DemsyConfigService(TenantPreferenceManager manager) {
		demsyConfigManager = manager;
	}

	protected String getStr(String key) {
		return demsyConfigManager.get(key, "");
	}

}
