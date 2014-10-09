package com.kmjsoft.cocit.entity.impl.config;

import com.kmjsoft.cocit.entity.TenantOwnerNamedEntity;
import com.kmjsoft.cocit.entity.config.ITenantPreference;

public class TenantPreference extends TenantOwnerNamedEntity implements ITenantPreference {

	private String prefKey;

	private String prefValue;

	private String prefDesc;

	public String getPrefKey() {
		return prefKey;
	}

	public void setPrefKey(String prefKey) {
		this.prefKey = prefKey;
	}

	public String getPrefValue() {
		return prefValue;
	}

	public void setPrefValue(String prefValue) {
		this.prefValue = prefValue;
	}

	public String getPrefDesc() {
		return prefDesc;
	}

	public void setPrefDesc(String prefDesc) {
		this.prefDesc = prefDesc;
	}
}
