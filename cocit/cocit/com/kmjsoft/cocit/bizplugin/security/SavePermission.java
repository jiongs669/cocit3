package com.kmjsoft.cocit.bizplugin.security;

import com.jiongsoft.cocit.Demsy;
import com.kmjsoft.cocit.entityengine.bizplugin.ActionEvent;
import com.kmjsoft.cocit.entityengine.bizplugin.BasePlugin;

public class SavePermission extends BasePlugin {

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
