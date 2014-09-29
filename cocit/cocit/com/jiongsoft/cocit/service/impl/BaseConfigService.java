// $codepro.audit.disable unnecessaryCast
package com.jiongsoft.cocit.service.impl;

import com.jiongsoft.cocit.service.ConfigManager;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.StringUtil;

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
