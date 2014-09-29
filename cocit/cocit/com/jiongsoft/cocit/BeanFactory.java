package com.jiongsoft.cocit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import com.jiongsoft.cocit.entity.PermissionEntity;
import com.jiongsoft.cocit.entity.WebCatalogEntity;
import com.jiongsoft.cocit.entity.WebContentEntity;
import com.jiongsoft.cocit.sms.SmsClient;
import com.jiongsoft.cocit.util.ClassUtil;
import com.jiongsoft.cocit.util.Log;

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

	private String smsClient_zucp;

	private String smsClient_zr;

	private String smsClient_emay;

	private Class<? extends PermissionEntity> permissionEntityType;

	private Class<? extends WebCatalogEntity> webCatalogEntityType;

	private Class<? extends WebContentEntity> webContentEntityType;

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

		try {
			if ("zucp".equals(type)) {
				Log.info("BeanFactory.makeSmsClient ... {type:%s, smsClient_zucp:%s}", type, smsClient_zucp);

				return (SmsClient) ClassUtil.newInstance(smsClient_zucp);

			} else if ("zr".equals(type)) {
				Log.info("BeanFactory.makeSmsClient ... {type:%s, smsClient_zr:%s}", type, smsClient_zr);

				return (SmsClient) ClassUtil.newInstance(smsClient_zr);

			} else if ("emay".equals(type)) {
				Log.info("BeanFactory.makeSmsClient ... {type:%s, smsClient_emay:%s}", type, smsClient_emay);

				return (SmsClient) ClassUtil.newInstance(smsClient_emay);

			} else {
				Log.warn("BeanFactory.makeSmsClient: 指定的短信接口类型不存在！{type:%s}", type);
			}
		} catch (Throwable e) {
			Log.error("BeanFactory.makeSmsClient: 失败! {type:%s}", type, e);
		}

		return null;
	}

	/**
	 * 获取授权实体类型
	 * 
	 * @return
	 */
	public Class getPermissionEntityType() {
		return permissionEntityType;
	}

	/**
	 * 设置权限实体类型：该方法共IOC配置文件注入调用。
	 * 
	 * @param entityType
	 */
	public void setPermissionEntityType(String entityType) {
		permissionEntityType = ClassUtil.forName(entityType);
	}

	public Class getWebCatalogEntityType() {
		return webCatalogEntityType;
	}

	public void setWebCatalogEntityType(String entityType) {
		this.webCatalogEntityType = ClassUtil.forName(entityType);
	}

	public Class getWebContentEntityType() {
		return webContentEntityType;
	}

	public void setWebContentEntityType(String entityType) {
		this.webContentEntityType = ClassUtil.forName(entityType);
	}
}
