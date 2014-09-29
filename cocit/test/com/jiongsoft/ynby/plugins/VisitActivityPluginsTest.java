package com.jiongsoft.ynby.plugins;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.jiongsoft.cocit.entity.ActionEvent;
import com.jiongsoft.cocit.orm.Orm;
import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.util.CocException;
import com.jiongsoft.cocit.util.HttpUtil;
import com.jiongsoft.ynby.entity.VisitActivity;
import com.jiongsoft.ynby.entity.VisitActivityRegister;
import com.jiongsoft.ynby.plugins.VisitActivityPlugins.SaveActivity;
import com.jiongsoft.ynby.plugins.VisitActivityPlugins.SaveRegister;
import com.kmetop.demsy.Demsy;

public class VisitActivityPluginsTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = NullPointerException.class)
	public void testSaveActivity1() {
		SaveActivity plugin = new SaveActivity();

		ActionEvent<VisitActivity> event = new ActionEvent();
		VisitActivity entity = new VisitActivity();

		event.setEntity(entity);

		plugin.before(event);
	}

	@Test(expected = CocException.class)
	public void testSaveActivity2() {
		SaveActivity plugin = new SaveActivity();

		ActionEvent<VisitActivity> event = new ActionEvent();
		VisitActivity entity = new VisitActivity();
		entity.setPlanDate(new Date(2013 - 1900, 10 - 1, 27));

		// 有效期非法
		entity.setExpiredTo(new Date(2013 - 1900, 10 - 1, 27));

		entity.setPlanPersonNumber(10);

		event.setEntity(entity);

		plugin.before(event);
	}

	@Test(expected = CocException.class)
	public void testSaveActivity3() {
		SaveActivity plugin = new SaveActivity();

		ActionEvent<VisitActivity> event = new ActionEvent();
		VisitActivity entity = new VisitActivity();
		entity.setPlanDate(new Date(2013 - 1900, 10 - 1, 27));

		// 有效期非法
		entity.setExpiredFrom(new Date(2013 - 1900, 10 - 1, 25));
		entity.setExpiredTo(new Date(2013 - 1900, 10 - 1, 25));

		entity.setPlanPersonNumber(10);

		event.setEntity(entity);

		plugin.before(event);
	}

	@Test(expected = CocException.class)
	public void testSaveActivity4() {
		SaveActivity plugin = new SaveActivity();

		ActionEvent<VisitActivity> event = new ActionEvent();
		VisitActivity entity = new VisitActivity();
		entity.setPlanDate(new Date(2013 - 1900, 10 - 1, 27));
		entity.setExpiredFrom(new Date(2013 - 1900, 10 - 1, 22));
		entity.setExpiredTo(new Date(2013 - 1900, 10 - 1, 25));

		// 计划人数非法
		entity.setPlanPersonNumber(0);

		event.setEntity(entity);

		plugin.before(event);
	}

	@Test
	public void testSaveActivity5() {
		SaveActivity plugin = new SaveActivity();

		ActionEvent<VisitActivity> event = new ActionEvent();
		VisitActivity entity = new VisitActivity();
		entity.setPlanDate(new Date(2013 - 1900, 10 - 1, 27));
		entity.setExpiredFrom(new Date(2013 - 1900, 10 - 1, 22));
		entity.setExpiredTo(new Date(2013 - 1900, 10 - 1, 25));
		entity.setPlanPersonNumber(50);

		event.setEntity(entity);

		plugin.before(event);
	}

	@Test
	public void testSaveRegister1() {

		final VisitActivity activity = new VisitActivity();
		final ActionEvent<VisitActivityRegister> event = new ActionEvent();
		new Expectations(HttpUtil.class, Demsy.class, event, activity) {
			@Mocked
			Orm orm;

			@Mocked
			Demsy demsy;
			{
				event.getOrm();
				returns(orm);

				orm.get(VisitActivityRegister.class, (CndExpr) any);
				returns(null);

				orm.load(VisitActivity.class, anyLong);
				returns(activity);

				activity.isExpired();
				returns(false);

				activity.getRegisterPersonNumber();
				returns(0);

				activity.getPlanPersonNumber();
				returns(60);

				Demsy.me();
				returns(demsy);

				demsy.request();
				returns(null);

				HttpUtil.checkSmsVerifyCode((HttpServletRequest) any, anyString, anyString, anyString);

				orm.save(any);
			}
		};

		SaveRegister plugin = new SaveRegister();
		VisitActivityRegister entity = new VisitActivityRegister();
		event.setEntity(entity);

		entity.setName("吉永山");
		entity.setCode("53212819791206631X");
		entity.setTel("15911731833");
		entity.setActivity(activity);
		entity.setSex((byte) 0);
		StringBuffer members = new StringBuffer();
		members.append("[" + //
				" {\"orderby\":0,\"id\":0,\"name\":\"爸爸\",\"age\":55,\"teamMemberRole\":\"父母\",\"sex\":0,\"tel\":\"\",\"qq\":\"\",\"email\":\"\",\"unit\":\"\",\"carCode\":\"\"}" + //
				",{\"orderby\":1,\"id\":0,\"name\":\"张菊\",\"age\":34,\"teamMemberRole\":\"配偶\",\"sex\":1,\"tel\":\"\",\"qq\":\"\",\"email\":\"\",\"unit\":\"\",\"carCode\":\"\"}" + //
				",{\"orderby\":2,\"id\":0,\"name\":\"张华\",\"age\":28,\"teamMemberRole\":\"亲戚\",\"sex\":0,\"tel\":\"\",\"qq\":\"\",\"email\":\"\",\"unit\":\"\",\"carCode\":\"\"}" + //
				"]");
		entity.setTeamMembers(members.toString());

		plugin.before(event);

		assertEquals(entity.getPersonNumber(), (Integer) 4);
		assertEquals(entity.getStatus(), 0);
		assertEquals(activity.getRegisterPersonNumber(), 4);
	}

	public static void main(String[] args) {
		JUnitCore.runClasses(new Class[] { TestAll.class });
	}
}