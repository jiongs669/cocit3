package com.jiongsoft.cocit.service.impl.demsy;

import com.jiongsoft.cocit.service.impl.BaseConfigService;
import com.kmetop.demsy.config.SoftConfigManager;

public class DemsyConfigService extends BaseConfigService {
	private SoftConfigManager demsyConfigManager;

	DemsyConfigService(SoftConfigManager manager) {
		demsyConfigManager = manager;
	}

	protected String getStr(String key) {
		return demsyConfigManager.get(key, "");
	}

}
