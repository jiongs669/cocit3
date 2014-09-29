package com.jiongsoft.cocit.ui.model.widget;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.nutz.mvc.view.JspView;

import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.service.FieldService;
import com.jiongsoft.cocit.ui.model.WidgetModel;
import com.jiongsoft.cocit.util.StringUtil;

/**
 * 实体表单模型
 * 
 * @author yongshan.ji
 * 
 */
public class EntityForm extends WidgetModel {

	private Object data;

	private List<FormField> groupFields;

	/**
	 * 使用JSP 模版产生实体表单
	 */
	private String jsp;

	/**
	 * 环境路径
	 */
	private String contextPath;

	/**
	 * 表单所需环境变量
	 */
	private Map<String, Object> context;

	public EntityForm() {
		groupFields = new ArrayList();
		context = new HashMap();
		contextPath = Cocit.getContextPath();
	}

	/**
	 * 获取环境变脸
	 * 
	 * @param key
	 * @return
	 */
	public <T> T getVar(String key) {
		return (T) context.get(key);
	}

	/**
	 * 设置环境变量
	 * 
	 * @param key
	 * @param value
	 */
	public void setVar(String key, Object value) {
		context.put(key, value);
	}

	@Override
	public void render(Writer out) throws Throwable {
		if (StringUtil.isNil(jsp)) {
			super.render(out);

			return;
		}

		// 用JSP输入表单模型
		ActionContext ctx = Cocit.getActionContext();
		new JspView(jsp).render(ctx.getRequest(), ctx.getResponse(), this);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<FormField> getGroupFields() {
		return groupFields;
	}

	public void setGroupFields(List<FormField> fields) {
		this.groupFields = fields;
	}

	public static class FormField {
		// 字段类型
		private byte type;

		private String field;

		private String title;

		private String pattern;

		private String mode;

		// 字段关联的对象
		private FieldService entityField;

		private Properties props;

		private List<FormField> children;

		private List<FormField> visibleChildren;

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
		public FormField(String title) {
			this.props = new Properties();

			this.children = new ArrayList();
			visibleChildren = new ArrayList();
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

		public Properties getProps() {
			return props;
		}

		public void setProps(Properties props) {
			this.props = props;
		}

		public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		public String getMode() {
			return mode;
		}

		public void setMode(String mode) {
			this.mode = mode;
		}

		public byte getType() {
			return type;
		}

		public void setType(byte type) {
			this.type = type;
		}

		public FieldService getEntityField() {
			return entityField;
		}

		public void setEntityField(FieldService entityField) {
			this.entityField = entityField;
		}

		public List<FormField> getChildren() {
			return children;
		}

		public void setChildren(List<FormField> children) {
			this.children = children;
		}

		public void addChild(FormField field) {
			this.children.add(field);
		}

		public void addVisibleChild(FormField field) {
			this.visibleChildren.add(field);
		}

		public List<FormField> getVisibleChildren() {
			return visibleChildren;
		}
	}

	public void addGroupField(FormField groupField) {
		this.groupFields.add(groupField);
	}

	public String getJsp() {
		return jsp;
	}

	public void setJsp(String jsp) {
		this.jsp = jsp;
	}

	public String getContextPath() {
		return contextPath;
	}
}
