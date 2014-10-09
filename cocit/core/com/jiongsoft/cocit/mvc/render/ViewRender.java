package com.jiongsoft.cocit.mvc.render;

import com.jiongsoft.cocit.lang.Str;
import com.jiongsoft.cocit.mvc.template.ITemplateEngine;
import com.jiongsoft.cocit.mvc.ui.IUIView;
import com.jiongsoft.cocit.mvc.ui.model.UIWidgetModel;
import com.kmjsoft.cocit.Demsy;

public class ViewRender extends TemplateRender {

	public ViewRender(ITemplateEngine tpl) {
		super(tpl);
	}

	public String getContentType(IUIView ui) {
		return "text/html; charset=UTF-8";

	}

	protected String getTemplate(IUIView ui) {
		String tpl = ui.getTemplate();

		if (Str.isEmpty(tpl)) {
			if (ui instanceof UIWidgetModel) {
				ui = ((UIWidgetModel) ui).getModel();
				tpl = ui.getTemplate();
			}
		}
		if (Str.isEmpty(tpl)) {
			tpl = ui.getClass().getName().replace("com.kmetop.demsy.mvc.", "");
		}

		if (tpl.startsWith("/")) {
			if (tpl.endsWith(".st")) {
				return tpl;
			} else {
				return tpl + ".st";
			}
		}

		return (Demsy.appconfig.getTplPackage() + "." + tpl).replace(".", "/") + (Str.isEmpty(ui.getTemplateType()) ? ".st" : ui.getTemplateType());
	}

}
