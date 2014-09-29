package com.jiongsoft.cocit.ui.model.widget;

import java.io.Writer;
import java.util.List;

import com.jiongsoft.cocit.ui.model.WidgetData;
import com.jiongsoft.cocit.util.Json;
import com.jiongsoft.cocit.util.ObjectUtil;

/**
 * Grid数据模型：由Grid界面模型和Grid数据组成。
 * 
 * @author jiongsoft
 * 
 */
public class ListWidgetData extends WidgetData<ListWidget, List> {

	@Override
	public void render(Writer out) throws Throwable {

		// ListWidget gridModel = getModel();
		// List<Column> columns = gridModel.getColumns();

		List data = getData();
		if (data != null) {

			boolean noFirstRow = false;
			StringBuffer sb = new StringBuffer();
			sb.append('[');
			for (Object obj : data) {

				if (noFirstRow) {
					sb.append(",{");
				} else {
					sb.append('{');
					noFirstRow = true;
				}

				sb.append(String.format("\"id\":%s", Json.toJson(ObjectUtil.getValue(obj, "id"))));
				sb.append(String.format(",\"text\":%s", Json.toJson(obj == null ? "" : obj.toString())));

				// for (Column col : columns) {
				// String prop = col.getField();
				// Object value = ObjectUtil.getValue(obj, prop);
				// value = col.getEntityField().format(value);
				//
				// sb.append(String.format(",\"%s\":%s", prop, Json.toJson(value)));
				// }

				sb.append('}');
			}
			sb.append(']');

			out.write(sb.toString());
		}
	}
}
