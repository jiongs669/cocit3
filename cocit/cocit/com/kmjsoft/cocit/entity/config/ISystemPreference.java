package com.kmjsoft.cocit.entity.config;

import com.kmjsoft.cocit.entity.INamedEntity;
import com.kmjsoft.cocit.entity.security.ISystem;

/**
 * 系统个性化设置：
 * 
 * @author Ji Yongshan
 * 
 */
public interface ISystemPreference extends INamedEntity {

	/**
	 * 系统GUID：逻辑外键，关联到{@link ISystem#getDataGuid()}字段。
	 * 
	 * @return
	 */
	public String getSystemGuid();

	/**
	 * 系统名称：冗余字段，关联到{@link ISystem#getName()}字段。
	 * 
	 * @return
	 */
	public String getSystemName();

	public String getPrefKey();

	public String getPrefValue();

	public String getPrefDesc();
}
