package com.jiongsoft.cocit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiongsoft.cocit.orm.OrmFactory;
import com.jiongsoft.cocit.service.ServiceFactory;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.sms.SmsClient;
import com.jiongsoft.cocit.ui.model.widget.WidgetModelFactory;
import com.jiongsoft.cocit.ui.render.WidgetRenderFactory;
import com.jiongsoft.cocit.util.Log;

/**
 * Cocit：组件化自定义平台，也称“CoC平台”。
 * <P>
 * <b>Cocit含义：</b>
 * <UL>
 * <LI>组件化自定义信息技术：Componentization of custom information technology；
 * <LI>组件化自定义它：Componentization of custom it，“it”包括“软件、系统、报表、流程、操作、展现视图、网站、权限 ......”；
 * <LI>全称“组件化自定义平台”，简称“自定义平台”、“CoC平台”、“CoC”等；
 * </UL>
 * <p>
 * <b>功能说明：</b>
 * <UL>
 * <LI>调用该类的静态方法之前，必须先调用{@link #init(ServletContext)}方法对CoC平台进行初始化；
 * <LI>通过该类的静态方法可以获得Cocit平台中的其他管理接口；
 * </UL>
 * <b>名词解释：</b>
 * <UL>
 * <LI>Cocit: 组件化自定义平台（Componentization of custom IT），也称“CoC平台”；
 * <LI>Coc: 组件化自定义
 * <LI>cocsoft: 组件化自定义软件（Componentization of custom software）；
 * <LI>cocui(Cui): 组件化自定义界面（Componentization of custom UI），也称“CoC界面”;
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public abstract class Cocit {

	private static String contextPath;

	private static String contextDir;

	private static ThreadLocal<ActionContext> actionContext;

	private static BeanFactory beanFactory;

	/**
	 * 初始化CoC平台
	 * 
	 * @param context
	 */
	public static void init(ServletContext context) {
		Log.info("Cocit.init......");

		// init contextPath
		contextPath = context.getContextPath().trim();
		if (contextPath.endsWith("/")) {
			contextPath = contextPath.substring(0, contextPath.length() - 1);
		}
		if (contextPath.length() > 0 && contextPath.charAt(0) != '/') {
			contextPath = "/" + contextPath;
		}
		//
		contextDir = context.getRealPath("/").replace("\\", "/");
		if (contextDir.endsWith("/"))
			contextDir = contextDir.substring(0, contextDir.length() - 1);
		else if (contextDir.endsWith("/."))
			contextDir = contextDir.substring(0, contextDir.length() - 2);

		// init actionContext
		actionContext = new ThreadLocal<ActionContext>();

		// init beanAssist
		beanFactory = BeanFactory.make(context);

		Log.info("Cocit.init: end! {contextPath: %s, beanFactory: %s}", contextPath, beanFactory);
	}

	/**
	 * 释放CoC平台
	 * 
	 * @param context
	 */
	public static void destroy(ServletContext context) {
		beanFactory.clear();

		contextPath = null;
		beanFactory = null;
		actionContext = null;
	}

	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * 获取Servlet环境路径，如果路径长度大于1则以/开头，否则路径为空串。
	 * 
	 * @return
	 */
	public static String getContextPath() {
		return contextPath;
	}

	public static String getContextDir() {
		return contextDir;
	}

	/**
	 * 初始化HTTP环境，即初始化当前请求的运行环境。
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public static ActionContext initActionContext(HttpServletRequest req, HttpServletResponse res) {
		ActionContext ret = beanFactory.makeHttpContext(req, res);

		if (actionContext == null)
			return ret;

		actionContext.remove();
		actionContext.set(ret);

		Log.debug("Cocit.initHttpContext: ret = %s", ret);

		return ret;
	}

	/**
	 * 获取当前请求的HTTP环境。该方法只有在调用过{@link #initActionContext(HttpServletRequest, HttpServletResponse)}之后才会有返回值，否则返回null。
	 * 
	 * @return
	 */
	public static ActionContext getActionContext() {
		if (actionContext == null)
			return null;

		return actionContext.get();
	}

	/**
	 * 获取配置中的Bean对象
	 * 
	 * @param name
	 *            Bean名称
	 * @return
	 */
	public static <T> T getBean(String name) {
		return beanFactory.getBean(name);
	}

	/**
	 * 获取平台配置中的Bean对象
	 * 
	 * @param type
	 *            Bean类
	 * @return
	 */
	public static <T> T getBean(Class<T> type) {
		return beanFactory.getBean(type);
	}

	/**
	 * 根据指定的类型创建一个短信客户端接口对象，该方法被通常被{@link SoftService}调用，且每个{@link SoftService}对象只会调用该方法一次来创建短信第三方接口对象，之后将缓存在{@link SoftService}对象中。
	 * 
	 * @param type
	 * @return 返回一个新建的短信客户端接口。
	 */
	public static SmsClient makeSmsClient(String type) {
		return beanFactory.makeSmsClient(type);
	}

	/**
	 * 获取CoC组工厂。
	 * 
	 * @return 返回已被缓存的CoC组件库{@link ServiceFactory}的单例对象。
	 */
	public static ServiceFactory getServiceFactory() {
		return beanFactory.getBean(ServiceFactory.class);
	}

	/**
	 * 获取CoC UI模型工厂
	 * 
	 * @return 返回已被缓存的CoC UI模型工厂{@link WidgetModelFactory}的单例对象。
	 */
	public static WidgetModelFactory getWidgetModelFactory() {
		return beanFactory.getBean(WidgetModelFactory.class);
	}

	/**
	 * 获取CoC UIRender 工厂
	 * 
	 * @return 返回已被缓存的CoC UI Render工厂{@link WidgetRenderFactory}的单例对象。
	 */
	public static WidgetRenderFactory getWidgetRenderFactory() {
		return beanFactory.getBean(WidgetRenderFactory.class);
	}

	public static OrmFactory getOrmFactory() {
		return beanFactory.getBean(OrmFactory.class);
	}

}
