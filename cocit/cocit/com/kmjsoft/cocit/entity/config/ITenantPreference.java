package com.kmjsoft.cocit.entity.config;

import com.kmjsoft.cocit.entity.INamedEntity;
import com.kmjsoft.cocit.entity.ITenantOwnerEntity;

/**
 * 租户个性化设置：
 * 
 * @author Ji Yongshan
 * 
 */
public interface ITenantPreference extends INamedEntity, ITenantOwnerEntity {

	public String getPrefKey();

	public String getPrefValue();

	public String getPrefDesc();
}
