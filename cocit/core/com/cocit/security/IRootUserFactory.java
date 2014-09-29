package com.cocit.security;

import java.io.IOException;

import com.cocit.api.security.IUser;

public interface IRootUserFactory {

	IUser getUser(String username);

	void saveUser(IUser login) throws IOException;
}
