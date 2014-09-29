package com.kmetop.demsy.mvc.controller;

import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.kmetop.demsy.mvc.ui.IUIViewController;
import com.kmetop.demsy.mvc.ui.UIBlockContext;

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
