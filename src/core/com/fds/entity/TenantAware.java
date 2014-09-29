package com.fds.entity;

/**
 * 租户敏感的：该类的所有子类将自动带有“所属租户”字段，用户访问实体数据的时候，硬性规定只能访问自己租户的数据。
 * 
 * @author Ji Yongshan
 * 
 */
public interface TenantAware {

	/**
	 * 获取所属租户ID
	 * 
	 * @return
	 */
	public Long getTenantId();
}
