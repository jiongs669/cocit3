package com.jiongsoft.cocit.ui.render;

import com.jiongsoft.cocit.ui.UIModel;
import com.jiongsoft.cocit.ui.UIRender;



/**
 * CuiRender工厂：用于创建并管理CuiRender对象。
 * 
 * @author yongshan.ji
 * 
 */
public interface WidgetRenderFactory {

	UIRender getRender(Class<? extends UIModel> model);
}
