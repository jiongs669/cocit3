package com.jiongsoft.cocit.ui.model.widget;

import java.util.List;

import com.jiongsoft.cocit.ui.model.WidgetData;


/**
 * Grid数据模型：由Grid界面模型和Grid数据组成。
 * 
 * @author jiongsoft
 * 
 */
public class GridWidgetData extends WidgetData<GridWidget, List> {
	private int total;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
