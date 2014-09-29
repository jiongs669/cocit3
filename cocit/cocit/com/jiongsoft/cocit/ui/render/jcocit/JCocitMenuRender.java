package com.jiongsoft.cocit.ui.render.jcocit;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.jiongsoft.cocit.ui.model.widget.MenuWidget;
import com.jiongsoft.cocit.ui.model.widget.SearchBoxWidget;
import com.jiongsoft.cocit.ui.render.WidgetRender;
import com.jiongsoft.cocit.util.ObjectUtil;
import com.jiongsoft.cocit.util.StringUtil;
import com.jiongsoft.cocit.util.Tree;
import com.jiongsoft.cocit.util.Tree.Node;

@SuppressWarnings("unused")
public class JCocitMenuRender extends WidgetRender<MenuWidget> {

	@Override
	public void render(Writer out, MenuWidget model) throws Throwable {
		// this.printJSMenu(out, model);

		printHtmlMenu(out, model);
	}

	private void printHtmlMenu(Writer out, MenuWidget model) throws Throwable {
		Tree tree = model.getData();

		// 工具栏容器：DIV
		print(out, "<div id=\"toolbar_%s\" style=\"padding:2px; height: auto;\">", model.get("token", ""));

		/*
		 * 用一个Table将工具栏容器分成两部分：1.左边为工具栏菜单，2.右边为搜索框。
		 */
		print(out, "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"5\" nowrap>");

		/*
		 * 1.左边为工具栏菜单
		 */
		if (tree != null) {
			List<Node> nodes = tree.getChildren();
			if (!ObjectUtil.isNil(nodes)) {

				// toolbar
				print(out, "<div style=\"margin: 1px 0 1px 0; white-space: nowrap;\">");

				for (Node node : nodes) {
					print(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-toolbar\" data-options=\"");
					print(out, "name:'%s'", node.getName());

					// token: 用来关联到导航树（tree_????）和DataGrid（datagrid_???）。???表示token。
					String str = model.get("token", "");
					if (!StringUtil.isNil(str))
						print(out, ", token: '%s'", str);// 菜单通过该令牌获取DataGrid对象

					// funcExpr = moduleID:tableID:operationID
					str = node.get("funcExpr", "");
					if (!StringUtil.isNil(str))
						print(out, ", funcExpr: '%s'", str);

					String opMode = node.get("opMode", "");
					if (!StringUtil.isNil(opMode))
						print(out, ", opMode: '%s'", opMode);

					String actionPath = node.get("actionPath", "");
					if (!StringUtil.isNil(actionPath))
						print(out, ", actionPath: '%s'", actionPath);
					
					String actionWindow = node.get("actionWindow", "");
					if (!StringUtil.isNil(actionWindow))
						print(out, ", actionWindow: '%s'", actionWindow);

					str = node.get("opCode", "");
					if (!StringUtil.isNil(str)) {
						print(out, ", opCode: %s", str);
						print(out, ", iconCls: 'icon-%s icon-%s-%s'", str, str, opMode);// iconCls 由菜单操作码决定
					}

					// 子菜单
					if (node.size() > 0) {
						print(out, ", menu: '#submenu_%s_%s'", model.get("token", ""), node.getId());
					} else {
						print(out, ", onClick: jCocit.entity.doAction");
					}

					print(out, "\">%s</a>", node.getName());
				}
				print(out, "</div>");

				// sub menu
				for (Node node : nodes) {
					if (node.size() > 0)
						printHtmlSubMenu(out, model, node);
				}
			}
		}

		/*
		 * 2.右边为搜索框
		 */
		SearchBoxWidget searchMode = model.getSearchBoxModel();
		if (searchMode != null) {
			print(out, "</td><td align=\"right\" style=\"padding-left: 5px;\">");

			searchMode.set("token", model.get("token", ""));
			searchMode.render(out);

		}

		print(out, "</td></tr></table>");

		print(out, "</div>");
	}

	private void printHtmlSubMenu(Writer out, MenuWidget model, Node node) throws IOException {
		print(out, "<div id=\"submenu_%s_%s\" data-options=\"onClick: jCocit.entity.doAction\" style=\"width:120px;\">", //
				model.get("token", ""), node.getId());

		for (Node child : node.getChildren()) {
			print(out, "<div data-options=\"", child.getId());
			print(out, "name:'%s'", node.getName());

			// funcExpr = moduleID:tableID:operationID
			String str = node.get("funcExpr", "");
			if (!StringUtil.isNil(str))
				print(out, ", funcExpr: '%s'", str);

			str = model.get("token", "");
			if (!StringUtil.isNil(str))
				print(out, ", token: '%s'", str);

			str = child.get("operationCode", "");
			if (!StringUtil.isNil(str)) {
				print(out, ", iconCls: 'icon-%s'", str);
				print(out, ", opCode: %s", str);
			}

			print(out, "\"><span>%s</span>", child.getName());

			// 输出子菜单
			if (child.size() > 0) {
				printHtmlSubMenu(out, model, child);
			}

			print(out, "</div>");
		}

		print(out, "</div>");
	}

	private void printJSMenu(Writer out, MenuWidget model) throws IOException {
		print(out, "<script type=\"text/javascript\">var toolbar_%s = [", model.get("token", ""));

		Tree tree = model.getData();
		List<Node> nodes = tree.getAll();

		if (!ObjectUtil.isNil(nodes)) {
			boolean notFirst = false;
			for (Node node : nodes) {

				if (notFirst) {
					print(out, ",");
				} else {
					notFirst = true;
				}

				if (node.size() > 0) {

					print(out, "'-'");

					continue;
				}

				print(out, "{text: '%s', opID: '%s'", node.getName(), node.getId());
				String value = node.get("moduleID", "");
				if (!StringUtil.isNil(value))
					print(out, ", moduleID: %s", value);

				value = node.get("tableID", "");
				if (!StringUtil.isNil(value))
					print(out, ", tableID: %s", value);

				value = node.get("opMode", "");
				if (!StringUtil.isNil(value))
					print(out, ", opMode: '%s'", value);

				value = node.get("operationCode", "");
				if (!StringUtil.isNil(value)) {
					print(out, ", opCode: %s", value);
					print(out, ", iconCls: 'icon-%s'", value);// iconCls 由菜单操作码决定
				}

				print(out, ", onClick:jCocit.entity.doAction");

				print(out, "}");

			}
		}

		print(out, "]</script>");
	}
}
