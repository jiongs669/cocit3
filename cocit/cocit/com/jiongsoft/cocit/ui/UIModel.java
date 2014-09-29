package com.jiongsoft.cocit.ui;

import java.io.Writer;

/**
 * CoC UI模型：表示界面窗体的逻辑模型。
 * <UL>
 * <LI>可以是不含数据的窗体模型；
 * <LI>可以是包含数据的窗体模型；
 * <LI>可以是没有窗体的数据模型；
 * <LI>UI模型由{@link CuiModuleMnR}创建；
 * <LI>UI模型通过Action方法返回；
 * <LI>UI模型被{@link UIRender}输出到浏览器；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public interface UIModel {

	static final String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";

	static final String CONTENT_TYPE_JSON = "text/json; charset=UTF-8";

	static final String CONTENT_TYPE_XML = "text/xml; charset=UTF-8";

	/**
	 * Render 当前 UIModel 到客户端
	 * 
	 * @param out
	 * @throws Throwable
	 */
	public void render(Writer out) throws Throwable;

	/**
	 * Get HttpServletResponse Content Type
	 * 
	 * <B>可选值</B>
	 * <UL>
	 * <LI>HTML: {@link #CONTENT_TYPE_HTML}
	 * <LI>Json: {@link #CONTENT_TYPE_JSON};
	 * <LI>XML: {@link #CONTENT_TYPE_XML};
	 * </UL>
	 * 
	 * @return
	 */
	public String getContentType();

	/**
	 * 判断是否支持浏览器端cache?
	 * 
	 * @return
	 */
	public boolean isCachable();
}
