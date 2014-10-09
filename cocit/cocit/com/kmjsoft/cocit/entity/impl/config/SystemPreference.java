package com.kmjsoft.cocit.entity.impl.config;

import com.kmjsoft.cocit.entity.NamedEntity;
import com.kmjsoft.cocit.entity.config.ISystemPreference;

public class SystemPreference extends NamedEntity implements ISystemPreference {

	private String systemGuid;

	private String systemName;

	private String prefKey;

	private String prefValue;

	private String prefDesc;

	public String getSystemGuid() {
		return systemGuid;
	}

	public void setSystemGuid(String systemGuid) {
		this.systemGuid = systemGuid;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

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
