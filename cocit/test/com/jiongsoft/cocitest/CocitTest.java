package com.jiongsoft.cocitest;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Expectations;
import mockit.Mocked;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.BeanFactory;
import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.service.ConfigManager;
import com.jiongsoft.cocit.sms.impl.ZrSmsClient;
import com.jiongsoft.cocit.util.Json;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.impl.base.lib.DemsySoft;
import com.kmetop.demsy.config.SoftConfigManager;
import com.kmetop.demsy.engine.BizEngine;
import com.kmetop.demsy.engine.ModuleEngine;

public class CocitTest {
	MockServletContext context;

	@BeforeMethod
	public void setUp() {
		context = new MockServletContext();
		context.setContextPath("/");

		Cocit.init(context);
	}

	@Test
	public void destroy() {
		Cocit.destroy(context);

		assertNull(Cocit.getContextPath());

		try {
			Cocit.getBean("");
			assertTrue(false);
		} catch (NullPointerException e) {
			assertTrue(true);
		}

		assertNull(Cocit.getActionContext());
	}

	@Test
	public void getContextPath() {
		MockServletContext context = new MockServletContext();

		context.setContextPath(null);
		Cocit.init(context);
		assertEquals("", Cocit.getContextPath());

		context.setContextPath("/");
		Cocit.init(context);
		assertEquals("", Cocit.getContextPath());

		context.setContextPath("");
		Cocit.init(context);
		assertEquals("", Cocit.getContextPath());

		context.setContextPath("/test");
		Cocit.init(context);
		assertEquals("/test", Cocit.getContextPath());

		context.setContextPath("/test/");
		Cocit.init(context);
		assertEquals("/test", Cocit.getContextPath());

		context.setContextPath("test");
		Cocit.init(context);
		assertEquals("/test", Cocit.getContextPath());
	}

	@Test
	public void getBean() {
		Json json = Cocit.getBean(Json.class);
		assertNull(json);

		json = Cocit.getBean("json");
		assertNull(json);

		BeanFactory beanFactory = Cocit.getBean(BeanFactory.class);
		assertNotNull(beanFactory);

		beanFactory = Cocit.getBean("beanFactory");
		assertNotNull(beanFactory);

		assertNotNull(Cocit.getServiceFactory());
		assertNotNull(Cocit.getWidgetModelFactory());
		assertNotNull(Cocit.getWidgetRenderFactory());
		assertNull(Cocit.getOrmFactory());

	}

	@Test
	public void makeAndGetHttpContext() {
		final HttpServletRequest req = new MockHttpServletRequest();
		final HttpServletResponse res = new MockHttpServletResponse();
		new Expectations(Demsy.class, SoftConfigManager.class) {
			@Mocked
			DemsySoft mockDemsySoft;

			@Mocked
			BizEngine bizEngine;

			@Mocked
			ModuleEngine moduleEngine;

			@Mocked
			SoftConfigManager mockDemsyConfig;
			{
				Demsy.bizEngine = bizEngine;
				Demsy.moduleEngine = moduleEngine;
				moduleEngine.getSoft(anyString);
				result = mockDemsySoft;
				SoftConfigManager.me();
				result = mockDemsyConfig;
				mockDemsyConfig.get("sms.type", "");
				result = "zr";
				mockDemsyConfig.get(ConfigManager.SMS_PROXY_HOST, "");
				result = "192.168.128.3";
				mockDemsyConfig.get(ConfigManager.SMS_PROXY_PORT, "");
				result = "80";
				mockDemsyConfig.get(ConfigManager.SMS_URL, "");
				result = "http://oa.zrsms.com";
				mockDemsyConfig.get(ConfigManager.SMS_UID, "");
				result = "zlsandi";
				mockDemsyConfig.get(ConfigManager.SMS_PWD, "");
				result = "zlsandi";
			}
		};

		ActionContext ctx = Cocit.initActionContext(req, res);
		assertNotNull(ctx);

		ctx = Cocit.getActionContext();
		assertNotNull(ctx);

		assertNotNull(ctx.getSoftService());

		assertNotNull(ctx.getSoftService().getSmsClient());

		assertNotNull(ctx.getSoftService().getSmsClient() instanceof ZrSmsClient);
	}
}
