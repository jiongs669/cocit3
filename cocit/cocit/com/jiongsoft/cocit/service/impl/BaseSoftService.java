package com.jiongsoft.cocit.service.impl;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.service.ConfigManager;
import com.jiongsoft.cocit.service.SecurityManager;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.service.impl.security.SecurityManagerImpl;
import com.jiongsoft.cocit.sms.SmsClient;

public abstract class BaseSoftService implements SoftService {

	/*
	 * lazy load the following properties
	 */
	protected ConfigManager config;

	protected SmsClient smsClient;

	protected SecurityManager securityManager;

	@Override
	public SmsClient getSmsClient() {
		if (smsClient == null) {
			String type = getConfig(ConfigManager.SMS_TYPE, "");
			smsClient = Cocit.makeSmsClient(type);
		}

		return smsClient;
	}

	@Override
	public <T> T getConfig(String configKey, T defaultReturn) {
		// initialize config
		if (config == null)
			config = getSoftConfig();

		return config.get(configKey, defaultReturn);
	}

	protected abstract ConfigManager getSoftConfig();

	@Override
	public SecurityManager getSecurityManager() {
		if (securityManager == null) {
			securityManager = new SecurityManagerImpl(this);
		}

		return securityManager;
	}
}
