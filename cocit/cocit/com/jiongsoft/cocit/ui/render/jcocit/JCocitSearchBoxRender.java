package com.jiongsoft.cocit.ui.render.jcocit;

import java.io.Writer;
import java.util.List;

import com.jiongsoft.cocit.service.FieldService;
import com.jiongsoft.cocit.ui.model.widget.SearchBoxWidget;
import com.jiongsoft.cocit.ui.render.WidgetRender;
import com.jiongsoft.cocit.util.KeyValue;
import com.jiongsoft.cocit.util.ObjectUtil;

public class JCocitSearchBoxRender extends WidgetRender<SearchBoxWidget> {

	@Override
	public void render(Writer out, SearchBoxWidget model) throws Throwable {

		// 下拉菜单：分类查询
		List<KeyValue> list = model.getData();

		String token = model.get("token", "");
		print(out, "<input id=\"searchbox_%s\" class=\"jCocit-ui jCocit-searchbox\" data-options=\"token:'%s', prompt:'', height: 24, width:%s, onSearch: jCocit.entity.doSearch", token, token, 250);

		// 下拉菜单：分类查询
		if (!ObjectUtil.isNil(list)) {
			print(out, ", menu:'#searchbox_menu_%s'", model.get("token", model.getId()));
		}

		print(out, "\"/>");

		// 下拉菜单：分类查询
		if (!ObjectUtil.isNil(list)) {
			print(out, "<div id=\"searchbox_menu_%s\" data-options=\"minWidth:150\">", model.get("token", model.getId()));

			print(out, "<div data-options=\"name: ''\">关键字</div>");
			int count = 0;
			String value;
			String key;
			byte type = 0;
			for (KeyValue obj : list) {
				value = obj.getValue();
				key = obj.getKey();
				type = obj.get("type", (byte) 0);

				print(out, "<div data-options=\"name: '%s'\">%s", value, key);

				// 操作符子菜单
				switch (type) {
				case FieldService.TYPE_NUMBER:
				case FieldService.TYPE_DATE:
					print(out, "<div>");
					print(out, "<div data-options=\"name: '%s eq'\">%s 等于...</div>", value, key);
					print(out, "<div data-options=\"name: '%s ne'\">%s 不等于...</div>", value, key);
					print(out, "<div data-options=\"name: '%s lt'\">%s 小于...</div>", value, key);
					print(out, "<div data-options=\"name: '%s le'\">%s 小于等于...</div>", value, key);
					print(out, "<div data-options=\"name: '%s gt'\">%s 大于...</div>", value, key);
					print(out, "<div data-options=\"name: '%s ge'\">%s 大于等于...</div>", value, key);
					print(out, "<div data-options=\"name: '%s in'\">%s 在...中</div>", value, key);
					print(out, "<div data-options=\"name: '%s ni'\">%s 不在...中</div>", value, key);
					print(out, "<div data-options=\"name: '%s gl'\">%s 在...之间</div>", value, key);
					print(out, "</div>");
					break;
				case FieldService.TYPE_RICH_TEXT:
				case FieldService.TYPE_STRING:
				case FieldService.TYPE_TEXT:
				case FieldService.TYPE_UPLOAD:
					print(out, "<div style=\"width:60px;\">");
					print(out, "<div data-options=\"name: '%s eq'\">%s 等于...</div>", value, key);
					print(out, "<div data-options=\"name: '%s ne'\">%s 不等于...</div>", value, key);
					print(out, "<div data-options=\"name: '%s nu'\">%s 为空</div>", value, key);
					print(out, "<div data-options=\"name: '%s nn'\">%s 不为空</div>", value, key);
					print(out, "<div data-options=\"name: '%s cn'\">%s 包含...</div>", value, key);
					print(out, "<div data-options=\"name: '%s nc'\">%s 不包含...</div>", value, key);
					print(out, "<div data-options=\"name: '%s sw'\">%s 以...开头</div>", value, key);
					print(out, "<div data-options=\"name: '%s bn'\">%s 不以...开头</div>", value, key);
					print(out, "<div data-options=\"name: '%s ew'\">%s 以...结尾</div>", value, key);
					print(out, "<div data-options=\"name: '%s en'\">%s 以...结尾</div>", value, key);
					print(out, "<div data-options=\"name: '%s in'\">%s 在...中</div>", value, key);
					print(out, "<div data-options=\"name: '%s ni'\">%s 不在...中</div>", value, key);
					print(out, "</div>");
					break;
				default:

				}

				print(out, "</div>");
				count++;
				if (count == 12)
					break;
			}
			print(out, "</div>");
		}

	}

}
