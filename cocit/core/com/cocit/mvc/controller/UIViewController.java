package com.cocit.mvc.controller;

import com.cocit.mvc.ui.IUIViewController;
import com.cocit.mvc.ui.UIBlockContext;
import com.jiongsoft.cocit.orm.expr.CndExpr;

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
