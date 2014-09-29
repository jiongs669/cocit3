package com.jiongsoft.cocit;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BeanFactoryTest.class,
	com.jiongsoft.cocit.service.TestAll.class,
	com.jiongsoft.cocit.sms.TestAll.class,
	com.jiongsoft.cocit.util.TestAll.class,
	com.jiongsoft.cocit.utils.TestAll.class,
})
public class TestAll {

	public static void main(String[] args) {
		JUnitCore.runClasses(new Class[] { TestAll.class });
	}
}
