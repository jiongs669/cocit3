package com.jiongsoft.cocit.entityservice.impl.demsy;

import com.cocit.config.SoftConfigManager;
import com.jiongsoft.cocit.entityservice.impl.BaseConfigService;

public class DemsyConfigService extends BaseConfigService {
	private SoftConfigManager demsyConfigManager;

	DemsyConfigService(SoftConfigManager manager) {
		demsyConfigManager = manager;
	}

	protected String getStr(String key) {
		return demsyConfigManager.get(key, "");
	}

}
