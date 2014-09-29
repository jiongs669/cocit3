package com.jiongsoft.cocit.ui.model.widget;

import com.jiongsoft.cocit.ui.model.WidgetModel;

/**
 * “业务表”窗体界面模型：由“左边导航树、右边顶部操作菜单、右边顶部查询栏、右边下部Grid”组成。
 * 
 * @author yongshan.ji
 * 
 */
public class EntityTableUI extends WidgetModel {

	// 实体表名称：可用作 TAB 名称
	private String name;

	// URL: TAB 异步加载
	private String loadUrl;

	// 导航树
	private TreeWidget naviTreeModel;

	// 操作菜单
	private MenuWidget operationMenuModel;

	// 检索框
	private SearchBoxWidget searchBoxModel;

	// Grid
	private GridWidget gridModel;

	public void setGrid(GridWidget gridWidget) {
		this.gridModel = gridWidget;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TreeWidget getNaviTreeModel() {
		return naviTreeModel;
	}

	public void setNaviTreeModel(TreeWidget naviTreeModel) {
		this.naviTreeModel = naviTreeModel;
	}

	public MenuWidget getOperationMenuModel() {
		return operationMenuModel;
	}

	public void setOperationMenuModel(MenuWidget operationMenuModel) {
		this.operationMenuModel = operationMenuModel;
	}

	public SearchBoxWidget getSearchBoxModel() {
		return searchBoxModel;
	}

	public void setSearchBoxModel(SearchBoxWidget searchBoxModel) {
		this.searchBoxModel = searchBoxModel;
	}

	public GridWidget getGridModel() {
		return gridModel;
	}

	public void setGridModel(GridWidget gridModel) {
		this.gridModel = gridModel;
	}

	public String getLoadUrl() {
		return loadUrl;
	}

	public void setLoadUrl(String dataLoadUrl) {
		this.loadUrl = dataLoadUrl;
	}

}
