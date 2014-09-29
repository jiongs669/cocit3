package com.kmetop.demsy.security.impl;

import static com.kmetop.demsy.Demsy.moduleEngine;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jiongsoft.cocit.service.SecurityManager;
import com.kmetop.demsy.comlib.entity.IDemsySoft;
import com.kmetop.demsy.comlib.security.IAdminUser;
import com.kmetop.demsy.comlib.security.IRealm;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.lang.Str;
import com.kmetop.demsy.log.Log;
import com.kmetop.demsy.log.Logs;
import com.kmetop.demsy.security.ILogin;
import com.kmetop.demsy.security.ISecurity;
import com.kmetop.demsy.security.SecurityException;

public class LoginImpl implements ILogin {
	protected static Log log = Logs.getLog(LoginImpl.class);

	private Map<String, Object> cachedData = new HashMap();

	private IUser user;

	private IDemsySoft soft;

	private long module;

	private String realm;

	private String username;

	private byte roleType;

	private double clientWidth = 1024.0;

	private double clientHeight = 768.0;

	private ISecurity security;

	LoginImpl(ISecurity security, HttpServletRequest request, IDemsySoft app, String realm, String username, String password) throws SecurityException {
		log.debug("创建登录信息对象......");

		this.security = security;
		this.soft = app;
		this.realm = realm;
		this.username = username;

		if (!Str.isEmpty(realm)) {
			IRealm realmObj = moduleEngine.getRealm(soft, realm);
			if (realmObj != null && realmObj.getUserModule() != null)
				this.module = realmObj.getUserModule().getId();
		}

		if (Str.isEmpty(username)) {
			throw new SecurityException("检查登录用户失败! 未指定登录帐号.");
		} else {
			user = security.checkUser(soft, realm, username, password);

			this.initRole();

			log.debug("创建登录信息对象: 成功.");
		}

		//
		String sClientWidth = request.getParameter(PARAM_CLIENT_WIDTH);
		String sClientHeight = request.getParameter(PARAM_CLIENT_HEIGHT);
		if (!Str.isEmpty(sClientWidth)) {
			try {
				clientWidth = Double.parseDouble(sClientWidth);
			} catch (Throwable e) {

			}
		}
		if (!Str.isEmpty(sClientHeight)) {
			try {
				clientHeight = Double.parseDouble(sClientHeight);
			} catch (Throwable e) {

			}
		}
	}

	public LoginImpl(IDemsySoft app, IUser user) {
		this.soft = app;
		this.username = user.getUsername();

		this.user = user;
		this.initRole();
	}

	private void initRole() {
		if (security.isRootUser(username)) {
			roleType = SecurityManager.ROLE_DP_SUPPORT;
		} else if (user instanceof IAdminUser) {
			IAdminUser admin = (IAdminUser) user;
			if (admin.getRole() != null)
				roleType = admin.getRole().getType();
			else
				roleType = SecurityManager.ROLE_ADMIN_USER;
		} else if (user instanceof IDemsySoft) {
			roleType = SecurityManager.ROLE_ADMIN_ROOT;
		}
	}

	public IUser getUser() {
		return user;
	}

	public IDemsySoft getApp() {
		return soft;
	}

	public String getRealm() {
		return realm;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public Object get(String key) {
		return cachedData.get(key);
	}

	@Override
	public ILogin set(String key, Object value) {
		cachedData.put(key, value);
		return this;
	}

	public byte getRoleType() {
		return roleType;
	}

	public long getModule() {
		return module;
	}

	@Override
	public void setUser(IUser user) {
		this.user = user;
	}

	@Override
	public double getClientWidth() {
		return clientWidth;
	}

	@Override
	public double getClientHeight() {
		return clientHeight;
	}

	@Override
	public double getBodyWidth() {
		return clientWidth * 0.8;
	}

}
