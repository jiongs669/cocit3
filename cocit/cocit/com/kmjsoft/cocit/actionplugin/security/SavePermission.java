package com.kmjsoft.cocit.actionplugin.security;

import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.actionplugin.ActionEvent;
import com.kmjsoft.cocit.entity.actionplugin.ActionPlugin;

public class SavePermission extends ActionPlugin {

	@Override
	public void before(ActionEvent event) {
	}

	@Override
	public void after(ActionEvent event) {
		Demsy.security.clearPermissions();
	}

	@Override
	public void loaded(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
