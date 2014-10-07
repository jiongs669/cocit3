package com.kmjsoft.cocit.ui.render;

import com.kmjsoft.cocit.ui.UIModel;
import com.kmjsoft.cocit.ui.UIRender;



/**
 * CuiRender工厂：用于创建并管理CuiRender对象。
 * 
 * @author yongshan.ji
 * 
 */
public interface WidgetRenderFactory {

	UIRender getRender(Class<? extends UIModel> model);
}
