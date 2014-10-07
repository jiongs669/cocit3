package com.jiongsoft.cocit.log.impl;

import com.jiongsoft.cocit.log.Log;
import com.jiongsoft.cocit.log.LogAdapter;

public class SystemLogAdapter implements LogAdapter {

	public Log getLogger(String className) {
		return SystemLog.me();
	}

	public boolean canWork() {
		return true;
	}

}
