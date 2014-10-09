package com.kmjsoft.cocit.entityengine.service.impl;

import com.kmjsoft.cocit.Cocit;
import com.kmjsoft.cocit.entityengine.service.ITenantPreferenceService;
import com.kmjsoft.cocit.entityengine.service.SecurityManager;
import com.kmjsoft.cocit.entityengine.service.SoftService;
import com.kmjsoft.cocit.entityengine.service.impl.security.SecurityManagerImpl;
import com.kmjsoft.cocit.sms.SmsClient;

public abstract class BaseSoftService implements SoftService {

	/*
	 * lazy load the following properties
	 */
	protected ITenantPreferenceService config;

	protected SmsClient smsClient;

	protected SecurityManager securityManager;

	@Override
	public SmsClient getSmsClient() {
		if (smsClient == null) {
			String type = getConfig(ITenantPreferenceService.SMS_TYPE, "");
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

	protected abstract ITenantPreferenceService getSoftConfig();

	@Override
	public SecurityManager getSecurityManager() {
		if (securityManager == null) {
			securityManager = new SecurityManagerImpl(this);
		}

		return securityManager;
	}
}
