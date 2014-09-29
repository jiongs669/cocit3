package com.jiongsoft.cocit.service.impl.security;

import java.util.Date;
import java.util.List;

import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.orm.expr.CndType;
import com.jiongsoft.cocit.orm.expr.SimpleCndExpr;
import com.jiongsoft.cocit.service.ModuleService;
import com.jiongsoft.cocit.service.SecurityManager;
import com.jiongsoft.cocit.service.TableService;
import com.jiongsoft.cocit.service.UserService;
import com.jiongsoft.cocit.util.ObjectUtil;

/**
 * 权限：权限实体被解析成该权限然后缓存。
 * 
 * @author jiongsoft
 * 
 */
class Permission {
	/**
	 * 权限条目有效起始日期
	 */
	Date expiredFrom;

	/**
	 * 权限条目有效截止日期
	 */
	Date expiredTo;

	/**
	 * 权限类型：拒绝还是允许？true 表示拒绝，false 表示允许。
	 */
	boolean denied;

	/**
	 * 用户角色ID：与“用户模块”和“用户过滤器”互斥，用来表示当前授权项对哪些角色的用户有效，该项主要用于动态授权。可选值包括：
	 * <UL>
	 * <LI>{@link SecurityManager#ROLE_ANONYMOUS}
	 * <LI>{@link SecurityManager#ROLE_LOGIN_USER}
	 * <LI>{@link SecurityManager#ROLE_ADMIN_USER}
	 * <LI>{@link SecurityManager#ROLE_ADMIN_ROOT}
	 * <LI>{@link SecurityManager#ROLE_DP_SUPPORT}
	 * </UL>
	 */
	byte userRole = -1;

	/**
	 * 用户实体表ID：与“用户角色{@link #userRole}”互斥，用来表示用户类型，如：后台管理员、网站注册会员等。
	 */
	long userTableID;

	/**
	 * 用户群体：表示该权限被授予哪些“用户”？
	 */
	CndExpr userFilter;

	/**
	 * 功能模块ID或*，模块Code或GUID将被转换Long类型的ID；
	 */
	String module;

	/**
	 * 实体表ID或*，实体表Code或GUID将被转换Long类型的ID；
	 */
	String table;

	/**
	 * List类型操作模式列表或*；
	 */
	List<String> opModes;

	/**
	 * 数据权限：一个查询表达式，表示“用户群体”可以访问模块中的哪些数据？
	 */
	CndExpr dataFilter;

	static Permission make(byte userRole, String table) {
		Permission ret = new Permission();
		ret.userRole = userRole;
		ret.table = table;

		return ret;
	}

	/**
	 * 检查该权限是否与指定的用户匹配？
	 */
	boolean match(UserService user) {
		// 该权限是授予某角色的
		if (userRole != -1)
			return user.getRoleType() > userRole;

		// 匹配用户表达式
		if (userFilter instanceof SimpleCndExpr) {
			SimpleCndExpr expr = (SimpleCndExpr) userFilter;

			// 获取表达式值
			String prop = expr.getProp();
			CndType type = expr.getType();
			Object value = expr.getValue();

			// 获取属性字段ID值
			Object propValue = ObjectUtil.getValue(user, prop);
			if (propValue == null)
				return false;
			String id = ObjectUtil.idOrtoString(propValue);

			// 检查是否相等
			if (type == CndType.in) {
				List list = (List) value;
				for (Object item : list) {
					if (item.toString().equals(id))
						return true;
				}
			} else if (type == CndType.eq) {
				if (value.toString().equals(id))
					return true;
			}
		}

		return false;
	}

	/**
	 * 检查该权限是否与指定的模块匹配？
	 */
	boolean match(ModuleService module) {
		if (this.module.charAt(0) == '*')
			return true;

		return this.module.equals("" + module.getID());
	}

	/**
	 * 检查该权限是否与指定的模块匹配？
	 */
	boolean match(TableService table) {
		if (this.table.charAt(0) == '*')
			return true;

		return this.table.equals("" + table.getID());
	}

	/**
	 * 检查该权限是否与指定的操作模式匹配？
	 */
	boolean match(String opMode) {
		if (this.opModes.contains("*"))
			return true;

		return this.opModes.contains(opMode);
	}
}
