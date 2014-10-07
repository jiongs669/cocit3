package com.kmjsoft.cocit.ui.model.widget;

import java.util.List;

import com.kmjsoft.cocit.ui.model.WidgetModel;
import com.kmjsoft.cocit.util.KeyValue;

public class SearchBoxWidget extends WidgetModel {
	private List<KeyValue> data;

	public List<KeyValue> getData() {
		return data;
	}

	public void setData(List<KeyValue> data) {
		this.data = data;
	}

}
