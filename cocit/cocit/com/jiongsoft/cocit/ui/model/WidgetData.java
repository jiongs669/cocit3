package com.jiongsoft.cocit.ui.model;

import static com.jiongsoft.cocit.Cocit.getWidgetRenderFactory;

import java.io.Writer;

import com.jiongsoft.cocit.ui.UIModel;

/**
 * 数据模型：用于表示通过AJAX访问的数据模型。由两部分组成：1.模型，2.数据
 * <p>
 * 继承该类的所有模型都将输出JSON或XML格式的数据。
 * 
 * @author yongshan.ji
 * 
 * @param <T>界面模型泛型
 * @param <D>数据泛型
 */
public abstract class WidgetData<TModel extends WidgetModel, TData> implements UIModel {

	/**
	 * HTML模型：数据的输出依赖于该HTML模型
	 */
	protected TModel model;

	/**
	 * 业务数据：待输出的业务数据对象
	 */
	protected TData data;

	protected Throwable exception;

	@Override
	public void render(Writer out) throws Throwable {
		getWidgetRenderFactory().getRender(getClass()).render(out, this);
	}

	@Override
	public String getContentType() {
		return CONTENT_TYPE_JSON;
	}

	@Override
	public boolean isCachable() {
		return false;
	}

	public TModel getModel() {
		return model;
	}

	public void setModel(TModel model) {
		this.model = model;
	}

	public TData getData() {
		return data;
	}

	public void setData(TData data) {
		this.data = data;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

}
