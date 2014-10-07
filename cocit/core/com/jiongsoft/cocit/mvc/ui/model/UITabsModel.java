package com.jiongsoft.cocit.mvc.ui.model;

import java.io.Serializable;

import com.jiongsoft.cocit.lang.Nodes;
import com.jiongsoft.cocit.mvc.ui.widget.UITabs;

public class UITabsModel extends UIWidgetModel<UITabs, Nodes> {

	public UITabsModel(UITabs model, Nodes data) {
		super(model, data);
	}

	public UITabsModel setDacorator(Serializable pageID) {
		this.dacorator = pageID;

		return this;
	}

	public UITabsModel setAjaxData(boolean ajaxData) {
		this.ajaxData = ajaxData;

		return this;
	}
}
