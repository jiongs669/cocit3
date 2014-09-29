package com.kmetop.demsy.plugins.security;

import com.jiongsoft.cocit.entity.ActionEvent;
import com.kmetop.demsy.comlib.entity.base.BaseUser;

public class SaveStaffUser extends SaveUser {

	@Override
	public void before(ActionEvent event) {
		super.before(event);

		BaseUser user = (BaseUser) event.getEntity();
		user.setDisabled(true);
	}
}
