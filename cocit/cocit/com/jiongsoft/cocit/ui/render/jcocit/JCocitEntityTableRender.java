package com.jiongsoft.cocit.ui.render.jcocit;

import java.io.Writer;

import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.ui.model.widget.EntityTableUI;
import com.jiongsoft.cocit.ui.model.widget.GridWidget;
import com.jiongsoft.cocit.ui.model.widget.MenuWidget;
import com.jiongsoft.cocit.ui.model.widget.SearchBoxWidget;
import com.jiongsoft.cocit.ui.model.widget.TreeWidget;
import com.jiongsoft.cocit.ui.render.WidgetRender;

public class JCocitEntityTableRender extends WidgetRender<EntityTableUI> {

	@Override
	public void render(Writer out, EntityTableUI model) throws Throwable {
		ActionContext ctx = Cocit.getActionContext();
		int width = model.get("width", ctx.getClientUIWidth());
		int height = model.get("height", ctx.getClientUIHeight());

		int treeWidth = new Double(Math.min(240, width * 0.3)).intValue();
		int gridWidth = width - treeWidth - 30;
		int treeHeight = height - 56;
		int gridHeight = height - 51;

		String token = model.get("token", Long.toHexString(System.currentTimeMillis()));

		/*
		 * 工具栏菜单：工具栏菜单ID将被DataGrid引用
		 */
		MenuWidget menuWidget = model.getOperationMenuModel();
		if (menuWidget != null) {
			menuWidget.set("token", token);
			menuWidget.render(out);
		}

		/**
		 * 用一个Table将数据表界面分成两部分：1.左边为导航树，2.右边为DataGrid。
		 * <UL>
		 * <LI>token: 业务令牌，工具栏操作菜单通过业务令牌与该table相关联；
		 * </UL>
		 */
		print(out, "<table class=\"entityTable\" token=\"%s\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>", token);

		/*
		 * 1.左边为导航树
		 */
		TreeWidget tree = model.getNaviTreeModel();
		if (tree != null) {

			tree.set("height", "" + treeHeight);
			tree.set("width", "" + treeWidth);
			tree.set("token", token);

			print(out, "<td valign=\"top\" width=\"%s\" style=\"padding: 5px;\">", treeWidth);

			SearchBoxWidget searchModel = model.getSearchBoxModel();
			if (searchModel != null) {
				searchModel.set("width", "" + treeWidth);
				searchModel.render(out);
				print(out, "<div style=\"height: 1px;\"></div>");
			}
			tree.render(out);

			print(out, "</td>");
		} else {
			gridWidth = width - 23;
		}

		/*
		 * 2.右边为DataGrid
		 */
		GridWidget gridWidget = model.getGridModel();
		if (gridWidget != null) {
			gridWidget.set("width", "" + gridWidth);
			gridWidget.set("height", "" + gridHeight);
			gridWidget.set("token", token);

			print(out, "<td valign=\"top\" width=\"%s\" style=\"padding:5px;0 5px 5px;\">", gridWidth);

			gridWidget.render(out);

			print(out, "</td>");
		}

		//
		print(out, "</tr></table>");
	}
}
