package com.kmjsoft.cocit.bizplugin.security;

import com.kmjsoft.cocit.entity.impl.security.BaseUser;
import com.kmjsoft.cocit.entityengine.bizplugin.ActionEvent;

public class SaveStaffUser extends SaveUser {

	@Override
	public void before(ActionEvent event) {
		super.before(event);

		BaseUser user = (BaseUser) event.getEntity();
		user.setDisabled(true);
	}
}
