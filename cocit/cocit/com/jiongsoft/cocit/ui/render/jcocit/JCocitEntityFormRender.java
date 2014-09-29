package com.jiongsoft.cocit.ui.render.jcocit;

import static com.jiongsoft.cocit.service.FieldService.TYPE_BOOL;
import static com.jiongsoft.cocit.service.FieldService.TYPE_DATE;
import static com.jiongsoft.cocit.service.FieldService.TYPE_FK;
import static com.jiongsoft.cocit.service.FieldService.TYPE_NUMBER;
import static com.jiongsoft.cocit.service.FieldService.TYPE_RICH_TEXT;
import static com.jiongsoft.cocit.service.FieldService.TYPE_TEXT;
import static com.jiongsoft.cocit.service.FieldService.TYPE_UPLOAD;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.service.FieldService;
import com.jiongsoft.cocit.service.TableService;
import com.jiongsoft.cocit.ui.model.widget.Column;
import com.jiongsoft.cocit.ui.model.widget.EntityForm;
import com.jiongsoft.cocit.ui.model.widget.EntityForm.FormField;
import com.jiongsoft.cocit.ui.model.widget.GridWidget;
import com.jiongsoft.cocit.ui.model.widget.TreeWidget;
import com.jiongsoft.cocit.ui.render.WidgetRender;
import com.jiongsoft.cocit.util.KeyValue;
import com.jiongsoft.cocit.util.ObjectUtil;
import com.jiongsoft.cocit.util.StringUtil;

public class JCocitEntityFormRender extends WidgetRender<EntityForm> {

	@Override
	public void render(Writer writer, EntityForm model) throws Throwable {
		StringBuffer sb = new StringBuffer();

		Object formData = model.getData();
		print(sb, "<form class=\"entity-form\">");
		print(sb, "<input name=\"entity.id\" type=\"hidden\" value=\"%s\">", ObjectUtil.idOrtoString(formData));

		FieldService entityField;
		String propName;
		Object objFldValue;
		/*
		 * 计算可见字段、隐藏字段、不显示的字段等
		 */
		for (FormField group : model.getGroupFields()) {
			List<FormField> fields = group.getChildren();
			for (FormField field : fields) {

				entityField = field.getEntityField();
				propName = entityField.getPropName();
				objFldValue = ObjectUtil.getValue(formData, propName);
				String strFldValue = ObjectUtil.idOrtoString(objFldValue);
				String mode = field.getMode();

				// N：不显示
				if (mode.equals("N"))
					continue;

				// P：字段有值，则显示
				if (mode.equals("P") && (StringUtil.isNil(strFldValue)))
					continue;

				// H：隐藏字段
				if (mode.equals("H")) {
					print(sb, "\"<input type=\"hidden\" name=\"entity.%s\" value=\"%s\" />\"", propName, strFldValue);
					continue;
				}

				// 需显示的字段
				group.addVisibleChild(field);
			}
		}

		print(sb, "<div class=\"entity-groups\">");
		for (FormField group : model.getGroupFields()) {
			List<FormField> visibleFields = group.getVisibleChildren();

			// 该分组中没有需要显示的字段
			if (visibleFields.size() == 0)
				continue;

			print(sb, "<div class=\"entity-group\"><div class=\"entity-group-header\">%s</div>", group.getTitle());

			// fields table
			print(sb, "<table valign=\"top\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
			print(sb, "<tr>");

			int count = 0;
			boolean isNewRow = false;
			int colspan = 1;
			for (FormField field : visibleFields) {
				if (count % 2 == 0) {
					isNewRow = true;
				}

				entityField = field.getEntityField();

				byte type = entityField.getType();
				if (type == TYPE_TEXT || type == TYPE_RICH_TEXT) {
					if (!isNewRow)
						isNewRow = true;
					colspan = 3;
				}
				if (isNewRow && count != 0) {
					print(sb, "</tr>");
					print(sb, "<tr>");
				}

				propName = entityField.getPropName();
				objFldValue = ObjectUtil.getValue(formData, propName);
				String strFldValue = ObjectUtil.idOrtoString(objFldValue);

				String mode = field.getMode();

				print(sb, "<th class=\"entity-field-header\">");

				printFieldLabel(sb, model, entityField);

				print(sb, "</th>");
				print(sb, "<td class=\"entity-field-box\"");
				if (colspan > 1) {
					print(sb, " colspan=\"" + colspan + "\"");
				}
				print(sb, ">");

				// I：检查字段值，字段值被隐藏且可以提交。
				if (mode.equals("I")) {
					print(sb, "\"<input type=\"hidden\" name=\"entity.%s\" value=\"%s\" />\"", propName, strFldValue);
					print(sb, "<span>%s</span>", entityField.format(objFldValue));
				}
				// R：只读字段显示的是字段文本，隐藏的是字段值。
				else if (mode.equals("R")) {
					print(sb, "\"<input type=\"hidden\" name=\"entity.%s\" value=\"%s\" />\"", propName, strFldValue);
					print(sb, "\"<input value=\"%s\" readonly=\"true\" />\"", propName, entityField.format(objFldValue));
				}
				// D：禁用字段，字段值不能被提交
				else if (mode.equals("D")) {
					print(sb, "\"<input value=\"%s\" disabled=\"true\" />\"", propName, entityField.format(objFldValue));
				}
				// S：显示字段值文本
				else if (mode.equals("P") || mode.equals("S")) {
					print(sb, "<span>%s</span>", entityField.format(objFldValue));
				}
				// E：编辑模式
				else if (mode.equals("E") || mode.equals("M")) {
					printEditBox(sb, model, entityField, objFldValue);

					// M：字段必填
					if (mode.equals("M")) {
						print(sb, "<span class=\"icon-mode-%s\">&nbsp;&nbsp;&nbsp;</span>", mode);
					}
				}

				print(sb, "</td>");

				count++;
				isNewRow = false;
				colspan = 1;
			}
			print(sb, "</tr>");
			print(sb, "</table></div>");// end entity-group
		}

		print(sb, "</div></form>");

		print(writer, sb.toString());
	}

	private void printFieldLabel(StringBuffer sb, EntityForm model, FieldService entityField) throws IOException {
		print(sb, entityField.getName());
	}

	private void printEditBox(StringBuffer sb, EntityForm model, FieldService entityField, Object fldvalue) throws IOException {

		byte type = entityField.getType();
		switch (type) {
		case TYPE_NUMBER:
			printNum(sb, entityField, (Number) fldvalue);
			break;
		case TYPE_DATE:
			printDate(sb, entityField, (Date) fldvalue);
			break;
		case TYPE_TEXT:
			printText(sb, entityField, fldvalue);
			break;
		case TYPE_BOOL:
			printDic(sb, entityField, (Boolean) fldvalue);
			break;
		case TYPE_UPLOAD:
			break;
		case TYPE_FK:
			this.printFK(sb, entityField, fldvalue);
			break;
		case TYPE_RICH_TEXT:
			printText(sb, entityField, fldvalue);
			break;
		default:
			printStr(sb, entityField, (String) fldvalue);
		}
	}

	private boolean printDic(StringBuffer sb, FieldService fieldService, Object fieldValue) {
		KeyValue[] options = fieldService.getDicOptions();

		if (options == null || options.length == 0)
			return false;

		KeyValue selected = fieldService.getDicOption(fieldValue);
		String selectedValue = selected == null ? "" : selected.getValue();

		// Combobox
		// print(sb, "<select name=\"entity.%s\" value=\"%s\" class=\"jCocit-ui jCocit-combobox\" data-options=\"width:202\">", fieldService.getPropName(),
		// (selectedItem == null ? "" : selectedItem.getValue()));

		// Select
		print(sb, "<select name=\"entity.%s\" value=\"%s\" class=\"select\">", fieldService.getPropName(), selectedValue);
		print(sb, "<option value=\"\">--请选择--</option>");

		// options
		for (KeyValue option : options) {
			print(sb, "<option %s value=\"%s\">%s</option>", ((option.getValue().equals(selectedValue)) ? "selected" : ""), option.getValue(), option.getKey());
		}
		print(sb, "</select>");

		return true;
	}

	private void printStr(StringBuffer sb, FieldService fieldService, String fieldValue) {
		boolean isDic = this.printDic(sb, fieldService, fieldValue);
		if (!isDic) {
			String str = fieldValue == null ? "" : fieldValue.toString();
			print(sb, "<input name=\"entity.%s\" value=\"%s\" class=\"input\" />", fieldService.getPropName(), str);
		}
	}

	private void printText(StringBuffer sb, FieldService fieldService, Object fieldValue) {
		String str = fieldValue == null ? "" : fieldValue.toString();
		print(sb, "<textarea name=\"entity.%s\" class=\"textarea\">%s</textarea>", fieldService.getPropName(), str);
	}

	private void printDate(StringBuffer sb, FieldService fieldService, Date fieldValue) {
		boolean isDic = this.printDic(sb, fieldService, fieldValue);
		if (!isDic) {
			String str = ObjectUtil.format(fieldValue, fieldService.getPattern());
			String combotype = "combodate";
			String pattern = fieldService.getPattern();
			if (StringUtil.trim(pattern).length() > 10) {
				combotype = "combodatetime";
			}
			print(sb, "<input name=\"entity.%s\" value=\"%s\" class=\"jCocit-ui jCocit-%s\" data-options=\"width:202\"/>", fieldService.getPropName(), str, combotype);
		}
	}

	private void printNum(StringBuffer sb, FieldService fieldService, Number fieldValue) {
		boolean isDic = this.printDic(sb, fieldService, fieldValue);
		if (!isDic) {
			String str = ObjectUtil.format(fieldValue, fieldService.getPattern());
			print(sb, "<input name=\"entity.%s\" value=\"%s\" class=\"input jCocit-ui jCocit-numberbox\" data-options=\"precision:%s,groupSeparator:','\"/>", fieldService.getPropName(), str, fieldService.getScale());
		}
	}

	private void printFK(StringBuffer sb, FieldService fieldService, Object fieldValue) {
		String ui = fieldService.getUiTemplate();
		if ("combotree".equals(ui)) {
			printFKTree(sb, fieldService, fieldValue);
		} else {
			printFKGrid(sb, fieldService, fieldValue);
		}
	}

	private void printFKTree(StringBuffer sb, FieldService fieldService, Object fieldValue) {

		TableService entityTable = fieldService.getFkEntityTable();
		TreeWidget treeModel = Cocit.getWidgetModelFactory().getEntityTreeUI(null, entityTable);

		String id = ObjectUtil.idOrtoString(fieldValue);
		String text = fieldValue == null ? "" : fieldValue.toString();

		print(sb, "<input name=\"entity.%s.id\" class=\"jCocit-ui jCocit-combotree\" style=\"width:202px;\" data-options=\"value:'%s',text:'%s',", fieldService.getPropName(), id, text);
		print(sb, "url: '%s'", treeModel.getDataLoadUrl());
		print(sb, "\"/>");
	}

	private void printFKGrid(StringBuffer sb, FieldService fieldService, Object fieldValue) {
		TableService entityTable = fieldService.getFkEntityTable();
		GridWidget gridModel = Cocit.getWidgetModelFactory().getGridUI(null, entityTable);
		List<Column> columns = gridModel.getColumns();

		String id = ObjectUtil.idOrtoString(fieldValue);
		String text = fieldValue == null ? "" : fieldValue.toString();
		print(sb, "<input name=\"entity.%s.id\" class=\"jCocit-ui jCocit-combogrid\" data-options=\"value:'%s',text:'%s',width:202,", fieldService.getPropName(), id, text);
		print(sb, "panelWidth: 600,");
		print(sb, "panelHeight: 300,");
		print(sb, "singleSelect: false,");
		print(sb, "idField: 'id',");
		print(sb, "textField: '%s',", columns.get(0).getField());
		print(sb, "url: '%s',", gridModel.getDataLoadUrl());
		print(sb, "rownumbers: true,");
		print(sb, "pagination: true,");
		print(sb, "mode: 'remote',");
		print(sb, "pageSize: %s,", 20);
		print(sb, "columns: [[");
		print(sb, "{field:'id',title:'ID',width:80,align:'right',checkbox:true},");

		int count = 0;
		for (Column col : columns) {
			print(sb, "{field:'%s',title:'%s',width:%s,sortable:true,align:'%s'},", col.getField(), col.getTitle(), col.getWidth(), col.getAlign() == null ? "" : col.getAlign());
			count++;
			if (count == 3)
				break;
		}

		print(sb, "]],");
		print(sb, "fitColumns: true");
		print(sb, "\" />");
	}
}
