package com.jiongsoft.cocit.service;

public interface UserService {
	/**
	 * 获取用户类型：用户类型表示用户来自那张实体表。
	 * <p>
	 * 如“后台管理员、网站注册会员”等。
	 * 
	 * @return 用户实体表ID
	 */
	Long getType();

	/**
	 * 获取用户角色
	 * 
	 * @return {@link SecurityManager}.ROLE_XXX
	 */
	byte getRoleType();
}
