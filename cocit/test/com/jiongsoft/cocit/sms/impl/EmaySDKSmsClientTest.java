package com.jiongsoft.cocit.sms.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.service.ConfigManager;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.sms.impl.EmaySDKSmsClient;
import com.jiongsoft.cocit.util.DateUtil;

public class EmaySDKSmsClientTest {
	@Test
	public void testEmaySmsClient_1() throws Exception {

		EmaySDKSmsClient result = new EmaySDKSmsClient();

		assertNotNull(result);
	}

	@Test
	public void testQueryBalance_1() throws Exception {
		EmaySDKSmsClient fixture = new EmaySDKSmsClient();

		Integer result = fixture.getBalance();

		assertNotNull(result);
	}

	@Test
	public void testReceive_1() throws Exception {
		EmaySDKSmsClient fixture = new EmaySDKSmsClient();

		List<String[]> result = fixture.receive();

		assertNotNull(result);
	}

	@Test
	public void testSend_stress() throws Exception {
		EmaySDKSmsClient fixture = new EmaySDKSmsClient();
		String mobiles = "15911731833";
		for (int i = 1; i <= 10; i++) {
			String extCode = "";
			String time = "";
			String rrid = "";

			String result = fixture.send(mobiles, "压力测试 " + i, extCode, time, rrid);

			assertNotNull(result);
		}
	}

	@Test
	public void testSend_contentLength() throws Exception {
		EmaySDKSmsClient fixture = new EmaySDKSmsClient();
		String mobiles = "15911731833";
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < 500; i++) {
			content.append("烱");
		}
		String extCode = "";
		String time = "";
		String rrid = "";

		String result = fixture.send(mobiles, content.toString(), extCode, time, rrid);

		assertNotNull(result);
	}

	@Before
	public void setUp() throws Exception {
		new Expectations(Cocit.class) {
			@Mocked
			ActionContext softContext;

			@Mocked
			SoftService soft;
			{
				Cocit.getActionContext();
				result = softContext;
				softContext.getSoftService();
				result = soft;

				soft.getConfig(ConfigManager.SMS_PROXY_HOST, "");
				result = "192.168.128.3";
				soft.getConfig(ConfigManager.SMS_PROXY_PORT, 80);
				result = 80;

				/*
				 * 员工网帐号
				 */
				soft.getConfig(ConfigManager.SMS_UID, "");
				result = "3SDK-KYJ-0130-KJXQT";
				soft.getConfig(ConfigManager.SMS_PWD, "");
				result = "257330";
				soft.getConfig("sms.key", "");
				result = "147088";

				/*
				 * 茶缘帐号
				 */
				// soft.getConfig(ConfigManager.SMS_UID, "");
				// result = "3SDK-KYJ-0130-KJXQL";
				// soft.getConfig(ConfigManager.SMS_PWD, "");
				// result = "356860";
				// soft.getConfig("sms.key", "");
				// result = "147080";
			}
		};
	}

	@After
	public void tearDown() throws Exception {
	}
}