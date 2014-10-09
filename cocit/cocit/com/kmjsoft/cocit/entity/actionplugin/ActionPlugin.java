package com.kmjsoft.cocit.entity.actionplugin;


public abstract class ActionPlugin<T> implements IActionPlugin<T> {

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
