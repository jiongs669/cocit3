package com.jiongsoft.cocit.actions;

import static com.jiongsoft.cocit.Demsy.security;
import static com.jiongsoft.cocit.mvc.MvcConst.MvcUtil.globalVariables;

import java.util.HashMap;
import java.util.Map;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.jiongsoft.cocit.Demsy;
import com.jiongsoft.cocit.lang.DemsyException;
import com.jiongsoft.cocit.lang.Ex;
import com.jiongsoft.cocit.lang.Status;
import com.jiongsoft.cocit.lang.Str;
import com.jiongsoft.cocit.log.Log;
import com.jiongsoft.cocit.log.Logs;
import com.jiongsoft.cocit.orm.IOrm;
import com.jiongsoft.cocit.security.ILogin;
import com.jiongsoft.cocit.security.IRootUserFactory;
import com.jiongsoft.cocit.security.SecurityException;
import com.kmjsoft.cocit.entity.EntityConst;
import com.kmjsoft.cocit.entity.security.ISystemTenant;
import com.kmjsoft.cocit.entity.security.IUser;
import com.kmjsoft.cocit.orm.expr.Expr;
import com.kmjsoft.cocit.util.StringUtil;

@Ok("json")
public class SecurityActions extends ModuleActions {
	protected static Log log = Logs.getLog(SecurityActions.class);

	@At(URL_SEC_LOGIN_FORM)
	@Ok("st:admin/login")
	public Map loginForm() {
		log.debug("访问后台登录页面...");

		Map context = new HashMap();
		context.putAll(globalVariables);
		Demsy ctx = Demsy.me();

		ISystemTenant soft = ctx.getSoft();

		if (soft != null) {
			// context.put("realmNodes", moduleEngine.makeNodesByRealm(soft));
			context.put("title", soft.getName() + "——登录");
		} else {
			context.put("title", "登录");
		}
		context.put("loginBodyBg", MvcUtil.contextPath("/themes2/images/login_body_bg.jpg"));
		context.put("loginBoxBg", MvcUtil.contextPath("/themes2/images/login_box_bg.jpg"));
		context.put("loginBtn", MvcUtil.contextPath("/themes2/images/login_btn.jpg"));

		context.put("user", ctx.loginUser());

		log.debug("访问后台登录成功.");
		return context;
	}

	@At(URL_SEC_LOGIN)
	public Status login(@Param("_loginrealm_") String realm) throws SecurityException {
		log.debugf("登录...[%s]", realm);

		Demsy ctx = Demsy.me();
		String user = ctx.request().getParameter(ILogin.PARAM_USER);
		String pwd = ctx.request().getParameter(ILogin.PARAM_PWD);
		try {
			ILogin login = security.login(ctx.request(), ctx.getSoft(), realm, user, pwd);

			String uri = (String) ctx.request().getSession().getAttribute("lasturi");
			ctx.request().getSession().removeAttribute("lasturi");

			log.debugf("登录成功. [%s]", login);
			return new Status(true, "登录成功.", uri, login);
		} catch (Throwable e) {
			String info = Ex.msg(e);
			log.debugf("登录失败! %s", info);

			return new Status(false, "登录失败: " + info);
		}
	}

	@At(URL_SEC_LOGOUT)
	public Status logout() {
		log.debugf("注销...");

		Demsy ctx = Demsy.me();
		ILogin login = security.login(ctx.request(), ctx.getSoft());

		security.logout(ctx.request(), ctx.getSoft());

		log.debugf("注销成功. [%s]", login);
		return new Status(true, "注销成功.", null, login);
	}

	@At(URL_ADMIN_CHGPWDFORM)
	@Ok("st:admin/chgpwd")
	public Map chgpwdForm() {
		Map context = new HashMap();
		context.putAll(globalVariables);
		Demsy ctx = Demsy.me();

		ISystemTenant soft = ctx.getSoft();

		if (soft != null) {
			// context.put("realmNodes", moduleEngine.makeNodesByRealm(soft));
			context.put("title", soft.getName() + "——修改密码");
		} else {
			context.put("title", "修改密码");
		}

		context.put("user", ctx.loginUser());

		return context;
	}

	@At(URL_ADMIN_CHGPWD)
	public Status chgpwd(@Param("_loginpwd_") String oldpwd, @Param("rawPassword") String pwd1, @Param("rawPassword2") String pwd2) throws SecurityException {
		Demsy ctx = Demsy.me();
		String username = ctx.username();
		try {

			if (Str.isEmpty(pwd1) || Str.isEmpty(pwd2)) {
				throw new DemsyException("请输入新密码!");
			}
			StringUtil.validatePassword(pwd1);

			IUser user = ctx.loginUser();
			if (user == null) {
				throw new DemsyException("尚未登录，请先登录!");
			}

			security.login(ctx.request(), ctx.getSoft(), ctx.login().getRealm(), username, oldpwd);

			if (security.isRootUser(username)) {
				IRootUserFactory f = security.getRootUserFactory();
				user = f.getUser(username);
				user.setRawPassword("");
				user.setRawPassword2("");
				user.setRawPassword(pwd1);
				user.setRawPassword2(pwd2);
				f.saveUser(user);
			} else {
				IOrm orm = Demsy.orm();
				user = (IUser) orm.load(user.getClass(), Expr.eq(EntityConst.F_SOFT_ID, ctx.getSoft()).and(Expr.eq(EntityConst.F_CODE, username)));
				if (user == null) {
					throw new DemsyException("登录用户不存在!");
				}
				user.setRawPassword("");
				user.setRawPassword2("");
				user.setRawPassword(pwd1);
				user.setRawPassword2(pwd2);
				orm.save(user);
			}

			return new Status(true, "修改密码成功.");
		} catch (Throwable e) {
			String info = Ex.msg(e);
			log.debugf("修改密码失败! %s", info);

			return new Status(false, "修改密码失败: " + info);
		}
	}
}
