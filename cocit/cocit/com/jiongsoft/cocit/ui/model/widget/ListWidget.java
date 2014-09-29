package com.jiongsoft.cocit.ui.model.widget;

import java.util.List;

import com.jiongsoft.cocit.ui.model.WidgetModel;

/**
 * @author yongshan.ji
 * 
 */
public class ListWidget extends WidgetModel {

	private String name;

	// List数据，如果该值为Null，则将通过AJAX方式加载List数据。
	private List data;

	// List数据“增、删、查、改”操作的URL地址
	private String dataLoadUrl;

	private String dataDeleteUrl;

	private String dataEditUrl;

	private String dataAddUrl;

	// List列
	private List<Column> columns;

	public ListWidget() {
		super();
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

}
