package com.jiongsoft.cocit.modules.webmail;

import com.jiongsoft.cocit.modules.webmail.business.DefaultMailboxConnection;
import com.jiongsoft.cocit.modules.webmail.business.DemsyTrustManager;
import com.jiongsoft.cocit.modules.webmail.model.LoginModel;

public class MailboxConnectionFactory {
	private static MailboxConnectionFactory mailboxConnectionFactory = new MailboxConnectionFactory();

	private MailboxConnectionFactory() {
	}

	public static MailboxConnectionFactory getInstance() {
		return (mailboxConnectionFactory);
	}

	public MailboxConnection createMailboxConnection(LoginModel loginData,
			DemsyTrustManager trustManager) throws InstantiationException {
		return (new DefaultMailboxConnection(loginData, trustManager));
	}

}
