package com.jiongsoft.cocit.ui.model;

import java.io.Writer;

import com.jiongsoft.cocit.ui.UIModel;

/**
 * 输出HTML/TEXT文本。
 * 
 * @author jiongsoft
 * 
 */
public class XMLModel implements UIModel {
	/**
	 * 提示信息
	 */
	String content;

	public static XMLModel make(String content) {
		XMLModel ret = new XMLModel();

		ret.content = content;

		return ret;
	}

	protected XMLModel() {
	}

	@Override
	public void render(Writer out) throws Throwable {
		out.write(content);
	}

	@Override
	public String getContentType() {
		return CONTENT_TYPE_XML;
	}

	@Override
	public boolean isCachable() {
		return false;
	}

}
