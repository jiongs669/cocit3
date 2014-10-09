package com.kmjsoft.cocit.ui;

import java.util.List;

import com.jiongsoft.cocit.entitydef.field.FakeSubSystem;
import com.jiongsoft.cocit.lang.DemsyException;
import com.jiongsoft.cocit.lang.Nodes;
import com.jiongsoft.cocit.mvc.ui.IUIViewController;
import com.jiongsoft.cocit.mvc.ui.model.UIBizFormModel;
import com.jiongsoft.cocit.mvc.ui.model.UIBizGridModel;
import com.jiongsoft.cocit.mvc.ui.model.UIBizMenuModel;
import com.jiongsoft.cocit.mvc.ui.model.UIBizNaviModel;
import com.jiongsoft.cocit.mvc.ui.model.UIBlockViewModel;
import com.jiongsoft.cocit.mvc.ui.model.UIWidgetModel;
import com.jiongsoft.cocit.mvc.ui.widget.UIBizModule;
import com.jiongsoft.cocit.mvc.ui.widget.UIBizSystem;
import com.jiongsoft.cocit.mvc.ui.widget.UIPageView;
import com.jiongsoft.cocit.mvc.ui.widget.menu.UIToolbarMenu;
import com.kmjsoft.cocit.entity.definition.IEntityAction;
import com.kmjsoft.cocit.entity.security.IModule;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.entity.web.IStatistic;
import com.kmjsoft.cocit.entity.web.IWebContentCatalog;
import com.kmjsoft.cocit.entity.webdef.IPage;
import com.kmjsoft.cocit.entity.webdef.IPageBlock;
import com.kmjsoft.cocit.entity.webdef.IStyle;
import com.kmjsoft.cocit.entity.webdef.IStyleItem;
import com.kmjsoft.cocit.entity.webdef.IUIViewComponent;
import com.kmjsoft.cocit.orm.ExtOrm;

public interface IUIEngine {

	/**
	 * 清空缓存的UI模型
	 */
	public void clearCache();

	public UIPageView makePageView(String pageID);

	/**
	 * 创建模块功能菜单
	 * 
	 * @param moduleID
	 * @return
	 * @throws DemsyException
	 */
	public UIWidgetModel makeFunctionMenuView(ITenant soft) throws DemsyException;

	/**
	 * 创建模块主界面
	 * 
	 * @param moduleID
	 * @param gridColumns
	 * @param idField
	 * @return
	 * @throws DemsyException
	 */
	public UIBizModule makeModuleView(IModule module, String gridColumns, String idField) throws DemsyException;

	/**
	 * 创建系统主界面
	 * 
	 * @param moduleID
	 *            业务模块
	 * @return 业务模块主界面
	 * @throws DemsyException
	 */
	public UIBizSystem makeSystemView(IModule module, String gridColumns, String idField) throws DemsyException;

	/**
	 * 获取系统数据网格
	 * 
	 * @param moduleID
	 *            业务模块
	 * @param gridColumns
	 *            数据网格字段， 如果该值为一个数字，则表示在网格中显示多少列？否则表示显示在网格中显示哪些字段？
	 * @return 业务模块主界面
	 * @throws DemsyException
	 */
	public UIBizGridModel makeSystemGridView(IModule module, String gridColumns, String idField, boolean existNaviTree) throws DemsyException;

	/**
	 * 创建系统数据分类导航菜单
	 * 
	 * @param moduleID
	 *            业务模块
	 * @return 业务模块导航菜单
	 * @throws DemsyException
	 */
	public UIBizNaviModel makeSystemNaviView(IModule module) throws DemsyException;

	/**
	 * 获取系统工具栏操作菜单
	 * 
	 * @param moduleID
	 * @return
	 * @throws DemsyException
	 */
	public UIBizMenuModel<UIToolbarMenu> makeSystemActionView(IModule module) throws DemsyException;

	/**
	 * 获取子模块界面
	 * 
	 * @param moduleID
	 * @return
	 * @throws DemsyException
	 */
	// public TabsDataModel getSlaveUI(IModule moduleID) throws DemsyException;

	/**
	 * 获取模块业务表单
	 * 
	 * @param moduleID
	 * @param entityAction
	 * @return
	 * @throws DemsyException
	 */
	public UIBizFormModel makeSystemFormView(IModule module, IEntityAction entityAction, Object data) throws DemsyException;

	/*
	 * 获取内置模块业务窗体模型
	 */

	// public BizModuleUI getStaticMainUI(int entityID) throws DemsyException;
	//
	// public GridDataModel getStaticGrid(int entityID) throws DemsyException;
	//
	// public NaviDataModel getStaticNaviMenu(int entityID) throws
	// DemsyException;
	//
	// public MenuDataModel<ToolbarMenu> getStaticActionTMenu(int entityID)
	// throws DemsyException;
	//
	// public FormDataModel getStaticForm(int entityID, int actionID) throws
	// DemsyException;

	public IPage loadPageTemplate(Long pageID);

	public IPageBlock loadPageBlock(Long blockID);

	public List<? extends IPageBlock> loadPageBlocks(Long pageID);

	public IUIViewComponent loadViewComponent(Long id);

	public IUIViewController getUIController(String classname);

	public UIPageView makePageView(Long pageID, Long dynamicBlockID, Long dynamicModuleID, Long dynamicDataID);

	public UIPageView makePageView(IPage uiPage, IPageBlock dynamicBlock, Long dynamicModuleID, Long dynamicDataID);

	/**
	 * 
	 * @param uiPage
	 *            页面视图
	 * @param parentExprModel
	 *            板块父视图
	 * @param pageBlock
	 *            页面板块
	 * @param dynamicModuleID
	 *            动态模块ID
	 * @param dynamicDataID
	 *            动态数据ID
	 * @param pathModule
	 *            URL路径模块
	 * @param pathData
	 *            URL路径模块指定的数据
	 * @return
	 */
	public UIBlockViewModel makeBlockView(IPageBlock pageBlock, Long dynamicModuleID, Long dynamicDataID, IModule pathModule, Object pathData);

	public IStyle loadStyle(Long styleID);

	public Nodes makeNodesOfViewCompnents(Long blockID);

	public IWebContentCatalog loadWebContentCatalog(Long catalogID);

	public IWebContentCatalog loadWebContentCatalog(String catalogGuid);

	public IPage loadIndexPage();

	public void addClickNum(ExtOrm orm, IStatistic obj);

	public void addCommentNum(ExtOrm orm, IStatistic obj);

	public IStyle makeStyle(String styleID, FakeSubSystem<? extends IStyleItem> styleItems);

}
