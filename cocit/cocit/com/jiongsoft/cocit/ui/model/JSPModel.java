package com.jiongsoft.cocit.ui.model;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.view.JspView;

import com.jiongsoft.cocit.action.ActionHelper;
import com.jiongsoft.cocit.ui.UIModel;

/**
 * JSP模型： 环境context中可以包含
 * <UL>
 * <LI>actionService: {@link ActionHelper}对象
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class JSPModel implements UIModel {

	private String contextPath;

	private String jsp;

	private Map<String, Object> context;

	private HttpServletRequest req;

	private HttpServletResponse resp;

	/**
	 * 
	 * 软件环境路径 = WEB环境路径 + 软件JSP根路径。
	 * <p>
	 * 软件JSP根路径是软件编号替换“.”为“_”后的路径。如软件编号 www.yunnanbaiyao.com.cn 替换后变成 www_yunnanbaiyao_com_cn
	 * <p>
	 * JSP相对路径是相对于软件JSP根路径而言。
	 * 
	 * @param contextPath
	 *            软件环境路径
	 * @param jspPath
	 *            JSP相对路径
	 * @return
	 */
	public static JSPModel make(HttpServletRequest req, HttpServletResponse resp, String contextPath, String jspPath) {
		JSPModel ret = new JSPModel(req, resp, contextPath, jspPath);

		return ret;
	}

	protected JSPModel(HttpServletRequest req, HttpServletResponse resp, String contextPath, String jspPath) {
		context = new HashMap();
		this.req = req;
		this.resp = resp;
		this.contextPath = contextPath;
		this.jsp = contextPath + jspPath;
	}

	@Override
	public void render(Writer out) throws Throwable {
		new JspView(jsp).render(req, resp, this);
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public boolean isCachable() {
		return false;
	}

	public String getJsp() {
		return jsp;
	}

	public <T> T get(String key) {
		return (T) context.get(key);
	}

	public void set(String key, Object value) {
		context.put(key, value);
	}

	/**
	 * WEB环境路径 + 软件环境路径
	 * 
	 * @param contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

}
