package com.kmjsoft.cocit.entity.config;

import com.kmjsoft.cocit.entity.INamedEntity;

/**
 * 租户个性化设置：
 * 
 * @author Ji Yongshan
 * 
 */
public interface IPreferenceOfTenant extends INamedEntity {

	public String getKey();

	public String getValue();

	public String getDesc();
}
