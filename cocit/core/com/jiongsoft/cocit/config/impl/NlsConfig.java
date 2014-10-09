package com.jiongsoft.cocit.config.impl;

import static com.kmjsoft.cocit.Demsy.*;

import com.jiongsoft.cocit.config.IConfig;
import com.jiongsoft.cocit.config.INlsConfig;

public class NlsConfig extends BaseConfig implements INlsConfig {
	public NlsConfig() {
		super(appconfig.getNLSConfig());
	}

	@Override
	public IConfig copy() {
		NlsConfig ret = new NlsConfig();
		this.copyTo(ret);

		return ret;
	}
}
