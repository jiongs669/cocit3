package com.kmjsoft.cocit.entity.module;

import com.kmjsoft.cocit.entity.ITreeEntity;

/**
 * 动作：即在功能模块上执行的操作。
 * 
 * @author yongshan.ji
 */
public interface IEntityAction extends ITreeEntity {
	String getModuleGuid();

	/**
	 * 
	 * 操作模式：是{@link #getDataGuid()}的别名。
	 * <p>
	 * 操作模式和{@link #getModuleGuid()}共同组成了实体操作的逻辑主键。
	 * 
	 * @return
	 */
	String getMode();

	Integer getTypeCode();

	String getPlugin();

	String getImage();

	String getLogo();

	/**
	 * 执行该操作时的显示的页面模版
	 * 
	 * @return
	 */
	String getPageTemplate();

	String getInfo();

	String getError();

	String getWarn();

	String getTargetUrl();

	String getTargetWindow();

	String getParams();
}
