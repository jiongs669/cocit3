package com.jiongsoft.cocit.security;

import java.io.IOException;

import com.kmjsoft.cocit.entity.security.IUser;

public interface IRootUserFactory {

	IUser getUser(String username);

	void saveUser(IUser login) throws IOException;
}
