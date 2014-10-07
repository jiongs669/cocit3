package com.kmjsoft.cocit.entity.webdef;

import com.jiongsoft.cocit.entitydef.field.Upload;
import com.kmjsoft.cocit.entity.INamedEntity;

/**
 * 视图组件库：描述可供使用的视图组件
 * 
 * @author yongshan.ji
 * 
 */
public interface IUIViewComponent extends INamedEntity {

	public String getViewTemplate();

	public String getViewExpression();

	public String getViewController();

	public IUIViewComponent getParent();

	public Upload getImage();

	public int getDefaultWidth();

	public int getDefaultHeight();

	public boolean isSummOptions();

	public boolean isScrollOptions();

	public boolean isTitleOptions();

	public boolean isImageOptions();

}
