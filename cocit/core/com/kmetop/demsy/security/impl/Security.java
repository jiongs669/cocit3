package com.kmetop.demsy.security.impl;

import static com.kmetop.demsy.Demsy.bizEngine;
import static com.kmetop.demsy.Demsy.moduleEngine;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_LIB_ENCODER;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_DEMSY_SOFT;
import static com.kmetop.demsy.comlib.LibConst.F_CODE;
import static com.kmetop.demsy.comlib.LibConst.F_SOFT_ID;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.orm.expr.CndType;
import com.jiongsoft.cocit.orm.expr.CombCndExpr;
import com.jiongsoft.cocit.orm.expr.Expr;
import com.jiongsoft.cocit.orm.expr.SimpleCndExpr;
import com.jiongsoft.cocit.service.SecurityManager;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.LibConst;
import com.kmetop.demsy.comlib.biz.IBizSystem;
import com.kmetop.demsy.comlib.biz.field.Dataset;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.entity.IEncryption;
import com.kmetop.demsy.comlib.security.IModule;
import com.kmetop.demsy.comlib.security.IPermission;
import com.kmetop.demsy.comlib.security.IRealm;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.engine.RootUserFactory;
import com.kmetop.demsy.lang.Cls;
import com.kmetop.demsy.lang.Obj;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.orm.IOrm;
import com.kmetop.demsy.security.ILogin;
import com.kmetop.demsy.security.IPasswordEncoder;
import com.kmetop.demsy.security.IRootUserFactory;
import com.kmetop.demsy.security.ISecurity;
import com.kmetop.demsy.security.SecurityException;
import com.kmetop.demsy.security.UnloginException;

public class Security implements ISecurity {
	protected static Log log = Logs.getLog(Security.class);

	// 依赖注入
	private IPasswordEncoder defaultPasswordEncoder;

	private IRootUserFactory rootUserFactory = new RootUserFactory();

	// <softID,<moduleID, Permission>>
	private Map<Long, Map<Long, List<PermissionItem>>> allPermissions = new HashMap();

	private Map<Long, Map<String, PermissionItem>> dynamicPermissions = new HashMap();

	private IOrm orm() {
		return Demsy.orm();
	}

	// ===============================================================================================================
	// 登录相关的API实现
	// ===============================================================================================================
	public IPasswordEncoder getPwdEncoder(Long encoder, IPasswordEncoder defaultPE) {
		if (encoder == null || encoder <= 0) {
			return defaultPE;
		}
		synchronized (Security.class) {
			IPasswordEncoder pwdEncoder = null;
			Class<IPasswordEncoder> encoderClass = null;
			try {
				IEncryption strategy = (IEncryption) Demsy.orm().load(bizEngine.getStaticType(BIZSYS_DEMSY_LIB_ENCODER), Expr.eq(F_CODE, "" + encoder).or(Expr.eq(LibConst.F_ID, encoder)));
				if (strategy != null) {
					encoderClass = Cls.forName(strategy.getEncodeClass());
				}
			} catch (Throwable e) {
				log.errorf("加载密码加密器类出错! [encoder=%s]详细信息： %s", encoder, e);
			}
			if (encoderClass != null) {
				try {
					pwdEncoder = encoderClass.newInstance();
				} catch (Throwable e1) {
					log.errorf("创建密码加密器实例出错! 详细信息： %s", e1);
				}
			}
			return pwdEncoder;
		}
	}

	@Override
	public String encrypt(String username, String rawPwd, Long encoder) {
		return getPwdEncoder(encoder, defaultPasswordEncoder).encodePassword(rawPwd, username);
	}

	protected String genLoginKey(IDemsySoft app) {
		if (app == null) {
			return ILogin.SESSION_KEY_LOGIN_INFO;
		}
		return app.getId() + "." + ILogin.SESSION_KEY_LOGIN_INFO;
	}

	@Override
	public ILogin login(HttpServletRequest request, IDemsySoft app, String realm, String username, String password) throws SecurityException {
		HttpSession session = request.getSession();
		LoginImpl login = new LoginImpl(this, request, app, realm, username, password);
		session.setAttribute(genLoginKey(app), login);
		if (login.getRoleType() > 0) {
			session.setAttribute(ILogin.SESSION_KEY_USER_ROLE, "" + login.getRoleType());
		}

		return login;
	}

	@Override
	public ILogin login(HttpServletRequest request, IDemsySoft app) {
		return (ILogin) request.getSession().getAttribute(genLoginKey(app));
	}

	@Override
	public ILogin logout(HttpServletRequest request, IDemsySoft app) {
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
	public IUser checkUser(IDemsySoft soft, String realmCode, String username, String pwd) throws SecurityException {
		log.debugf("获取用户......[soft=%s, realm=%s, user=%s]", soft, realmCode, username);

		// 获取超级用户
		IUser user = null;
		if (Str.isEmpty(realmCode)) {
			user = getRootUser(username);
			if (user == null) {
				user = (IUser) orm().load(bizEngine.getStaticType(BIZSYS_DEMSY_SOFT), Expr.eq(F_CODE, username));
			}
		} else {
			IRealm realm = moduleEngine.getRealm(soft, realmCode);
			if (realm != null) {
				IBizSystem sys = moduleEngine.getSystem(realm.getUserModule());
				if (sys != null) {
					Class type = bizEngine.getType(sys);
					if (!IUser.class.isAssignableFrom(type)) {
						throw new SecurityException("安全策略中的用户模块非法! [%s]", realmCode);
					}
					user = (IUser) orm().load(type, Expr.eq(F_SOFT_ID, soft.getId()).and(Expr.eq(F_CODE, username)));
				}
			}
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
		IPasswordEncoder encoder = getPwdEncoder(user.getPwdEncoder(), defaultPasswordEncoder);
		if (!encoder.isValidPassword(user.getPassword(), pwd, username)) {
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
	public boolean allowVisitModule(IModule module, boolean igloreDynamic) {
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
			Map dynitems = this.dynamicPermissions.get(me.getSoft().getId());
			if (dynitems != null) {
				Iterator<PermissionItem> it = dynitems.values().iterator();
				while (it.hasNext()) {
					PermissionItem p = it.next();
					if (module.getId().equals(p.moduleID) && match(login, p)) {
						return true;
					}
				}
			}
		}

		// 数据库授权
		List<PermissionItem> items = this.getModulePermissions(me.getSoft().getId(), module.getId());
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
		Map<String, PermissionItem> map = this.dynamicPermissions.get(Demsy.me().getSoft().getId());
		if (map == null) {
			map = new HashMap();
			dynamicPermissions.put(Demsy.me().getSoft().getId(), map);
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

	public CndExpr getDataFilter(IModule module) {
		Demsy me = Demsy.me();
		ILogin login = me.login();
		if (login == null)
			return null;

		if (login.getRoleType() >= SecurityManager.ROLE_ADMIN_ROOT) {
			return null;
		}

		List<CndExpr> exprs = new LinkedList();
		List<PermissionItem> items = this.getModulePermissions(me.getSoft().getId(), module.getId());
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

	public CndExpr getFkDataFilter(IModule module, String fkField) {
		Demsy me = Demsy.me();
		ILogin login = me.login();
		if (login == null)
			return null;

		if (login.getRoleType() >= SecurityManager.ROLE_ADMIN_ROOT) {
			return null;
		}

		List<CndExpr> exprs = new LinkedList();
		List<PermissionItem> items = this.getModulePermissions(me.getSoft().getId(), module.getId());
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
		IOrm orm = orm();
		Demsy me = Demsy.me();

		IBizSystem sys = bizEngine.getSystem(LibConst.BIZSYS_ADMIN_PERMISSION);
		Class type = bizEngine.getType(sys);

		Map<Long, List<PermissionItem>> softPermissions = new HashMap();

		List<IPermission> permissions = orm.query(type, Expr.eq(F_SOFT_ID, me.getSoft()));
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
