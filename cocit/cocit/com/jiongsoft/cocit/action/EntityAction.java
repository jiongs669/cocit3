package com.jiongsoft.cocit.action;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.mvc.adaptor.EntityParamAdaptor;
import com.jiongsoft.cocit.mvc.adaptor.EntityParamNode;
import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.service.FieldService;
import com.jiongsoft.cocit.ui.UIModelView;
import com.jiongsoft.cocit.ui.model.AlertsModel;
import com.jiongsoft.cocit.ui.model.widget.EntityForm;
import com.jiongsoft.cocit.ui.model.widget.EntityFormData;
import com.jiongsoft.cocit.ui.model.widget.EntityModuleUI;
import com.jiongsoft.cocit.ui.model.widget.EntityTableUI;
import com.jiongsoft.cocit.ui.model.widget.GridWidget;
import com.jiongsoft.cocit.ui.model.widget.GridWidgetData;
import com.jiongsoft.cocit.ui.model.widget.ListWidget;
import com.jiongsoft.cocit.ui.model.widget.ListWidgetData;
import com.jiongsoft.cocit.ui.model.widget.TreeWidgetData;
import com.jiongsoft.cocit.util.ClassUtil;
import com.jiongsoft.cocit.util.CocException;
import com.jiongsoft.cocit.util.ExcelUtil;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.ObjectUtil;
import com.jiongsoft.cocit.util.StringUtil;
import com.jiongsoft.cocit.util.UrlAPI;

/**
 * 实体Action：即用来管理实体数据的Action，负责接收管理实体数据的请求并处理这些请求，包括“增加、删除、查询、修改、导入、导出”等操作。
 * 
 * @author jiongs753
 * 
 */
@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
@AdaptBy(type = EntityParamAdaptor.class)
public class EntityAction {

	/**
	 * 获取“数据模块”界面模型，用于输出数据模块的界面。
	 * 
	 * @param funcExpr
	 *            Hex加密后的调用参数，参数组成“moduleID”
	 * @return
	 */
	@At(UrlAPI.GET_ENTITY_MODULE_UI)
	public EntityModuleUI getEntityModuleUI(String funcExpr) {
		ActionHelper helper = ActionHelper.make(funcExpr, null, null);

		EntityModuleUI moduleModel = helper.widgetFactory.getEntityModuleUI(helper.module);

		Log.debug("EntityAction.getEntityModuleUI: moduleModel = %s", moduleModel);

		// 返回
		return moduleModel;
	}

	/**
	 * 获取“数据表”界面模型，用于输出数据表界面。
	 * 
	 * @param opArgs
	 *            Hex加密后的调用参数，参数组成“moduleID:tableID”
	 * @return
	 */
	@At(UrlAPI.GET_ENTITY_TABLE_UI)
	public EntityTableUI getEntityTableUI(String funcExpr) {
		ActionHelper helper = ActionHelper.make(funcExpr, null, null);

		EntityTableUI tableModel = helper.widgetFactory.getEntityTableUI(helper.module, helper.table);

		Log.debug("EntityAction.getEntityTableUI: tableModel = %s", tableModel);

		// 返回
		return tableModel;
	}

	@At(UrlAPI.GET_ENTITY_SELECTION_TABLE_UI)
	public EntityTableUI getEntitySelectionTableUI(String funcExpr) {
		ActionHelper helper = ActionHelper.make(funcExpr, null, null);

		EntityTableUI tableModel = helper.widgetFactory.getEntitySelectionTableUI(helper.module, helper.table);

		Log.debug("EntityAction.getEntityTableUI: tableModel = %s", tableModel);

		// 返回
		return tableModel;
	}

	/**
	 * 获取“数据表GRID”数据模型，用于输出数据表GRID所需要的JSON数据。
	 * 
	 * @param opArgs
	 *            加密后的调用参数，参数组成“moduleID:tableID”
	 * @return
	 */
	@At(UrlAPI.GET_ENTITY_GRID_DATA)
	public GridWidgetData getEntityGridData(String funcExpr) {
		ActionHelper helper = ActionHelper.make(funcExpr, null, null);

		GridWidget gridWidget = helper.widgetFactory.getGridUI(helper.module, helper.table);

		/*
		 * 构造Grid数据模型
		 */
		GridWidgetData ret = new GridWidgetData();
		ret.setModel(gridWidget);

		// 构造查询条件
		CndExpr expr = helper.makeExpr();
		try {
			List data = helper.entityManager.query(expr, null);
			int total = helper.entityManager.count(expr, null);

			ret.setData(data);
			ret.setTotal(total);

			Log.debug("EntityAction.getEntityGridData: total = %s", total);
		} catch (CocException e) {
			ret.setException(e);
		}

		return ret;
	}

	@At(UrlAPI.GET_ENTITY_LIST_DATA)
	public ListWidgetData getEntityListData(String funcExpr) {
		ActionHelper helper = ActionHelper.make(funcExpr, null, null);

		ListWidget listWidget = helper.widgetFactory.getListUI(helper.module, helper.table);

		/*
		 * 构造Grid数据模型
		 */
		ListWidgetData ret = new ListWidgetData();
		ret.setModel(listWidget);

		// 构造查询条件
		CndExpr expr = helper.makeExpr();
		try {

			List data = helper.entityManager.query(expr, null);
			// int total = helper.entityManager.count(expr, null);

			ret.setData(data);

			Log.debug("EntityAction.getEntityListData...");
		} catch (CocException e) {
			ret.setException(e);
		}

		return ret;
	}

	/**
	 * 获取“获取数据表导航树”数据模型，用于输出树所需要的JSON数据。
	 * 
	 * @param opArgs
	 *            加密后的调用参数，参数组成“moduleID:tableID”
	 * @return
	 */
	@At(UrlAPI.GET_ENTITY_NAVI_DATA)
	public TreeWidgetData getEntityNaviData(String funcExpr) {
		ActionHelper helper = ActionHelper.make(funcExpr, null, null);

		TreeWidgetData treeModel = helper.widgetFactory.getEntityNaviData(helper.module, helper.table);

		return treeModel;
	}

	@At(UrlAPI.GET_ENTITY_TREE_DATA)
	public TreeWidgetData getEntityTreeData(String funcExpr) {
		ActionHelper helper = ActionHelper.make(funcExpr, null, null);

		TreeWidgetData treeModel = helper.widgetFactory.getEntityTreeData(helper.module, helper.table);

		return treeModel;
	}

	/**
	 * 
	 * 获取业务数据表单模型
	 * 
	 * @param opArgs
	 *            调用参数，参数组成“moduleID:tableID:operationID”
	 * @param rowID
	 *            实体数据ID
	 * @param rowNode
	 *            实体数据行参数节点
	 * @return
	 */
	@At(UrlAPI.GET_ENTITY_ROW_FORM)
	public EntityForm getEntityRowForm(String funcExpr, String rowID, @Param("::entity.") EntityParamNode rowNode) {
		ActionHelper helper = ActionHelper.make(funcExpr, rowID, rowNode);

		EntityForm formModel = helper.widgetFactory.getEntityFormUI(helper.module, helper.table, helper.op, helper.entity);

		formModel.setData(helper.entity);

		/**
		 * 返回
		 */
		return formModel;
	}

	@At(UrlAPI.GET_ENTITY_ROWS_FORM)
	public EntityForm getEntityRowsForm(String funcExpr, String rowID, @Param("::entity.") EntityParamNode rowNode) {
		return this.getEntityRowForm(funcExpr, rowID, rowNode);
	}

	/**
	 * 
	 * 保存业务数据
	 * 
	 * @param opArgs
	 *            调用参数，参数组成“moduleID:tableID:operationID”
	 * @param rowID
	 *            实体数据ID
	 * @param rowNode
	 *            实体数据行参数节点
	 * @return
	 */
	@At(UrlAPI.SAVE_ENTITY_ROW)
	public EntityFormData saveEntityRow(String funcExpr, String rowID, @Param("::entity.") EntityParamNode rowNode) {
		ActionHelper helper = ActionHelper.make(funcExpr, rowID, rowNode);

		EntityForm formModel = helper.widgetFactory.getEntityFormUI(helper.module, helper.table, helper.op, helper.entity);

		EntityFormData ret = new EntityFormData();
		ret.setModel(formModel);
		ret.setData(helper.entity);

		try {
			ObjectUtil.setValue(helper.entity, "softID", helper.actionContext.getSoftID());
			helper.entityManager.save(helper.entity, helper.opMode);
		} catch (Throwable e) {
			ret.setException(e);
		}

		return ret;
	}

	@At(UrlAPI.SAVE_ENTITY_ROWS)
	public EntityFormData saveEntityRows(String funcExpr, String rowID, @Param("::entity.") EntityParamNode rowNode) {
		ActionHelper helper = ActionHelper.make(funcExpr, rowID, rowNode);

		EntityForm formModel = helper.widgetFactory.getEntityFormUI(helper.module, helper.table, helper.op, helper.entity);

		EntityFormData ret = new EntityFormData();
		ret.setModel(formModel);
		ret.setData(helper.entity);

		try {
			ObjectUtil.setValue(helper.entity, "softID", helper.actionContext.getSoftID());
			helper.entityManager.execTask(helper, helper.opMode);
		} catch (Throwable e) {
			ret.setException(e);
		}

		return ret;
	}

	/**
	 * 删除实体列表数据：即批量删除实体记录
	 * 
	 * @param funcExpr
	 * @param rows
	 *            逗号分隔的实体数据行ID列表
	 * @return
	 */
	@At(UrlAPI.DEL_ENTITY_ROWS)
	public EntityFormData delEntityRows(String funcExpr, String rows) {
		ActionHelper helper = ActionHelper.make(funcExpr, rows, null);

		EntityForm formModel = helper.widgetFactory.getEntityFormUI(helper.module, helper.table, helper.op, helper.entity);

		EntityFormData ret = new EntityFormData();
		ret.setModel(formModel);
		ret.setData(helper.entity);

		try {
			String[] array = StringUtil.toArray(rows);
			// Long[] idArray = new Long[array.length];
			List list = new ArrayList();
			for (int i = 0; i < array.length; i++) {
				// idArray[i] = Long.parseLong(array[i]);
				Object obj = ClassUtil.newInstance(helper.entityManager.getType());
				ObjectUtil.setValue(obj, "id", Long.parseLong(array[i]));
				list.add(obj);
			}
			helper.entityManager.delete(list, helper.opMode);
		} catch (Throwable e) {
			ret.setException(e);
		}

		return ret;
	}

	/**
	 * 
	 * @param funcExpr
	 * @param dataID
	 * @return
	 */
	@At(UrlAPI.RUN_PLUGIN_ON_EXPR)
	public AlertsModel runPluginOnExpr(String funcExpr, String dataID) {
		ActionHelper helper = ActionHelper.make(funcExpr, dataID, null);

		try {
			String result = helper.entityManager.execTask(helper, helper.opMode);

			return AlertsModel.makeSuccess(result);
		} catch (Throwable e) {
			return AlertsModel.makeError(e == null ? "" : e.getMessage());
		}
	}

	//
	// /**
	// * 执行异步任务
	// *
	// * @param args
	// * @param entityID
	// * @param dataNode
	// * @return
	// */
	// @At(UrlAPI.RUN_ENTITY_ASYN_ROWS)
	// public AlertsModel runEntityAsynRows(String args, String entityID) {
	// ActionHelper helper = ActionHelper.make(args, entityID, null);
	//
	// try {
	// String result = helper.entityManager.execAsynTask(helper, helper.opMode);
	//
	// return AlertsModel.makeSuccess(result);
	// } catch (Throwable e) {
	// return AlertsModel.makeError(e == null ? "" : e.toString());
	// }
	// }

	/**
	 * 对应JSP: {@value UrlAPI#JSP_DIR}/getExportXlsForm.jsp
	 * 
	 * @return
	 */
	@At(UrlAPI.GET_EXPORT_XLS_FORM)
	public EntityForm getExportXlsForm(String args, String argDataID) {
		ActionHelper helper = ActionHelper.make(args, argDataID, null);

		EntityForm formModel = helper.widgetFactory.getEntityFormUI(helper.module, helper.table, helper.op, helper.entity);
		// if (StringUtil.isNil(formModel.getJsp()))
		formModel.setJsp(UrlAPI.JSP_DIR + "/getExportXlsForm");

		formModel.setVar("actionHelper", helper);
		formModel.setVar("query.filterExpr", StringUtil.escapeHTML(helper.actionContext.getParameterValue("query.filterExpr", "")));
		formModel.setVar("query.parentExpr", StringUtil.escapeHTML(helper.actionContext.getParameterValue("query.parentExpr", "")));
		formModel.setVar("query.keywords", StringUtil.escapeHTML(helper.actionContext.getParameterValue("query.keywords", "")));
		formModel.setVar("sortField", StringUtil.escapeHTML(helper.actionContext.getParameterValue("sortField", "")));
		formModel.setVar("sortOrder", StringUtil.escapeHTML(helper.actionContext.getParameterValue("sortOrder", "")));

		/**
		 * 返回
		 */
		return formModel;
	}

	/**
	 * 该方法在执行{@value UrlAPI#JSP_DIR}/getExportXlsForm.jsp中的form.submit()时调用。
	 * 
	 * @param args
	 * @param argDataID
	 */
	@At(UrlAPI.DO_EXPORT_XLS_ON_EXPR)
	public void doExportXlsOnExpr(String args, String argDataID) {
		ActionHelper helper = ActionHelper.make(args, argDataID, null);

		OutputStream outStream = null;
		try {
			List<String[]> excelRows = new ArrayList();

			// 生成Excel表头
			String[] columns = helper.actionContext.getParameterValues("columns");
			String[] header = new String[columns.length];
			Map<String, FieldService> fields = helper.table.getEntityFieldsPropMap();
			for (int i = 0; i < columns.length; i++) {
				String col = columns[i];
				FieldService fld = fields.get(col);
				header[i] = fld.getName();
			}
			excelRows.add(header);

			// 查询数据
			CndExpr expr = helper.makeExpr();
			List list = helper.entityManager.query(expr, null);

			// 生成Excel行
			String[] row;
			for (Object obj : list) {
				row = new String[columns.length];
				for (int i = 0; i < columns.length; i++) {
					String col = columns[i];

					Object value = ObjectUtil.getValue(obj, col);
					FieldService fld = fields.get(col);
					String strValue = fld.format(value);

					row[i] = strValue;
				}
				excelRows.add(row);
			}

			// 发送Excel文件
			HttpServletResponse response = helper.actionContext.getResponse();
			String fileName = helper.table.getName();
			fileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachement; filename=" + fileName + ".xls");
			response.setContentType("application/octet-stream");
			outStream = response.getOutputStream();
			ExcelUtil.makeExcel(outStream, excelRows);

			Log.debug("EntityAction.exportXls: total = %s", list == null ? 0 : list.size());
		} catch (Throwable e) {
			Log.error("EntityAction.exportXls: error! ", e);
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Throwable ex) {
				}
			}
		}
	}

	/**
	 * 对应JSP: {@value UrlAPI#JSP_DIR}/getImportXlsForm.jsp
	 * 
	 * @return
	 */
	@At(UrlAPI.GET_IMPORT_XLS_FORM)
	public EntityForm getImportXlsForm(String args, String argDataID) {
		ActionHelper helper = ActionHelper.make(args, argDataID, null);

		EntityForm formModel = helper.widgetFactory.getEntityFormUI(helper.module, helper.table, helper.op, helper.entity);
		// if (StringUtil.isNil(formModel.getJsp()))
		formModel.setJsp(UrlAPI.JSP_DIR + "/getImportXlsForm");

		formModel.setVar("actionHelper", helper);
		formModel.setVar("query.filterExpr", StringUtil.escapeHTML(helper.actionContext.getParameterValue("query.filterExpr", "")));
		formModel.setVar("query.parentExpr", StringUtil.escapeHTML(helper.actionContext.getParameterValue("query.parentExpr", "")));
		formModel.setVar("query.keywords", StringUtil.escapeHTML(helper.actionContext.getParameterValue("query.keywords", "")));
		formModel.setVar("sortField", StringUtil.escapeHTML(helper.actionContext.getParameterValue("sortField", "")));
		formModel.setVar("sortOrder", StringUtil.escapeHTML(helper.actionContext.getParameterValue("sortOrder", "")));

		/**
		 * 返回
		 */
		return formModel;
	}

	/**
	 * 该方法在执行{@value UrlAPI#JSP_DIR}/getImportXlsForm.jsp中的form.submit()时调用。
	 * 
	 * @param args
	 * @param argDataID
	 */
	@At(UrlAPI.DO_IMPORT_XLS_ON_EXPR)
	public AlertsModel doImportXlsOnExpr(String args, String argDataID) {
		ActionHelper helper = ActionHelper.make(args, argDataID, null);

		OutputStream outStream = null;
		try {
			String excelFilePath = helper.actionContext.getParameterValue("excelFilePath", "");
			File excelFile = new File(Cocit.getContextDir() + excelFilePath);
			List dataRows = helper.table.parseEntityDataFrom(excelFile);
			Long softID = helper.actionContext.getSoftID();
			for (Object row : dataRows) {
				ObjectUtil.setValue(row, "softID", softID);
			}
			helper.entityManager.save(dataRows, helper.opMode);

			Log.debug("EntityAction.importXls: total = %s", dataRows == null ? 0 : dataRows.size());
			return AlertsModel.makeSuccess("共导入了 " + (dataRows == null ? 0 : dataRows.size()) + " 条数据！");
		} catch (Throwable e) {
			Log.error("EntityAction.importXls: error! ", e);
			return AlertsModel.makeError("导入数据出错: " + (e == null ? "" : e.getMessage()));
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Throwable ex) {
				}
			}
		}
	}
}
