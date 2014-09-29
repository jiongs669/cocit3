package com.jiongsoft.cocit.service.impl.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.entity.PermissionEntity;
import com.jiongsoft.cocit.orm.Orm;
import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.orm.expr.CombCndExpr;
import com.jiongsoft.cocit.orm.expr.Expr;
import com.jiongsoft.cocit.orm.expr.SimpleCndExpr;
import com.jiongsoft.cocit.service.ModuleService;
import com.jiongsoft.cocit.service.SecurityManager;
import com.jiongsoft.cocit.service.ServiceFactory;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.service.TableService;
import com.jiongsoft.cocit.service.UserService;
import com.jiongsoft.cocit.util.Json;
import com.jiongsoft.cocit.util.StringUtil;
import com.kmetop.demsy.Demsy;

public class SecurityManagerImpl implements SecurityManager {
	private SoftService softService;

	private Orm orm;

	private ServiceFactory serviceFactory;

	private Class<PermissionEntity> permissionEntityType;

	private Map<String, List<Permission>> permissionsModuleMap;

	private Map<String, List<Permission>> permissionsTableMap;

	// 临时权限：通常有运行时的程序动态授予的权限。
	private Map<String, Permission> tempPermissionsMap;

	public SecurityManagerImpl(SoftService softService) {
		this.permissionsModuleMap = new HashMap();
		this.permissionsTableMap = new HashMap();
		this.tempPermissionsMap = new HashMap();

		this.softService = softService;
		this.orm = this.softService.getOrm();
		this.serviceFactory = Cocit.getServiceFactory();
		this.permissionEntityType = Cocit.getBeanFactory().getPermissionEntityType();
	}

	@Override
	public void clearPermissions() {
		permissionsModuleMap.clear();
		permissionsTableMap.clear();
		tempPermissionsMap.clear();
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

	/**
	 * 获取实体表授权列表
	 * 
	 * @param module
	 * @return
	 */
	private List<Permission> getPermissions(TableService table) {
		if (permissionsModuleMap.size() == 0 && this.permissionsTableMap.size() == 0)
			this.loadPermissions();

		List ret = new ArrayList();

		List list = this.permissionsTableMap.get("" + table.getID());
		if (list != null) {
			for (Object obj : list)
				ret.add(obj);
		}
		list = this.permissionsTableMap.get("*");
		if (list != null) {
			for (Object obj : list)
				ret.add(obj);
		}

		return ret;
	}

	/**
	 * 获取模块授权列表
	 * 
	 * @param module
	 * @return
	 */
	private List<Permission> getPermissions(ModuleService module) {
		if (permissionsModuleMap.size() == 0 && this.permissionsTableMap.size() == 0)
			this.loadPermissions();

		List ret = new ArrayList();

		List list = this.permissionsModuleMap.get("" + module.getID());
		if (list != null) {
			for (Object obj : list)
				ret.add(obj);
		}
		list = this.permissionsModuleMap.get("*");
		if (list != null) {
			for (Object obj : list)
				ret.add(obj);
		}

		return ret;
	}

	/**
	 * 从数据库加“权限实体”并解析成权限服务对象缓存。
	 */
	private void loadPermissions() {
		List<PermissionEntity> entityList = orm.query(this.permissionEntityType, Expr.eq("softID", softService.getID()));
		TableService tmpTable;
		String str;
		List<Permission> permissions;
		Permission perm;
		List<String> funcRulesList;
		String[] funcRules;
		for (PermissionEntity entity : entityList) {
			if (entity.isDisabled())
				continue;

			funcRulesList = Json.fromJson(entity.getFuncRule());
			for (String funcRuleStr : funcRulesList) {
				// 创建模块许可项
				perm = new Permission();

				/*
				 * 设置基本信息
				 */
				perm.expiredFrom = entity.getExpiredFrom();
				perm.expiredTo = entity.getExpiredTo();
				perm.denied = entity.isDenied();

				/*
				 * 用户实体表ID和用户过滤器
				 */
				str = entity.getUserType();
				try {
					perm.userTableID = Long.parseLong(str);
				} catch (Throwable e) {
					tmpTable = serviceFactory.getTable(str);
					perm.userTableID = tmpTable.getID();
				}

				/*
				 * 解析“用户群体”过滤器
				 */
				perm.userFilter = CndExpr.make(entity.getUserRule());

				//
				funcRules = StringUtil.toArray(funcRuleStr, ":");

				/*
				 * 解析功能权限
				 */
				// 解析模块
				if (funcRules.length > 0)
					str = funcRules[0];
				else
					str = "";

				if (StringUtil.isNil(str)) {
					perm.module = "";
				} else if (str.charAt(0) == '*') {
					perm.module = "*";
				} else {
					// 解析模块ID
					try {
						perm.module = "" + Long.parseLong(str);
					} catch (Throwable e) {
						ModuleService module = serviceFactory.getModule(str);
						if (module == null)
							perm.module = "";
						else
							perm.module = "" + module.getID();
					}
				}

				// 解析实体表
				if (funcRules.length > 1)
					str = funcRules[1];
				else
					str = "";

				if (StringUtil.isNil(str) || str.charAt(0) == '*') {
					if (StringUtil.isNil(perm.module) || perm.module.charAt(0) == '*') {
						perm.table = str;
					} else {
						// 通过模块查找绑定的“实体表”
						ModuleService module = serviceFactory.getModule(Long.parseLong(perm.module));
						TableService table = module.getTable();
						if (table != null)
							perm.table = "" + table.getID();
						else
							perm.table = "";
					}
				} else {
					// 解析实体表ID
					try {
						perm.table = "" + Long.parseLong(str);
					} catch (Throwable e) {
						TableService table = serviceFactory.getTable(str);
						if (table != null)
							perm.table = "" + table.getID();
						else
							perm.table = "";
					}
				}

				// 授权必须指定“模块”或“实体表/报表/流程”
				if (StringUtil.isNil(perm.module) || StringUtil.isNil(perm.table)) {
					continue;
				}

				// 解析表操作
				if (funcRules.length > 2)
					str = funcRules[2];
				else
					str = "";
				perm.opModes = StringUtil.toList(str, ",");

				/*
				 * 解析数据权限
				 */
				perm.dataFilter = CndExpr.make(entity.getDataRule());

				/*
				 * 缓存权限条目
				 */
				// 加入模块权限缓存
				if (!StringUtil.isNil(perm.module)) {
					permissions = this.permissionsModuleMap.get(perm.module);
					if (permissions == null) {
						permissions = new ArrayList();
						permissionsModuleMap.put(perm.module, permissions);
					}
					permissions.add(perm);
				}
				// 加入表权限缓存
				if (!StringUtil.isNil(perm.table)) {
					permissions = this.permissionsTableMap.get(perm.table);
					if (permissions == null) {
						permissions = new ArrayList();
						permissionsModuleMap.put(perm.table, permissions);
					}
					permissions.add(perm);
				}
			}
		}

	}

	@Override
	public boolean check(UserService user, ModuleService module) {
		boolean allow = false;
		boolean denied = false;

		// 获取模块授权列表
		List<Permission> items = this.getPermissions(module);
		if (items == null)
			return false;

		for (Permission perm : items) {

			// 检查有效期
			long now = new Date().getTime();
			if (perm.expiredFrom != null && now < perm.expiredFrom.getTime())
				continue;
			if (perm.expiredTo != null && now > perm.expiredTo.getTime())
				continue;
			// 检查模块
			if (!perm.match(module))
				continue;
			// 检查用户是否
			if (!perm.match(user))
				continue;

			if (perm.denied) {
				denied = true;
				break;
			} else
				allow = true;
		}

		return allow && !denied;
	}

	@Override
	public boolean check(UserService user, TableService table, String opMode, Long... data) {
		boolean allow = false;
		boolean denied = false;

		// 获取模块授权列表
		List<Permission> items = this.getPermissions(table);
		if (items == null)
			return false;

		for (Permission perm : items) {

			// 检查有效期
			long now = new Date().getTime();
			if (perm.expiredFrom != null && now < perm.expiredFrom.getTime())
				continue;
			if (perm.expiredTo != null && now > perm.expiredTo.getTime())
				continue;
			// 检查表
			if (!perm.match(table))
				continue;
			// 检查操作
			if (!StringUtil.isNil(opMode) && !perm.match(opMode))
				continue;
			// 检查用户
			if (!perm.match(user))
				continue;

			if (perm.denied) {
				denied = true;
				break;
			} else
				allow = true;
		}

		return allow && !denied;
	}

	@Override
	public CndExpr getDataFilter(UserService user, TableService table) {
		// 超级用户
		if (user.getRoleType() >= SecurityManager.ROLE_ADMIN_ROOT)
			return null;

		List<Permission> items = this.getPermissions(table);
		if (items == null)
			return null;

		List<CndExpr> exprs = new LinkedList();
		for (Permission perm : items) {
			// 检查有效期
			long now = new Date().getTime();
			if (perm.expiredFrom != null && now < perm.expiredFrom.getTime())
				continue;
			if (perm.expiredTo != null && now > perm.expiredTo.getTime())
				continue;

			// 权限是否与指定的表匹配
			if (!perm.match(table))
				continue;
			// 权限是否与指定的用户匹配
			if (!perm.match(user))
				continue;

			if (perm.dataFilter != null)
				exprs.add(perm.dataFilter);

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
	public CndExpr getFkDataFilter(UserService user, TableService table, String fkField) {
		if (user.getRoleType() >= SecurityManager.ROLE_ADMIN_ROOT)
			return null;

		List<Permission> items = this.getPermissions(table);
		if (items == null)
			return null;

		List<CndExpr> exprs = new LinkedList();
		for (Permission perm : items) {
			// 检查有效期
			long now = new Date().getTime();
			if (perm.expiredFrom != null && now < perm.expiredFrom.getTime())
				continue;
			if (perm.expiredTo != null && now > perm.expiredTo.getTime())
				continue;

			// 权限是否与指定的表匹配
			if (!perm.match(table))
				continue;
			// 权限是否与指定的用户匹配
			if (!perm.match(user))
				continue;

			addFkDataFilter(exprs, perm.dataFilter, fkField);
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

	public void authorize(String key, byte userRole, String tableCode) {
		if (tempPermissionsMap.containsKey(key))
			return;

		if (StringUtil.isNil(tableCode))
			return;

		String tableID = "";
		try {
			tableID = "" + Long.parseLong(tableCode);
		} catch (Throwable e) {
			TableService table = serviceFactory.getTable(tableCode);
			if (table != null)
				tableID = "" + table.getID();
			else
				tableID = "";
		}

		Permission p = Permission.make(userRole, tableID);

		tempPermissionsMap.put(key, p);
	}

	@Override
	public void checkLoginRole(byte roleType) {
		Demsy.security.checkLogin(roleType);
	}

}
