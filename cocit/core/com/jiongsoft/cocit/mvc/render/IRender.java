package com.jiongsoft.cocit.mvc.render;

import java.io.Writer;
import java.util.Map;

import com.jiongsoft.cocit.mvc.ui.IUIView;

public interface IRender {

	public void render(Writer out, Throwable ex, Map context) throws Exception;

	public void render(Writer out, IUIView ui, Map context) throws Exception;

	public String getContentType(IUIView ui);

}
