package com.jiongsoft.cocit.ui.model.widget;

import com.jiongsoft.cocit.ui.model.WidgetModel;
import com.jiongsoft.cocit.util.Tree;

/**
 * 树窗体界面模型：可以包含树所需要的数据，如果数据不存在则表示将异步获取JSON格式的树型数据。
 * 
 * <B>属性设置：</B>
 * <UL>
 * <LI>checkbox: bool值，true——支持checkbox多选框；falst——不支持checkbox多选框
 * </UL>
 * 
 * @author jiongsoft
 * 
 */
public class TreeWidget extends WidgetModel {

	// Tree数据，如果该值为Null，则将通过AJAX方式加载树数据。
	private Tree data;

	// Grid数据“增、删、查、改”操作的URL地址
	private String dataLoadUrl;
	private String dataDeleteUrl;
	private String dataEditUrl;
	private String dataAddUrl;

	public String getDataLoadUrl() {
		return dataLoadUrl;
	}

	public void setDataLoadUrl(String dataLoadUrl) {
		this.dataLoadUrl = dataLoadUrl;
	}

	public String getDataDeleteUrl() {
		return dataDeleteUrl;
	}

	public void setDataDeleteUrl(String dataDeleteUrl) {
		this.dataDeleteUrl = dataDeleteUrl;
	}

	public String getDataEditUrl() {
		return dataEditUrl;
	}

	public void setDataEditUrl(String dataEditUrl) {
		this.dataEditUrl = dataEditUrl;
	}

	public String getDataAddUrl() {
		return dataAddUrl;
	}

	public void setDataAddUrl(String dataUpdateUrl) {
		this.dataAddUrl = dataUpdateUrl;
	}

	public Tree getData() {
		return data;
	}

	public void setData(Tree data) {
		this.data = data;
	}

}
