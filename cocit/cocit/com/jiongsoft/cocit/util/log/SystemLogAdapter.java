package com.jiongsoft.cocit.util.log;


public class SystemLogAdapter implements LogAdapter {

	public ILog getLogger(String className) {
		return SystemLog.me();
	}

	public boolean canWork() {
		return true;
	}

}
