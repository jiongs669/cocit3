package com.jiongsoft.cocit.ui;

import java.io.Writer;

/**
 * UIRender： 用于输出指定类型的{@link UIModel}到浏览器。
 * 
 * @author jiongs753
 * 
 */
public interface UIRender<T extends UIModel> {

	/**
	 * 输出窗体界面
	 * 
	 * @param out
	 * @param model
	 * @throws Throwable
	 */
	void render(Writer out, T model) throws Throwable;

}
