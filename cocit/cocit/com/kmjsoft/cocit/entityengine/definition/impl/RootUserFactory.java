package com.kmjsoft.cocit.entityengine.definition.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jiongsoft.cocit.Demsy;
import com.jiongsoft.cocit.config.IConfig;
import com.jiongsoft.cocit.config.impl.BaseConfig;
import com.jiongsoft.cocit.lang.Str;
import com.jiongsoft.cocit.security.IRootUserFactory;
import com.kmjsoft.cocit.entity.impl.security.BaseUser;
import com.kmjsoft.cocit.entity.security.IUser;

public class RootUserFactory extends BaseConfig implements IRootUserFactory {
	public RootUserFactory() {
		super(Demsy.appconfig.getUserConfig());
	}

	private Map<String, BaseUser> userMap = new HashMap();

	public IUser getUser(final String username) {
		final String password = this.get(username);
		if (password == null) {
			return null;
		}
		BaseUser user = userMap.get(username);
		if (user == null) {
			user = new BaseUser() {
				public boolean isBuildin() {
					return true;
				}
			};
			user.setUsername(username);
			user.setPassword(password);

			userMap.put(username, user);
		}
		return user;
	}

	public void saveUser(IUser login) throws IOException {
		if (login != null) {
			IUser user = this.getUser(login.getUsername());
			if (user != null) {
				String pwd = login.getPassword();
				if (!Str.isEmpty(pwd)) {
					this.put(login.getUsername(), pwd);
					this.save();
				}
			}
		}
	}

	@Override
	public IConfig copy() {
		return null;
	}
}
