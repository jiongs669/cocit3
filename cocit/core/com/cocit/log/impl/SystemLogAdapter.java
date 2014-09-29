package com.cocit.log.impl;

import com.cocit.log.Log;
import com.cocit.log.LogAdapter;

public class SystemLogAdapter implements LogAdapter {

	public Log getLogger(String className) {
		return SystemLog.me();
	}

	public boolean canWork() {
		return true;
	}

}
