package com.jiongsoft.cocit.ui.render.jcocit;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.ui.model.widget.EntityModuleUI;
import com.jiongsoft.cocit.ui.model.widget.EntityTableUI;
import com.jiongsoft.cocit.ui.render.WidgetRender;
import com.jiongsoft.cocit.util.ObjectUtil;

public class JCocitEntityModuleRender extends WidgetRender<EntityModuleUI> {

	@Override
	public void render(Writer writer, EntityModuleUI model) throws Throwable {
		StringWriter out = new StringWriter();

		String contextPath = Cocit.getContextPath();

		print(out, "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		print(out, "<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		print(out, "<head>");
		print(out, "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		print(out, "<title>%s</title>", model.getName());

		// CSS
		print(out, "<link href=\"%s/jCocit/css/jCocit.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);

		/*
		 * 调试 CSS
		 */
		if (Cocit.getActionContext().isLocalHost()) {
			print(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.icon.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			print(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.searchbox.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			// print(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.panel.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			// print(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.tree.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			// print(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.window.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			// print(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.dialog.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			// print(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.datagrid.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			// print(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.combodate.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			print(out, "<link href=\"%s/jCocit-src/css/jCocit.plugin.entity.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
		}

		// JS
		print(out, "<script src=\"%s/jCocit/common/jquery.min.js\" type=\"text/javascript\"></script>", contextPath);
		print(out, "<script src=\"%s/jCocit/js/jCocit.pack.js\" type=\"text/javascript\"></script>", contextPath);

		/*
		 * 调试 JS
		 */
		if (Cocit.getActionContext().isLocalHost()) {
			print(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.combo.js\" type=\"text/javascript\"></script>", contextPath);
			print(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.combodialog.js\" type=\"text/javascript\"></script>", contextPath);
			print(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.searchbox.js\" type=\"text/javascript\"></script>", contextPath);
			// print(out, "<script src=\"%s/jCocit-src/js/jCocit.utils.js\" type=\"text/javascript\"></script>", contextPath);
			// print(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.dialog.js\" type=\"text/javascript\"></script>", contextPath);
			// print(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.tree.js\" type=\"text/javascript\"></script>", contextPath);
			// print(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.datagrid.js\" type=\"text/javascript\"></script>", contextPath);
			// print(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.pagination.js\" type=\"text/javascript\"></script>", contextPath);
			print(out, "<script src=\"%s/jCocit-src/js/jCocit.plugin.entity.js\" type=\"text/javascript\"></script>", contextPath);
		}

		// 语言设置
		print(out, "<script src=\"%s/jCocit/js/min/jCocit.nls.zh.js\" type=\"text/javascript\"></script>", contextPath);

		print(out, "</head>");
		print(out, "<body><div>");

		/**
		 * 准备参数:
		 * <UL>
		 * <LI>token: 操作令牌，通过该令牌将“NaviTree,Toolbar,DataGrid,ChildrenTabs,SearchBox”等串联在一起。
		 * <LI>width: 模块界面宽度
		 * <LI>height: 模块界面高度
		 * <LI>mainTabsHeight: 业务主表Tabs高度
		 * <LI>childrenTabsHeight: 业务子表Tabs高度
		 * </UL>
		 */
		ActionContext ctx = Cocit.getActionContext();
		String token = model.get("token", Long.toHexString(System.currentTimeMillis()));
		int width = model.get("width", ctx.getClientUIWidth());
		int height = model.get("height", ctx.getClientUIHeight());
		int mainTabsHeight = height / 2;
		int childrenTabsHeight = height - mainTabsHeight - 1;

		EntityTableUI mainEntityTable = model.getEntityTableModel();
		if (mainEntityTable == null) {
			childrenTabsHeight = height;
		}
		List<EntityTableUI> childrenEntityTables = model.getChildrenEntityTableModels();
		if (ObjectUtil.isNil(childrenEntityTables)) {
			mainTabsHeight = height;
		}

		// 计算最大高度：匹配20 Grid条记录
		mainTabsHeight = Math.min(mainTabsHeight, 655);
		childrenTabsHeight = Math.min(childrenTabsHeight, 655);

		// 模块界面 TABLE
		// print(out, "<table><tr><td>");

		/*
		 * 业务主表 Tabs
		 */
		if (mainEntityTable != null) {

			mainEntityTable.set("width", "" + width);
			mainEntityTable.set("height", "" + mainTabsHeight);

			// 业务主表 Tabs DIV
			print(out, "<div class=\"jCocit-ui jCocit-tabs\" data-options=\"\" style=\"width:%spx;height:%spx;\">"//
					, width, mainTabsHeight);

			// 业务主表 Tab DIV
			print(out, "<div title=\"%s\" class=\"jCocit-gridtab\" data-options=\"\" style=\"padding:5px;overflow:hidden;\">"//
					, model.getName());

			mainEntityTable.set("token", token);
			mainEntityTable.render(out);

			print(out, "</div>");// end: 业务主表 Tab DIV
			print(out, "</div>");// end: 业务主表 Tabs DIV

		}

		/**
		 * 业务子表 Tabs
		 */
		if (!ObjectUtil.isNil(childrenEntityTables)) {
			// 业务子表 TR
			// print(out, "</td></tr><tr><td>");

			// 业务主表与业务子表之间的间隙
			print(out, "<div style=\"height: 5px;\"></div>");

			// 业务子表 Tabs DIV
			print(out, "<div id=\"childrentabs_%s\" class=\"jCocit-ui jCocit-tabs\" data-options=\"onSelect: jCocit.entity.doTabsSelect\" style=\"width:%spx; height:%spx; \">"//
					, token, width, childrenTabsHeight);

			// fkfield: 表示业务子表将通过哪个外键字段关联到业务主表？
			String fkfield;
			for (EntityTableUI childEntityTable : childrenEntityTables) {
				fkfield = childEntityTable.get("fkfield", "");
				print(out, "<div title=\"%s\" class=\"jCocit-gridtab\" data-options=\"token:'%s', fkfield: '%s', url: '%s?_uiHeight=%s&_uiWidth=%s',closable: false, cache: true\" style=\"padding:5px\"></div>"//
						, childEntityTable.getName(), token, fkfield, childEntityTable.getLoadUrl(), childrenTabsHeight, width);
			}

			print(out, "</div>");// end: 业务子表 Tabs DIV

			// 子模块工具菜单——位于主模块TABS右边
			// print(out, "<div id=\"tabtools_%s\">", model.getId());
			// for (EntityTableWidgetModel child : children) {
			// print(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-button\" data-options=\"plain: false");
			// // print(out, ", iconCls:'icon-add'");
			// print(out, "\">%s</a>", child.getName());
			// }
			// print(out, "</div>");

		}

		// print(out, "</td></tr></table>");//end: 模块界面 TABLE

		print(out, "</div></body></html>");

		writer.write(out.toString());
	}
}
