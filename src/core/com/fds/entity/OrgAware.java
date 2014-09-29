package com.fds.entity;

/**
 * 组织机构敏感的：该类的所有子类将自动带有“所属机构”字段，用户访问数据的时候，可以根据权限设置使其只能访问自己所在部门的数据。
 * 
 * @author Ji Yongshan
 * 
 */
public interface OrgAware {

	/**
	 * 获取所属机构ID
	 * 
	 * @return
	 */
	public Long getOrgId();

}
