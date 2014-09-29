package com.jiongsoft.cocit.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.ui.UIModelView;
import com.jiongsoft.cocit.ui.model.AlertsModel;
import com.jiongsoft.cocit.ui.model.widget.Column;
import com.jiongsoft.cocit.ui.model.widget.EntityModuleUI;
import com.jiongsoft.cocit.ui.model.widget.EntityTableUI;
import com.jiongsoft.cocit.ui.model.widget.GridWidget;
import com.jiongsoft.cocit.ui.model.widget.GridWidgetData;
import com.jiongsoft.cocit.ui.model.widget.MenuWidget;
import com.jiongsoft.cocit.ui.model.widget.TreeWidget;
import com.jiongsoft.cocit.ui.model.widget.TreeWidgetData;
import com.jiongsoft.cocit.util.FileUtil;
import com.jiongsoft.cocit.util.Json;
import com.jiongsoft.cocit.util.SortUtil;
import com.jiongsoft.cocit.util.StringUtil;
import com.jiongsoft.cocit.util.Tree;
import com.jiongsoft.cocit.util.Tree.Node;
import com.jiongsoft.cocit.util.UrlAPI;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class FileManagerAction {

	@At(UrlAPI.GET_FILE_MANAGER)
	@Fail("redirect:/login/form")
	public EntityModuleUI getFileManager(String dir) {
		Cocit.getServiceFactory().getSoftService(null).getSecurityManager().checkLoginRole(com.jiongsoft.cocit.service.SecurityManager.ROLE_DP_SUPPORT);

		if (StringUtil.isNil(dir)) {
			dir = new File(Cocit.getContextDir()).getParentFile().getAbsolutePath();
		} else {
			try {
				String d = StringUtil.decodeHex(dir);
				if (d != null)
					dir = d;
			} catch (Throwable e) {
			}
		}

		EntityTableUI tableUI = new EntityTableUI();
		GridWidget grid = this.getGridWidget(dir);
		grid.setDataLoadUrl(UrlAPI.GET_FILE_GRID_DATA.replace("*", StringUtil.encodeHex(dir)));
		grid.set("pagination", "true");
		tableUI.setGrid(grid);

		TreeWidget tree = new TreeWidget();
		tree.setDataLoadUrl(UrlAPI.GET_FILE_TREE_DATA.replace("*", StringUtil.encodeHex(dir)));
		tree.set("checkbox", "true");
		tree.set("cascadeCheck", "false");
		tableUI.setNaviTreeModel(tree);

		MenuWidget menu = new MenuWidget();
		Tree opMenu = Tree.make();
		Node opDelete = opMenu.addNode(null, "delete");
		opDelete.setName("删除");
		opDelete.set("opMode", "r");
		opDelete.set("opCode", "299");
		opDelete.set("actionPath", UrlAPI.DEL_DISK_FILES.replace("*", ""));
		Node opRename = opMenu.addNode(null, "rename");
		opRename.setName("重命名");
		opRename.set("opMode", "e");
		opRename.set("opCode", "102");
		opRename.set("actionPath", "/coc/renameDiskFile/");

		menu.setData(opMenu);
		tableUI.setOperationMenuModel(menu);

		EntityModuleUI ret = new EntityModuleUI(tableUI);
		ret.setName("文件管理器");

		return ret;
	}

	@At(UrlAPI.GET_FILE_GRID_DATA)
	public GridWidgetData getFileGridData(String dir, String fileType) {
		Cocit.getServiceFactory().getSoftService(null).getSecurityManager().checkLoginRole(com.jiongsoft.cocit.service.SecurityManager.ROLE_DP_SUPPORT);

		if (StringUtil.isNil(dir)) {
			dir = Cocit.getContextDir();
		} else {
			dir = StringUtil.decodeHex(dir);
		}

		ActionContext actionContext = Cocit.getActionContext();
		String pathExpr = actionContext.getParameterValue("query.filterExpr", "");
		Map map = Json.fromJson(Map.class, pathExpr);
		List<String> folders = (List) map.get("folder");
		if (folders == null || folders.size() == 0) {
			folders = new ArrayList();
			folders.add(dir);
		} else {
			List<String> folders1 = folders;
			folders = new ArrayList();
			for (String folder : folders1) {
				folders.add(StringUtil.decodeHex(folder));
			}
		}
		List<String> fileTypes = (List) map.get("fileType");

		GridWidgetData ret = new GridWidgetData();
		ret.setModel(this.getGridWidget(dir));

		int pageIndex = actionContext.getParameterValue("pageIndex", 1);
		int pageSize = actionContext.getParameterValue("pageSize", 20);
		String sortOrder = actionContext.getParameterValue("sortOrder", "");
		String sortField = actionContext.getParameterValue("sortField", "");

		makeFileGridData(ret, folders, pageSize, pageIndex, sortOrder, sortField, fileTypes);

		// 返回
		return ret;
	}

	@At(UrlAPI.GET_FILE_TREE_DATA)
	public TreeWidgetData getFileTreeData(String dir) {
		Cocit.getServiceFactory().getSoftService(null).getSecurityManager().checkLoginRole(com.jiongsoft.cocit.service.SecurityManager.ROLE_DP_SUPPORT);

		if (StringUtil.isNil(dir)) {
			dir = Cocit.getContextDir();
		} else {
			dir = StringUtil.decodeHex(dir);
		}

		TreeWidgetData ret = new TreeWidgetData();

		TreeWidget tree = new TreeWidget();
		ret.setModel(tree);

		Tree data = getFileTree(dir);
		ret.setData(data);

		// 返回
		return ret;
	}

	@At(UrlAPI.DEL_DISK_FILES)
	public AlertsModel deleteDiskFiles(String dirs) {
		Cocit.getServiceFactory().getSoftService(null).getSecurityManager().checkLoginRole(com.jiongsoft.cocit.service.SecurityManager.ROLE_DP_SUPPORT);

		try {
			String[] dirArray = StringUtil.toArray(dirs);
			for (String dir : dirArray) {
				dir = StringUtil.decodeHex(dir);
				FileUtil.deleteAll(new File(dir));
			}
			return AlertsModel.makeSuccess("删除磁盘文件成功！");
		} catch (Throwable e) {
			return AlertsModel.makeError("删除磁盘文件失败！" + e.getMessage());
		}
	}

	private GridWidget getGridWidget(String dir) {

		GridWidget grid = new GridWidget();

		new File("").length();

		grid.addColumn(new Column("name", "文件名").setWidth(200).setAlign("left"));
		grid.addColumn(new Column("type", "类型").setWidth(60).setAlign("left"));
		grid.addColumn(new Column("lastModified", "修改时间").setAlign("right").setWidth(120).setPattern("yyyy-MM-dd HH:mm:ss"));
		grid.addColumn(new Column("length", "文件大小").setAlign("right").setWidth(200).setPattern("#,### K"));
		grid.addColumn(new Column("path", "文件路径").setWidth(400).setAlign("left"));

		return grid;
	}

	private void makeFileList(List<FileEntity> list, File dir, List<String> fileTypes) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					FileEntity model = new FileEntity();
					model.setId(StringUtil.encodeHex(file.getAbsolutePath()));
					if (file.isDirectory()) {
						model.setType("");
					} else {
						int dot = file.getName().lastIndexOf(".");
						if (dot > -1) {
							model.setType(file.getName().substring(dot + 1).toUpperCase());
						}
					}
					model.setPath(file.getAbsolutePath());
					model.setName(file.getName());
					model.setLastModified(new Date(file.lastModified()));
					model.setLength(file.length());

					if (fileTypes != null && fileTypes.size() > 0) {
						if (file.isDirectory()) {
							makeFileList(list, file, fileTypes);
						} else {
							for (String fileType : fileTypes) {
								if (file.getName().toLowerCase().endsWith(fileType.toLowerCase().trim())) {
									list.add(model);
								}
							}
						}
					} else {
						list.add(model);
					}
				}
			}
		}
	}

	private void makeFileGridData(GridWidgetData grid, List<String> dirPaths, int pageSize, int pageIndex, String sortOrder, String sortField, List<String> fileTypes) {
		List<FileEntity> list = new ArrayList();

		for (String dirPath : dirPaths) {
			File dir = new File(dirPath);
			makeFileList(list, dir, fileTypes);
		}

		int size = list.size();

		if (!StringUtil.isNil(sortField) && !sortField.equals("id"))
			SortUtil.sort(list, sortField, true);
		if (sortOrder.equals("desc")) {
			List list1 = new ArrayList();
			for (int i = list.size() - 1; i >= 0; i--) {
				list1.add(list.get(i));
			}
			list = list1;
		}

		List ret = new ArrayList();
		for (int i = 0; i < pageSize; i++) {
			int idx = (pageIndex - 1) * pageSize + i;
			if (idx >= list.size()) {
				break;
			}
			ret.add(list.get(idx));
		}

		grid.setData(ret);

		grid.setTotal(size);
	}

	private Tree getFileTree(String dirPath) {
		Tree tree = Tree.make();

		String folderID = null;
		String rootDir = new File(Cocit.getContextDir()).getParentFile().getAbsolutePath();
		if (rootDir.equals(dirPath)) {
			folderID = "folder:" + StringUtil.encodeHex(dirPath);
			Node folderNode = tree.addNode(null, folderID);
			folderNode.set("open", "true");
			folderNode.setName("文件夹(" + new File(dirPath).getName() + ")");
		}

		File dir = new File(dirPath);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null) {

				Node node;
				// node = tree.addNode(null, "folder:" + StringUtil.encodeHex(dir.getAbsolutePath()));
				// node.setName("..");
				for (File file : files) {
					if (!file.isDirectory()) {
						continue;
					}
					node = tree.addNode(folderID, "folder:" + StringUtil.encodeHex(file.getAbsolutePath()));
					node.setChildrenURL(UrlAPI.GET_FILE_TREE_DATA.replace("*", StringUtil.encodeHex(file.getAbsolutePath())));
					node.setName(file.getName());
				}
			}
		}

		if (rootDir.equals(dirPath)) {
			String fileTypeID = "fileType: all";
			Node fileTypeNode = tree.addNode(null, fileTypeID);
			fileTypeNode.set("open", "true");
			fileTypeNode.setName("按文件类型过滤");
			Node node = tree.addNode(fileTypeID, "fileType: .php");
			node.setName("PHP");
			node = tree.addNode(fileTypeID, "fileType: .jsp");
			node.setName("JSP");
			node = tree.addNode(fileTypeID, "fileType: .asp");
			node.setName("ASP");
			node = tree.addNode(fileTypeID, "fileType: .aspx");
			node.setName("ASPX");
			node = tree.addNode(fileTypeID, "fileType: .html");
			node.setName("HTML");
			node = tree.addNode(fileTypeID, "fileType: .htm");
			node.setName("HTM");
		}

		return tree;
	}

	public static class FileEntity {

		private String id;

		private String type;

		private String name;

		private String path;

		private Date lastModified;

		private Long length;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public Date getLastModified() {
			return lastModified;
		}

		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}

		public Long getLength() {
			return length;
		}

		public void setLength(Long length) {
			this.length = length;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}
}
