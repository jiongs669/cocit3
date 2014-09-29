package com.jiongsoft.cocit.service;

import com.jiongsoft.cocit.orm.expr.CndExpr;

/**
 * 安全管理器：每个软件实体有一个对应的安全管理器。
 * 
 * @author yongshan.ji
 * 
 */
public interface SecurityManager {
	/**
	 * 匿名用户：表示网站普通访客
	 */
	public static final byte ROLE_ANONYMOUS = 0;

	/**
	 * 登录用户：表示网站注册会员已经登录
	 */
	public static final byte ROLE_LOGIN_USER = 1;

	/**
	 * 普通管理员：后台普通管理员
	 */
	public static final byte ROLE_ADMIN_USER = 90;

	/**
	 * 超级管理员：后台超级管理员
	 */
	public static final byte ROLE_ADMIN_ROOT = 100;

	/**
	 * 开发方技术支持
	 */
	public static final byte ROLE_DP_SUPPORT = 127;

	/**
	 * 检查“用户”是否有权访问指定的“模块”？
	 * <p>
	 * 通常可以调用该方法检查用户是否可以看到模块功能菜单。
	 * 
	 * @param user
	 *            用户
	 * @param module
	 *            模块
	 * @return 有权操作返回true，无权操作则返回false。
	 */
	public boolean check(UserService user, ModuleService module);

	/**
	 * 检查“用户”是否有权“操作”指定的“实体表”数据？
	 * 
	 * @param user
	 *            用户服务对象
	 * @param table
	 *            实体表
	 * @param opMode
	 *            操作模式，允许为空。
	 * @param data
	 *            数据：即实体表中的数据ID数组
	 * @return 有权操作返回true，无权操作则返回false。
	 */
	public boolean check(UserService user, TableService table, String opMode, Long... data);

	/**
	 * 获取“实体表”数据过滤器：用来强制作为“实体表”的数据查询条件。
	 * <p>
	 * 当“用户”管理某个模块“实体表”时，在主界面GRID中所能看到的数据记录将无条件受该查询条件的限制。
	 * 
	 * @param user
	 *            用户
	 * @param table
	 *            实体表
	 * @return 条件表达式对象
	 */
	public CndExpr getDataFilter(UserService user, TableService table);

	/**
	 * 获取字段外键数据过滤器：通常用来过滤导航树，或Combo数据选项。
	 * 
	 * @param user
	 * @param table
	 * @param fkField
	 * @return
	 */
	public CndExpr getFkDataFilter(UserService user, TableService table, String fkField);

	/**
	 * 为指定的用户角色授权访问特定表的权限
	 * 
	 * @param key
	 *            动态授权将被缓存，该值作为缓存的KEY
	 * @param userRole
	 *            用户角色：参见{@link #ROLE_ADMIN_ROOT}等项。
	 * @param table
	 */
	public void authorize(String key, byte userRole, String table);

	/**
	 * 清除所有权限缓存
	 */
	public void clearPermissions();
	
	void checkLoginRole(byte roleType);
}
