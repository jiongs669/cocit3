package com.kmetop.demsy.security;

import javax.servlet.http.HttpServletRequest;

import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IUser;

/**
 * DEMSY安全管理器接口
 * 
 * @author yongshan.ji
 */
public interface ISecurity {

	/**
	 * 获取超级用户工厂
	 * 
	 * @return
	 */
	IRootUserFactory getRootUserFactory();

	/**
	 * 判断指定的用户是否是超级用户
	 * 
	 * @param username
	 * @return
	 */
	boolean isRootUser(String username);

	/**
	 * 判断当前登录用户是否有权访问指定的模块
	 * 
	 * @param moduleID
	 * @param igloreDynamic
	 * @return
	 */
	boolean allowVisitModule(IModule module, boolean igloreDynamic);

	/**
	 * 检查当前登录用户是否具有指定的角色权限
	 * 
	 * @param roleType
	 * @throws SecurityException
	 */
	void checkLogin(byte roleType) throws SecurityException;

	/**
	 * 对原始密文加密
	 * 
	 * @param salt
	 * 
	 * @param rawpwd
	 *            原始密码
	 * @param encoder
	 *            加密器ID，null——表示使用默认加密器
	 * @return 返回加密后的密文
	 */
	String encrypt(String salt, String rawpwd, Long encoder);

	// ***登录 API***
	/**
	 * 登录应用系统： 检查登录账号和密码是否匹配，如果匹配则将用户账户对象保存在HTTP SESSION中。
	 * <p>
	 * 已经登录的账户会被先注销然后再登录
	 * 
	 * @param app
	 *            应用系统
	 * @param realm
	 *            用户类型
	 * @param username
	 *            用户账号
	 * @param password
	 *            用户密码
	 * @return 返回登录信息
	 * @throws SecurityException
	 *             将会抛出安全异常。如用户名不存在；用户名密码不匹配；用户已被锁定等
	 */
	ILogin login(HttpServletRequest request, IDemsySoft app, String realm, String username, String password) throws SecurityException;

	/**
	 * 获取登录信息
	 * 
	 * @param app
	 *            应用系统
	 * @return 返回Session中的登录信息
	 */
	ILogin login(HttpServletRequest request, IDemsySoft app);

	/**
	 * 注销已经登录的用户会话对象
	 * 
	 * @param app
	 *            应用系统
	 * @return 返回登录信息
	 */
	ILogin logout(HttpServletRequest request, IDemsySoft app);

	// ***用户 API***

	/**
	 * 获取应用中帐号和密码匹配的用户
	 * <UL>
	 * <LI>应用系统——表示要检查是哪个应用系统中的用户？(DEMSY平台中可同时运行多个应用系统)
	 * <LI>登录类型——表示实现ILogin接口的业务系统编号。(应用系统支持有多种用户类型如：员工、会员、管理员等)
	 * </UL>
	 * 
	 * @param soft
	 *            应用系统。
	 * @param realm
	 *            用户类型
	 * @param username
	 *            用户账号
	 * @param password
	 *            用户密码
	 * @return 用户对象
	 * @throws SecurityException
	 *             将会抛出安全异常。如用户名不存在；用户名密码不匹配；用户已被锁定等
	 */
	IUser checkUser(IDemsySoft soft, String realm, String username, String password) throws SecurityException;

	/**
	 * 获取指定模块的外键字典数据过滤器，用来过滤模块外键字段的数据选项。
	 * 
	 * @param moduleID
	 * @param fkField
	 * @return
	 */
	CndExpr getFkDataFilter(IModule module, String fkField);

	/**
	 * 获取模块数据过滤器：用来过滤模块GRID中的数据。即表示当前登录用户只能访问权限范围内的数据。
	 * 
	 * @param moduleID
	 * @return
	 */
	CndExpr getDataFilter(IModule module);

	/**
	 * 动态授权指定角色的用户可以访问的哪些模块操作。
	 * 
	 * @param userRole
	 * @param moduleID
	 * @param actions
	 */
	void addPermission(String key, byte roleID, long moduleID, String action);

	/**
	 * 清楚缓存的权限许可
	 */
	void clearPermissions();

}
