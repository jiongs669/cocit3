package com.jiongsoft.cocit.ui.model;

import java.io.Writer;

import com.jiongsoft.cocit.ui.UIModel;

public class JSONModel implements UIModel {

	String content;

	/**
	 * 创建JSON模型，输出JSON文本。
	 * 
	 * @param content
	 *            JSON文本
	 */
	public static JSONModel make(String content) {
		JSONModel model = new JSONModel();

		model.content = content;

		return model;
	}

	protected JSONModel() {
	}

	@Override
	public void render(Writer out) throws Throwable {
		out.write(content);
	}

	@Override
	public String getContentType() {
		return CONTENT_TYPE_JSON;
	}

	@Override
	public boolean isCachable() {
		return false;
	}
}
