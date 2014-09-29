package com.jiongsoft.cocit.ui.model.widget;

import com.jiongsoft.cocit.service.ModuleService;
import com.jiongsoft.cocit.service.OperationService;
import com.jiongsoft.cocit.service.TableService;

/**
 * UI窗体模型工厂：负责创建或管理用户窗体界面
 * 
 * @author jiongs753
 * 
 */
public interface WidgetModelFactory {

	/**
	 * 获取“数据模块”界面模型
	 * 
	 * @param module
	 * @return
	 */
	EntityModuleUI getEntityModuleUI(ModuleService module);

	/**
	 * 获取数据表主界面窗体模型
	 * 
	 * @param module
	 * @param table
	 * @return
	 */
	EntityTableUI getEntityTableUI(ModuleService module, TableService table);

	EntityTableUI getEntitySelectionTableUI(ModuleService module, TableService table);

	EntityForm getEntityFormUI(ModuleService module, TableService table, OperationService op, Object entity);

	/**
	 * 获取数据表Grid模型
	 * 
	 * @param module
	 * @param table
	 * @return
	 */
	GridWidget getGridUI(ModuleService module, TableService table);

	/**
	 * 获取数据表List模型
	 * 
	 * @param module
	 * @param table
	 * @return
	 */
	ListWidget getListUI(ModuleService module, TableService table);

	/**
	 * 获取数据表搜索框模型
	 * 
	 * @param module
	 * @param table
	 * @return
	 */
	SearchBoxWidget getSearchBoxUI(ModuleService module, TableService table);

	/**
	 * 获取数据表操作菜单模型
	 * 
	 * @param module
	 * @param table
	 * @return
	 */
	MenuWidget getOperationMenuUI(ModuleService module, TableService table);

	/**
	 * 获取数据表导航树模型
	 * 
	 * @param module
	 * @param table
	 * @return
	 */
	TreeWidget getEntityNaviUI(ModuleService module, TableService table);

	/**
	 * 获取数据表自身树
	 * 
	 * @param module
	 * @param table
	 * @return
	 */
	TreeWidget getEntityTreeUI(ModuleService module, TableService table);

	/**
	 * 获取数据表Navi数据
	 * 
	 * @param module
	 * @param table
	 * @return
	 */
	TreeWidgetData getEntityNaviData(ModuleService module, TableService table);

	/**
	 * 数据实体自身树
	 * 
	 * @param module
	 * @param table
	 * @return
	 */
	TreeWidgetData getEntityTreeData(ModuleService module, TableService table);

}
