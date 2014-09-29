package com.jiongsoft.cocit.ui.render.jcocit;

import java.util.Hashtable;
import java.util.Map;

import com.jiongsoft.cocit.ui.UIModel;
import com.jiongsoft.cocit.ui.UIRender;
import com.jiongsoft.cocit.ui.model.widget.EntityFormData;
import com.jiongsoft.cocit.ui.model.widget.EntityForm;
import com.jiongsoft.cocit.ui.model.widget.EntityModuleUI;
import com.jiongsoft.cocit.ui.model.widget.EntityTableUI;
import com.jiongsoft.cocit.ui.model.widget.GridWidgetData;
import com.jiongsoft.cocit.ui.model.widget.GridWidget;
import com.jiongsoft.cocit.ui.model.widget.MenuWidget;
import com.jiongsoft.cocit.ui.model.widget.SearchBoxWidget;
import com.jiongsoft.cocit.ui.model.widget.TreeWidgetData;
import com.jiongsoft.cocit.ui.model.widget.TreeWidget;
import com.jiongsoft.cocit.ui.render.WidgetRenderFactory;
import com.jiongsoft.cocit.util.Log;

/**
 * 输出支持jCocit.js jQuery框架的HTML/Json/XML。
 * 
 * @author yongshan.ji
 * 
 */
public class JCocitWidgetRenderFactory implements WidgetRenderFactory {
	private Map<Class, UIRender> renders;

	public JCocitWidgetRenderFactory() {
		renders = new Hashtable();
	}

	@Override
	public UIRender getRender(Class<? extends UIModel> modelType) {

		UIRender ret = renders.get(modelType);
		if (ret == null) {
			ret = makeRender(modelType);

			// TODO: Develop mode dont cache it.
			// if (ret != null)
			// renders.put(modelType, ret);
		}

		return ret;
	}

	private UIRender makeRender(Class modelType) {

		/*
		 * 创建模型Render
		 */
		if (modelType.equals(EntityModuleUI.class))
			return new JCocitEntityModuleRender();

		if (modelType.equals(EntityTableUI.class))
			return new JCocitEntityTableRender();

		if (modelType.equals(EntityForm.class))
			return new JCocitEntityFormRender();
		if (modelType.equals(EntityFormData.class))
			return new JCocitEntityFormDataRender();

		if (modelType.equals(GridWidget.class))
			return new JCocitGridRenders.ModelRender();
		if (modelType.equals(GridWidgetData.class))
			return new JCocitGridRenders.DataRender();

		if (modelType.equals(MenuWidget.class))
			return new JCocitMenuRender();

		if (modelType.equals(SearchBoxWidget.class))
			return new JCocitSearchBoxRender();

		if (modelType.equals(TreeWidget.class))
			return new JCocitTreeRenders.ModelRender();
		if (modelType.equals(TreeWidgetData.class))
			return new JCocitTreeRenders.DataRender();

		/*
		 * 创建CoC UIRender 失败！
		 */
		Log.error("JCocitWidgetRenderFactory.makeRender: 创建失败！{modelType:%s}", modelType.getName());

		return null;
	}

}
