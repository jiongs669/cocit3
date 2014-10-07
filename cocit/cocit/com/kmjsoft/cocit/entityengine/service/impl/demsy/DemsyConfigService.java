package com.kmjsoft.cocit.entityengine.service.impl.demsy;

import com.jiongsoft.cocit.config.SoftConfigManager;
import com.kmjsoft.cocit.entityengine.service.impl.BaseConfigService;

public class DemsyConfigService extends BaseConfigService {
	private SoftConfigManager demsyConfigManager;

	DemsyConfigService(SoftConfigManager manager) {
		demsyConfigManager = manager;
	}

	protected String getStr(String key) {
		return demsyConfigManager.get(key, "");
	}

}
