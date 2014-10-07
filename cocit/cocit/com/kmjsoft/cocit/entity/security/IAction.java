package com.kmjsoft.cocit.entity.security;

import com.jiongsoft.cocit.entitydef.field.Upload;
import com.kmjsoft.cocit.entity.INamedEntity;

/**
 * 动作：即在功能模块上执行的操作。
 * 
 * @author yongshan.ji
 */
public interface IAction extends INamedEntity {
	IAction getActionDefinition();

	IAction getParentAction();

	Integer getTypeCode();

	String getMode();

	String getPlugin();

	Upload getImage();

	Upload getLogo();

	/**
	 * 执行该操作时的显示的页面模版
	 * 
	 * @return
	 */
	String getTemplate();

	String getInfo();

	String getError();

	String getWarn();

	String getTargetUrl();

	String getTargetWindow();

	String getParams();
}
