package com.jiongsoft.cocit.ui.render.jcocit;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.jiongsoft.cocit.ui.model.widget.TreeWidgetData;
import com.jiongsoft.cocit.ui.model.widget.TreeWidget;
import com.jiongsoft.cocit.ui.render.WidgetRender;
import com.jiongsoft.cocit.util.Json;
import com.jiongsoft.cocit.util.ObjectUtil;
import com.jiongsoft.cocit.util.StringUtil;
import com.jiongsoft.cocit.util.Tree;
import com.jiongsoft.cocit.util.Tree.Node;

public abstract class JCocitTreeRenders {

	public static class ModelRender extends WidgetRender<TreeWidget> {

		@Override
		public void render(Writer out, TreeWidget model) throws Throwable {
			if (model.getData() != null) {
				print(out, "<script type=\"text/javascript\">var treedata_%s=", model.get("token", ""));
				new DataRender().outNodes(out, model.getData().getChildren());
				print(out, "</script>");
			}

			// Tree容器：DIV
			print(out, "<div style=\"height:%spx;width:%spx;overflow: auto;\" class=\"tree_container\">", model.get("height", 300), model.get("width", 200));

			// Tree：id = "tree_" + token
			String token = model.get("token", "");
			print(out, "<ul id=\"tree_%s\" class=\"jCocit-ui jCocit-tree\" data-options=\"", token);

			print(out, "token: '%s'", token);// 导航树Tree通过该令牌查找DataGrid对象
			print(out, ",checkbox: %s", (boolean) model.get("checkbox", true));
			print(out, ",onlyLeafCheck: %s", (boolean) model.get("onlyLeafCheck", false));
			print(out, ",onlyLeafValue: %s", (boolean) model.get("onlyLeafValue", false));
			print(out, ",cascadeCheck: %s", (boolean) model.get("cascadeCheck", true));

			if (model.getData() == null) {
				print(out, ",url: '%s'", model.getDataLoadUrl());
			} else {
				print(out, ",data: treedata_%s", model.get("token", ""));
			}
			print(out, ",onSelect: jCocit.entity.doTreeSelect");
			print(out, ",onCheck: jCocit.entity.doTreeSelect");

			// print(out, ",lines: %s",model.is("lines"));
			// print(out, ",styleName: 'tree-lines'");

			print(out, "\">");

			print(out, "</ul>");
			print(out, "</div>");
		}
	}

	public static class DataRender extends WidgetRender<TreeWidgetData> {

		@Override
		public void render(Writer out, TreeWidgetData model) throws Throwable {

			Tree tree = model.getData();
			List<Node> nodes = tree.getChildren();
			if (!ObjectUtil.isNil(nodes))
				outNodes(out, nodes);
			else {
				print(out, "[]");
			}

		}

		private void outNodes(Writer out, List<Node> nodes) throws IOException {

			print(out, "[");

			boolean noFirst = false;
			for (Node node : nodes) {
				if (noFirst) {
					print(out, ",");
				} else {
					noFirst = true;
				}

				print(out, "{\"id\" : %s", Json.toJson(node.getId()));
				print(out, ",\"text\" : %s", Json.toJson(node.getName()));
				print(out, ",\"checked\" : %s", node.get("checked", false) ? 1 : 0);
				print(out, ",\"open\" : %s", node.get("open", false) ? 1 : 0);
				List<Node> children = node.getChildren();
				if (!ObjectUtil.isNil(children)) {
					print(out, ",\"children\" :");

					outNodes(out, children);
				} else if (!StringUtil.isNil(node.getChildrenURL())) {
					print(out, ",\"children\" : %s", Json.toJson(node.getChildrenURL()));

				}

				print(out, "}");
			}

			print(out, "]");
		}
	}
}
