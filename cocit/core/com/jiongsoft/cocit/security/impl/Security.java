package com.jiongsoft.cocit.security.impl;

import static com.kmjsoft.cocit.Demsy.entityModuleManager;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_SOFT;
import static com.kmjsoft.cocit.entity.EntityConst.F_CODE;
import static com.kmjsoft.cocit.entity.EntityConst.F_TENANT_OWNER_GUID;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jiongsoft.cocit.entitydef.field.Dataset;
import com.jiongsoft.cocit.lang.Obj;
import com.jiongsoft.cocit.lang.Str;
import com.jiongsoft.cocit.log.Log;
import com.jiongsoft.cocit.log.Logs;
import com.jiongsoft.cocit.security.ILogin;
import com.jiongsoft.cocit.security.IPasswordEncoder;
import com.jiongsoft.cocit.security.IRootUserFactory;
import com.jiongsoft.cocit.security.ISecurity;
import com.jiongsoft.cocit.security.SecurityException;
import com.jiongsoft.cocit.security.UnloginException;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.EntityConst;
import com.kmjsoft.cocit.entity.module.IEntityModule;
import com.kmjsoft.cocit.entity.security.IFunMenu;
import com.kmjsoft.cocit.entity.security.IPermission;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.entity.security.IUser;
import com.kmjsoft.cocit.entityengine.module.impl.RootUserFactory;
import com.kmjsoft.cocit.entityengine.service.SecurityManager;
import com.kmjsoft.cocit.orm.ExtOrm;
import com.kmjsoft.cocit.orm.expr.CndExpr;
import com.kmjsoft.cocit.orm.expr.CndType;
import com.kmjsoft.cocit.orm.expr.CombCndExpr;
import com.kmjsoft.cocit.orm.expr.Expr;
import com.kmjsoft.cocit.orm.expr.SimpleCndExpr;

public class Security implements ISecurity {
	protected static Log log = Logs.getLog(Security.class);

	// 依赖注入
	private IPasswordEncoder defaultPasswordEncoder;

	private IRootUserFactory rootUserFactory = new RootUserFactory();

	// <softID,<moduleID, Permission>>
	private Map<Long, Map<Long, List<PermissionItem>>> allPermissions = new HashMap();

	private Map<Long, Map<String, PermissionItem>> dynamicPermissions = new HashMap();

	private ExtOrm orm() {
		return Demsy.orm();
	}

	// ===============================================================================================================
	// 登录相关的API实现
	// ===============================================================================================================
	public IPasswordEncoder getPwdEncoder(Long encoder, IPasswordEncoder defaultPE) {
		return defaultPE;
	}

	@Override
	public String encrypt(String username, String rawPwd) {
		return defaultPasswordEncoder.encodePassword(rawPwd, username);
	}

	protected String genLoginKey(ITenant app) {
		if (app == null) {
			return ILogin.SESSION_KEY_LOGIN_INFO;
		}
		return app.getId() + "." + ILogin.SESSION_KEY_LOGIN_INFO;
	}

	@Override
	public ILogin login(HttpServletRequest request, ITenant app, String realm, String username, String password) throws SecurityException {
		HttpSession session = request.getSession();
		LoginImpl login = new LoginImpl(this, request, app, realm, username, password);
		session.setAttribute(genLoginKey(app), login);
		if (login.getRoleType() > 0) {
			session.setAttribute(ILogin.SESSION_KEY_USER_ROLE, "" + login.getRoleType());
		}

		return login;
	}

	@Override
	public ILogin login(HttpServletRequest request, ITenant app) {
		return (ILogin) request.getSession().getAttribute(genLoginKey(app));
	}

	@Override
	public ILogin logout(HttpServletRequest request, ITenant app) {
		HttpSession session = request.getSession();
		String key = genLoginKey(app);
		ILogin login = (ILogin) session.getAttribute(key);
		session.removeAttribute(key);

		return login;
	}

	public IUser getRootUser(String username) {
		return rootUserFactory.getUser(username);
	}

	@Override
	public IUser checkUser(ITenant soft, String realmCode, String username, String pwd) throws SecurityException {
		log.debugf("获取用户......[soft=%s, realm=%s, user=%s]", soft, realmCode, username);

		// 获取超级用户
		IUser user = null;
		if (Str.isEmpty(realmCode)) {
			user = getRootUser(username);
			if (user == null) {
				user = (IUser) orm().load(entityModuleManager.getStaticType(BIZSYS_DEMSY_SOFT), Expr.eq(F_CODE, username));
			}
		} else {
			// IRealm realm = moduleEngine.getRealm(soft, realmCode);
			// if (realm != null) {
			// IEntityDefinition sys = moduleEngine.getSystem(realm.getUserModule());
			// if (sys != null) {
			// Class type = entityDefEngine.getType(sys);
			// if (!IUser.class.isAssignableFrom(type)) {
			// throw new SecurityException("安全策略中的用户模块非法! [%s]", realmCode);
			// }
			// user = (IUser) orm().load(type, Expr.eq(F_SOFT_ID, soft.getId()).and(Expr.eq(F_CODE, username)));
			// }
			// }
		}

		// 检查用户是否存在
		if (user == null) {
			throw new SecurityException("用户不存在! [%s]", username);
		}

		// 检查用户是否被禁用
		if (user.isDisabled()) {
			throw new SecurityException("用户不可用! [%s]", username);
		}

		// 检查用户是否被锁定
		if (user.isLocked()) {
			throw new SecurityException("用户已被锁定! [%s]", username);
		}

		// 检查帐号有效期
		Date now = new Date();
		Date from = user.getExpiredFrom();
		Date to = user.getExpiredTo();
		if ((from != null && now.getTime() < from.getTime()) || (to != null && now.getTime() > to.getTime())) {
			throw new SecurityException("用户有效期已过! [%s]", username);
		}

		// 检查用户密码
		if (!defaultPasswordEncoder.isValidPassword(user.getPassword(), pwd, username)) {
			throw new SecurityException("用户密码不正确! [%s]", username);
		}

		log.debugf("获取用户: 结束. [ user=%s]", username);

		return user;
	}

	public boolean isRootUser(String username) {
		return this.rootUserFactory.getUser(username) != null;
	}

	@Override
	public void checkLogin(byte roleType) throws SecurityException {
		Demsy me = Demsy.me();
		ILogin login = me.login();
		if (login == null) {
			me.request().getSession().setAttribute("lasturi", Demsy.MvcUtil.requestURI(me.request()));
			throw new UnloginException("尚未登录或登录已过期，请先登录!");
		}
		if (login.getRoleType() < roleType)
			throw new SecurityException("你没有足够的权限执行该操作!");
	}

	public IRootUserFactory getRootUserFactory() {
		return rootUserFactory;
	}

	// =========================================================================================================
	// 以上为登录相关的API实现
	// ==========================================================================================================

	// =========================================================================================================
	// 授权管理系统相关API的实现
	// ==========================================================================================================
	/**
	 * 检查当前登录用户是否有权访问指定的模块。
	 */
	public boolean allowVisitModule(IFunMenu funMenu, boolean igloreDynamic) {
		Demsy me = Demsy.me();

		ILogin login = me.login();
		if (login != null) {
			if (login.getRoleType() >= SecurityManager.ROLE_ADMIN_ROOT) {
				return true;
			}
		}
		boolean allow = false;
		boolean denied = false;

		// 动态内存授权
		if (!igloreDynamic) {
			Map dynitems = this.dynamicPermissions.get(me.getTenant().getId());
			if (dynitems != null) {
				Iterator<PermissionItem> it = dynitems.values().iterator();
				while (it.hasNext()) {
					PermissionItem p = it.next();
					if (funMenu.getId().equals(p.moduleID) && match(login, p)) {
						return true;
					}
				}
			}
		}

		// 数据库授权
		List<PermissionItem> items = this.getModulePermissions(me.getTenant().getId(), funMenu.getId());
		if (items != null) {
			for (PermissionItem p : items) {
				long now = new Date().getTime();
				if (p.expiredFrom != null && now < p.expiredFrom.getTime())
					continue;
				if (p.expiredTo != null && now > p.expiredTo.getTime())
					continue;
				if (me.login().getModule() != p.userModuleID)
					continue;

				if (!match(login, p)) {
					continue;
				}
				if (p.denied)
					denied = true;
				else
					allow = true;
			}
		}

		return allow && !denied;
	}

	@Override
	public void addPermission(String key, byte roleID, long moduleID, String action) {
		Map<String, PermissionItem> map = this.dynamicPermissions.get(Demsy.me().getTenant().getId());
		if (map == null) {
			map = new HashMap();
			dynamicPermissions.put(Demsy.me().getTenant().getId(), map);
		}
		String key1 = key + "." + action;
		// if (map.get(key1) != null) {
		// return;
		// }

		PermissionItem item = new PermissionItem();
		item.userRole = roleID;
		item.moduleID = moduleID;
		// item.actions = new String[] { action };

		map.put(key1, item);
	}

	public CndExpr getDataFilter(IFunMenu funMenu) {
		Demsy me = Demsy.me();
		ILogin login = me.login();
		if (login == null)
			return null;

		if (login.getRoleType() >= SecurityManager.ROLE_ADMIN_ROOT) {
			return null;
		}

		List<CndExpr> exprs = new LinkedList();
		List<PermissionItem> items = this.getModulePermissions(me.getTenant().getId(), funMenu.getId());
		if (items != null) {
			for (PermissionItem p : items) {
				long now = new Date().getTime();
				if (p.expiredFrom != null && now < p.expiredFrom.getTime())
					continue;
				if (p.expiredTo != null && now > p.expiredTo.getTime())
					continue;
				if (me.login().getModule() != p.userModuleID)
					continue;
				if (!match(login, p)) {
					continue;
				}

				if (p.dataFilter != null) {
					if (p.denied == false)
						exprs.add(p.dataFilter);
				}
			}
		}

		int len = exprs.size();
		if (len == 1) {
			return exprs.get(0);
		} else if (len == 0) {
			return null;
		}
		CndExpr expr = exprs.get(0);
		for (int i = 1; i < len; i++) {
			expr = expr.or(exprs.get(i));
		}
		return expr;
	}

	public CndExpr getFkDataFilter(IFunMenu funMenu, String fkField) {
		Demsy me = Demsy.me();
		ILogin login = me.login();
		if (login == null)
			return null;

		if (login.getRoleType() >= SecurityManager.ROLE_ADMIN_ROOT) {
			return null;
		}

		List<CndExpr> exprs = new LinkedList();
		List<PermissionItem> items = this.getModulePermissions(me.getTenant().getId(), funMenu.getId());
		if (items != null) {
			for (PermissionItem p : items) {
				long now = new Date().getTime();
				if (p.expiredFrom != null && now < p.expiredFrom.getTime())
					continue;
				if (p.expiredTo != null && now > p.expiredTo.getTime())
					continue;
				if (me.login().getModule() != p.userModuleID)
					continue;
				if (!match(login, p)) {
					continue;
				}
				addFkDataFilter(exprs, p.dataFilter, fkField);
			}
		}

		int len = exprs.size();
		if (len == 1) {
			return exprs.get(0);
		} else if (len == 0) {
			return null;
		}
		CndExpr expr = exprs.get(0);
		for (int i = 1; i < len; i++) {
			expr = expr.or(exprs.get(i));
		}
		return expr;
	}

	@Override
	public void clearPermissions() {
		allPermissions.clear();
	}

	/**
	 * 解析权限限制的外键表达式
	 * 
	 * @param exprs
	 * @param expr
	 * @param fkField
	 */
	private void addFkDataFilter(List exprs, CndExpr expr, String fkField) {
		if (expr instanceof SimpleCndExpr) {
			SimpleCndExpr sexpr = (SimpleCndExpr) expr;
			if (sexpr != null) {
				String prop = sexpr.getProp();
				String fk = prop;
				int dot = prop.indexOf(".");
				if (dot > -1) {
					fk = prop.substring(0, dot);
				}
				if (fk.equals(fkField)) {
					prop = prop.substring(dot + 1);
					exprs.add(new SimpleCndExpr(prop, sexpr.getType(), sexpr.getValue()));
				}
			}
		} else if (expr instanceof CombCndExpr) {
			CombCndExpr cexpr = (CombCndExpr) expr;
			CndExpr expr1 = cexpr.getExpr();
			addFkDataFilter(exprs, expr1, fkField);
			CndExpr expr2 = cexpr.getExpr2();
			addFkDataFilter(exprs, expr2, fkField);
		}
	}

	private boolean match(ILogin login, PermissionItem p) {
		if (p.userRole == SecurityManager.ROLE_ANONYMOUS)
			return true;

		// 授权给匿名用户
		if (login != null) {
			IUser user = login.getUser();
			// 授权给登录用户
			if (p.userRole == SecurityManager.ROLE_LOGIN_USER) {
				return true;
			}

			// 授权给用户角色
			if (p.userRole == login.getRoleType())
				return true;

			// 匹配用户表达式
			if (user != null && p.userFilter instanceof SimpleCndExpr) {
				SimpleCndExpr expr = (SimpleCndExpr) p.userFilter;

				String prop = expr.getProp();
				CndType type = expr.getType();
				Object value = expr.getValue();

				Object propValue = Obj.getValue(user, prop);
				if (type == CndType.in) {
					List list = (List) value;
					if (propValue != null && list.contains(propValue))
						return true;
				} else if (type == CndType.eq) {
					if (propValue != null && propValue.equals(value))
						return true;
				}
			}
		}

		return false;
	}

	/**
	 * 获取软件授权数据，按模块ID进行分组。
	 * 
	 * @param softID
	 * @return
	 */
	private Map<Long, List<PermissionItem>> getSoftPermissions(long softID) {
		Map<Long, List<PermissionItem>> softPermissions = allPermissions.get(softID);
		if (softPermissions == null) {
			softPermissions = this.loadPermissions(softID);
			allPermissions.put(softID, softPermissions);
		}
		return softPermissions;
	}

	/**
	 * 获取模块授权列表
	 * 
	 * @param softID
	 * @param moduleID
	 * @return
	 */
	private List<PermissionItem> getModulePermissions(long softID, long module) {
		Map<Long, List<PermissionItem>> softPermisstions = getSoftPermissions(softID);
		return softPermisstions.get(module);
	}

	/**
	 * 从数据库加载软件授权数据，并按模块ID进行分组。
	 * 
	 * @param softID
	 * @return
	 */
	private Map<Long, List<PermissionItem>> loadPermissions(Long softID) {
		ExtOrm orm = orm();
		Demsy me = Demsy.me();

		IEntityModule sys = entityModuleManager.getSystem(EntityConst.BIZSYS_ADMIN_PERMISSION);
		Class type = entityModuleManager.getType(sys);

		Map<Long, List<PermissionItem>> softPermissions = new HashMap();

		List<IPermission> permissions = orm.query(type, Expr.eq(F_TENANT_OWNER_GUID, me.getTenant()));
		for (IPermission p : permissions) {
			if (p.isDisabled())
				continue;

			// 创建模块许可项
			PermissionItem item = new PermissionItem();

			Dataset users = p.getUsers();
			Dataset datas = p.getDatas();
			if (users == null || datas == null) {

				// TODO: 使用 COCIT 授权策略
				continue;
			} else {
				if (users.getModule() != null)
					item.userModuleID = users.getModule().getId();
				if (datas.getModule() != null)
					item.moduleID = datas.getModule().getId();
				if (!Str.isEmpty(users.getRules()))
					item.userFilter = CndExpr.make(users.getRules());
				if (!Str.isEmpty(datas.getRules()))
					item.dataFilter = CndExpr.make(datas.getRules());
			}

			item.expiredFrom = p.getExpiredFrom();
			item.expiredTo = p.getExpiredTo();
			item.denied = p.isDenied();

			// 添加模块许可
			List<PermissionItem> modulePermissions = softPermissions.get(item.moduleID);
			if (modulePermissions == null) {
				modulePermissions = new LinkedList();
				softPermissions.put(item.moduleID, modulePermissions);
			}
			modulePermissions.add(item);
		}

		return softPermissions;
	}

}
