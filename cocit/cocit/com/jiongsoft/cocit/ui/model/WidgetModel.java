package com.jiongsoft.cocit.ui.model;

import static com.jiongsoft.cocit.Cocit.getWidgetRenderFactory;

import java.io.Writer;
import java.util.Properties;

import com.jiongsoft.cocit.ui.UIModel;
import com.jiongsoft.cocit.util.CocException;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.StringUtil;

/**
 * 基本界面模型：继承该类的所有模型都将以HTML数据格式输出特定的界面模型。
 * 
 * @author yongshan.ji
 * 
 */
public abstract class WidgetModel implements UIModel {

	private String id;

	private String themeName;

	// Grid属性设置
	private Properties extProps;

	protected CocException exception;

	public WidgetModel() {
		extProps = new Properties();
	}

	@Override
	public void render(Writer out) throws Throwable {
		getWidgetRenderFactory().getRender(getClass()).render(out, this);
	}

	@Override
	public String getContentType() {
		return CONTENT_TYPE_HTML;
	}

	@Override
	public boolean isCachable() {
		return false;
	}

	/**
	 * 设置扩展属性
	 * 
	 * @param propName
	 * @param propValue
	 */
	public void set(String propName, String propValue) {
		extProps.put(propName, propValue);
	}

	/**
	 * 获取扩展属性
	 * 
	 * @param propName
	 * @return
	 */
	public <T> T get(String propName, T defaultReturn) {
		String value = extProps.getProperty(propName);

		if (value == null)
			return defaultReturn;
		if (defaultReturn == null)
			return (T) value;

		Class valueType = defaultReturn.getClass();

		try {
			return (T) StringUtil.castTo(value, valueType);
		} catch (Throwable e) {
			Log.error("WidgetModel.get: 出错！ {propName:%s, defaultReturn:%s, valueType:%s}", propName, defaultReturn, valueType.getName(), e);
		}

		return defaultReturn;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CocException getException() {
		return exception;
	}

	public void setException(CocException exception) {
		this.exception = exception;
	}

}
