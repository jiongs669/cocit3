package com.jiongsoft.cocit.ui.model.widget;

import java.util.ArrayList;
import java.util.List;

import com.jiongsoft.cocit.ui.model.WidgetModel;
import com.jiongsoft.cocit.util.UrlAPI;

/**
 * 数据表Grid窗体界面模型：由多个Grid列和数据组成，如果数据不存在则表示将异步获取Grid数据。
 * 
 * <b>属性说明：</b>
 * <UL>
 * <LI>rownumbers: bool值，是否在Grid中显示行号？
 * <LI>checkbox: bool值，是否在Grid的第一列显示复选框？
 * <LI>singleSelect: bool值，表示Grid是否只能单选？
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class GridWidget extends WidgetModel {

	private String name;

	// Grid数据，如果该值为Null，则将通过AJAX方式加载Grid数据。
	private List data;
	private int pageSize = UrlAPI.DEFAULT_PAGE_SIZE;

	// Grid数据“增、删、查、改”操作的URL地址
	private String dataLoadUrl;
	private String dataDeleteUrl;
	private String dataEditUrl;
	private String dataAddUrl;

	// Grid列
	private List<Column> columns;
	private int columnsTotalWidth;

	public GridWidget() {
		super();
		columns = new ArrayList();
	}

	public void addColumn(Column col) {
		columns.add(col);
	}

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

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getColumnsTotalWidth() {
		return columnsTotalWidth;
	}

	public void setColumnsTotalWidth(int columnsTotalWidth) {
		this.columnsTotalWidth = columnsTotalWidth;
	}

}
