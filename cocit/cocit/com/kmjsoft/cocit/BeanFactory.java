package com.kmjsoft.cocit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import com.kmjsoft.cocit.sms.SmsClient;
import com.kmjsoft.cocit.util.ClassUtil;
import com.kmjsoft.cocit.util.Log;

/**
 * Cocit Bean工厂助理：辅助Cocit平台完成Bean的“创建、缓存”等管理工作。
 * <UL>
 * <LI>配置文件：类路径下的com/jiongssoft/cocit/cocit.json；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public class BeanFactory {

	private static NutIoc beans;

	/*
	 * 接口实现类全名
	 */
	private String actionContext;

	static BeanFactory make(ServletContext context) {
		String config = Cocit.class.getName().replace(".", "/").toLowerCase() + ".json";

		Log.info("BeanFactory.init: 加载Cocit平台配置文件...{config:%s}", config);

		beans = new NutIoc(new JsonLoader(config));

		return beans.get(BeanFactory.class);
	}

	void clear() {
	}

	<T> T getBean(String name) {
		try {
			return beans.get(null, name);
		} catch (Exception e) {
			Log.warn("BeanFactory.get: 失败! {name:%s}", name, e);
		}

		return null;
	}

	<T> T getBean(Class<T> type) {
		try {
			String name = type.getSimpleName();
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			return beans.get(null, name);
		} catch (Exception e) {
			Log.warn("BeanFactory.get: 失败! {type:%s}", type, e);
		}

		return null;
	}

	ActionContext makeHttpContext(HttpServletRequest req, HttpServletResponse res) {
		try {
			return ClassUtil.newInstance(actionContext, req, res);
		} catch (Throwable e) {
			Log.error("BeanFactory.makeHttpContext: 失败! {req:%s, res:%s, httpContext:%s}", req, res, actionContext, e);
		}

		return null;
	}

	SmsClient makeSmsClient(String type) {

		return null;
	}

}
