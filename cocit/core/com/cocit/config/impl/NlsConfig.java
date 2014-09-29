package com.cocit.config.impl;

import static com.cocit.Demsy.*;

import com.cocit.config.IConfig;
import com.cocit.config.INlsConfig;

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
