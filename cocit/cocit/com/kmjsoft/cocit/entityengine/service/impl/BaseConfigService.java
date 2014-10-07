// $codepro.audit.disable unnecessaryCast
package com.kmjsoft.cocit.entityengine.service.impl;

import com.kmjsoft.cocit.entityengine.service.ConfigManager;
import com.kmjsoft.cocit.util.Log;
import com.kmjsoft.cocit.util.StringUtil;

public abstract class BaseConfigService implements ConfigManager {

	protected abstract String getStr(String key);

	public <T> T get(String configKey, T defaultReturn) {
		String value = this.getStr(configKey);

		try {
			return (T) StringUtil.castTo(value, defaultReturn);
		} catch (Throwable e) {
			Log.error("CoudSoftConfigImpl.get: 出错！ {key:%s, defaultReturn:%s}", configKey, defaultReturn, e);
		}

		return defaultReturn;
	}

}
