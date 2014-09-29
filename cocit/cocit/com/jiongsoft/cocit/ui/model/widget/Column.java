package com.jiongsoft.cocit.ui.model.widget;

import java.util.Properties;

import com.jiongsoft.cocit.service.FieldService;

public class Column {

	private String field;

	private String title;

	private String align;

	private int width;

	private String pattern;

	private FieldService entityField;

	private Properties props;

	/**
	 * 创建一个GridColumn对象
	 * 
	 * @param field
	 *            字段：用于标识该列数据来自哪个字段？
	 * @param title
	 *            标题：用于显示表头标题
	 * @param align
	 *            数据对齐方式：可选值“right/center/left”
	 * @param width
	 *            列宽度：单位px，-1 表示自动宽度。
	 */
	public Column(String field, String title) {
		this.props = new Properties();

		this.field = field;
		this.title = title;
	}

	public void addProp(String propName, String propValue) {
		props.put(propName, propValue);
	}

	public String getProp(String propName) {
		return props.getProperty(propName);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlign() {
		return align;
	}

	public Column setAlign(String assign) {
		this.align = assign;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public Column setWidth(int width) {
		this.width = width;
		return this;
	}

	public Properties getProps() {
		return props;
	}

	public Column setProps(Properties props) {
		this.props = props;
		return this;
	}

	public String getPattern() {
		return pattern;
	}

	public Column setPattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	public FieldService getEntityField() {
		return entityField;
	}

	public Column setEntityField(FieldService entityField) {
		this.entityField = entityField;
		return this;
	}
}
