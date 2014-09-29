package com.jiongsoft.cocit.sms.impl;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	EmaySDKSmsClientTest.class,
	ZucpSmsClientTest.class,
	EmayHttpSmsClientTest.class,
	ZrSmsClientTest.class,
})
public class TestAll {
}
