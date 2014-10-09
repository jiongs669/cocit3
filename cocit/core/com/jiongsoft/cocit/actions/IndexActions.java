package com.jiongsoft.cocit.actions;

import static com.jiongsoft.cocit.mvc.MvcConst.VW_BIZ;
import static com.kmjsoft.cocit.Demsy.me;
import static com.kmjsoft.cocit.Demsy.moduleManager;
import static com.kmjsoft.cocit.Demsy.security;
import static com.kmjsoft.cocit.Demsy.uIEngine;

import java.util.HashMap;
import java.util.Map;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.jiongsoft.cocit.config.TenantPreferenceManager;
import com.jiongsoft.cocit.entitydef.field.Upload;
import com.jiongsoft.cocit.lang.ConfigException;
import com.jiongsoft.cocit.lang.DemsyException;
import com.jiongsoft.cocit.lang.Ex;
import com.jiongsoft.cocit.lang.Str;
import com.jiongsoft.cocit.mvc.MvcConst;
import com.jiongsoft.cocit.mvc.ui.IUIView;
import com.jiongsoft.cocit.mvc.ui.model.UIWidgetModel;
import com.jiongsoft.cocit.mvc.ui.widget.UIPageView;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.security.IAdminUser;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.entity.security.IUser;
import com.kmjsoft.cocit.entity.webdef.IPage;
import com.kmjsoft.cocit.entityengine.service.SecurityManager;

@Ok(VW_BIZ)
@Fail(VW_BIZ)
public class IndexActions extends ModuleActions implements MvcConst {

	@At(URL_INDEX)
	public IUIView index() throws DemsyException {
		log.debug("访问主页...");

		IPage indexPage = Demsy.uIEngine.loadIndexPage();
		if (indexPage == null)
			throw new DemsyException("网站开发中...");

		UIPageView ui = (UIPageView) uIEngine.makePageView(indexPage, null, null, null);
		ui.set("loadBlockUrl", MvcUtil.contextPath(URL_UI_BLOCK, ""));

		log.debugf("访问主页结束. %s", ui);

		return ui;
	}

	/**
	 * 后台首页
	 * 
	 * @return
	 */
	@At(URL_ADMIN)
	@Ok("st:admin/index")
	@Fail("redirect:" + URL_SEC_LOGIN_FORM)
	public Map admin() {
		log.debug("访问后台主页...");

		security.checkLogin(SecurityManager.ROLE_ADMIN_USER);

		Map context = new HashMap();
		context.putAll(MvcUtil.globalVariables);

		ITenant soft = me().getTenant();

		context.put("title", soft.getName() + "——后台管理系统");
		context.put("topUrl", MvcUtil.contextPath(URL_ADMIN_TOP, ""));
		context.put("leftUrl", MvcUtil.contextPath(URL_ADMIN_MENU, ""));

		String rightUrl = "#";
		IUser user = Demsy.me().loginUser();
		if (user instanceof IAdminUser) {
			rightUrl = ((IAdminUser) user).getLatestUrl();
		} else {
			rightUrl = "/config";
		}
		context.put("rightUrl", Str.isEmpty(rightUrl) ? "#" : rightUrl);

		TenantPreferenceManager config = TenantPreferenceManager.me();
		context.put("topHeight", config.getInt(TenantPreferenceManager.ADMIN_UI_TOP_HEIGHT, 95) + 33);
		context.put("leftWidth", config.get(TenantPreferenceManager.ADMIN_UI_LEFT_WIDTH, "20%"));

		log.debug("访问后台主页成功.");

		return context;
	}

	@At(URL_ADMIN_TOP)
	@Ok("st:admin/top")
	@Fail("redirect:" + URL_SEC_LOGIN_FORM)
	public Map top() throws DemsyException {
		log.debug("访问后台顶部...");

		security.checkLogin(SecurityManager.ROLE_ADMIN_USER);

		Map context = new HashMap();
		context.putAll(MvcUtil.globalVariables);

		ITenant soft = me().getTenant();
		Upload logo = soft.getLogo();
		context.put("logo", MvcUtil.contextPath((logo == null || Str.isEmpty(logo.toString())) ? "/themes2/images/index_top_logo.jpg" : logo.toString()));

		String rightUrl = "#";
		IUser user = Demsy.me().loginUser();
		if (user instanceof IAdminUser) {
			rightUrl = ((IAdminUser) user).getLatestUrl();
		}
		context.put("rightUrl", Str.isEmpty(rightUrl) ? "#" : rightUrl);

		TenantPreferenceManager config = TenantPreferenceManager.me();
		context.put("topHeight", config.get(TenantPreferenceManager.ADMIN_UI_TOP_HEIGHT, "95"));

		context.put("user", me().loginUser());
		String sessionID = me().request().getRequestedSessionId();
		context.put("jsessionid", sessionID);

		log.debug("访问后台顶部成功.");
		return context;
	}

	@At(URL_ADMIN_MENU)
	public UIWidgetModel menu() throws DemsyException {
		String title = "访问后台功能菜单";
		log.debugf("%s....", title);

		try {
			security.checkLogin(SecurityManager.ROLE_ADMIN_USER);

			UIWidgetModel modelUI = uIEngine.makeFunctionMenuView(me().getTenant());
			modelUI.setDacorator(null);

			modelUI.setData(moduleManager.makeNodesByModule(me().getTenant()));
			modelUI.set("target", "body");

			log.debugf("%s成功.", title);
			return modelUI;
		} catch (ConfigException e) {
			throw e;
		} catch (SecurityException e) {
			throw e;
		} catch (Throwable e) {
			String msg = String.format("%s出错! %s", title, Ex.msg(e));
			log.error(msg, e);

			throw new DemsyException(msg);
		}
	}
}
