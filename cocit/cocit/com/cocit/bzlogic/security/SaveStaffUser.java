package com.cocit.bzlogic.security;

import com.cocit.entity.security.BaseUser;
import com.jiongsoft.cocit.entity.ActionEvent;

public class SaveStaffUser extends SaveUser {

	@Override
	public void before(ActionEvent event) {
		super.before(event);

		BaseUser user = (BaseUser) event.getEntity();
		user.setDisabled(true);
	}
}
