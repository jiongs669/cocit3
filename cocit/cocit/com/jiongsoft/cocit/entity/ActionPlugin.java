package com.jiongsoft.cocit.entity;

/**
 * 操作插件：在执行业务表相关操作时自动回调插件中的方法处理业务逻辑。
 * 
 * @author jiongsoft
 * 
 * @param <T>
 */
public interface ActionPlugin<T> {
	public void before(ActionEvent<T> event);

	public void after(ActionEvent<T> event);

	public void load(ActionEvent<T> event);

	public void loaded(ActionEvent<T> event);
}
