package com.kmjsoft.cocit.entityengine.bizplugin;


public abstract class BasePlugin<T> implements ActionPlugin<T> {

	@Override
	public void before(ActionEvent<T> event) {
	}

	@Override
	public void after(ActionEvent<T> event) {
	}

	@Override
	public void load(ActionEvent<T> event) {
	}

	@Override
	public void loaded(ActionEvent<T> event) {
	}

}
