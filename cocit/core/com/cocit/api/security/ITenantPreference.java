package com.cocit.api.security;

import com.cocit.api.entity.INamedEntity;

/**
 * 租户个性化设置：
 * 
 * @author Ji Yongshan
 * 
 */
public interface ITenantPreference extends INamedEntity {

	public String getKey();

	public String getValue();

	public String getDesc();
}
