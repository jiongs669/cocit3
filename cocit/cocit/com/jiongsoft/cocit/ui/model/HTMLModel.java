package com.jiongsoft.cocit.ui.model;

import java.io.Writer;

import com.jiongsoft.cocit.ui.UIModel;

/**
 * 输出HTML/TEXT文本。
 * 
 * @author jiongsoft
 * 
 */
public class HTMLModel implements UIModel {
	/**
	 * 提示信息
	 */
	String content;

	public static HTMLModel make(String content) {
		HTMLModel ret = new HTMLModel();

		ret.content = content;

		return ret;
	}

	protected HTMLModel() {
	}

	@Override
	public void render(Writer out) throws Throwable {
		out.write(content);
	}

	@Override
	public String getContentType() {
		return CONTENT_TYPE_HTML;
	}

	@Override
	public boolean isCachable() {
		return false;
	}
}
