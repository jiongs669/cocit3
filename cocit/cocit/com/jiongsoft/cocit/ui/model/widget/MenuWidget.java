package com.jiongsoft.cocit.ui.model.widget;

import com.jiongsoft.cocit.ui.model.WidgetModel;
import com.jiongsoft.cocit.util.Tree;

/**
 * 菜单窗体界面模型：包括菜单属性和菜单所需要的数据。
 * 
 * @author yongshan.ji
 * 
 */
public class MenuWidget extends WidgetModel {

	private SearchBoxWidget searchBoxModel;
	
	private Tree data;

	public Tree getData() {
		return data;
	}

	public void setData(Tree menu) {
		this.data = menu;
	}

	public SearchBoxWidget getSearchBoxModel() {
		return searchBoxModel;
	}

	public void setSearchBoxModel(SearchBoxWidget searchBoxModel) {
		this.searchBoxModel = searchBoxModel;
	}

}
