package com.jiongsoft.cocit.mvc.controller;

import com.jiongsoft.cocit.mvc.ui.IUIViewController;
import com.jiongsoft.cocit.mvc.ui.UIBlockContext;
import com.kmjsoft.cocit.orm.expr.CndExpr;

/**
 * 视图控制器
 * 
 * @author yongshan.ji
 * 
 */
public class UIViewController implements IUIViewController {

	protected CndExpr getExpr(UIBlockContext blockContext) {
		return null;
	}

	@Override
	public Object process(UIBlockContext blockContext) {
		blockContext.query(getExpr(blockContext));

		return null;
	}

	@Override
	public String getViewTemplate(UIBlockContext context, String defaultTemplate) {
		return defaultTemplate;
	}

	@Override
	public String getViewExpression(UIBlockContext context, String defaultExpression) {
		return defaultExpression;
	}

}
