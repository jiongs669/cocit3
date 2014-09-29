package com.jiongsoft.cocit.util.log;

public abstract class Logs {

	private static LogAdapter adapter;

	static {
		LogbackLogAdapter logback = new LogbackLogAdapter();
		if (logback.canWork()) {
			adapter = logback;
		} else {
			Log4jLogAdapter log4j = new Log4jLogAdapter();
			if (log4j.canWork()) {
				adapter = log4j;
			} else {
				JdkLogAdapter jdklog = new JdkLogAdapter();
				if (jdklog.canWork()) {
					adapter = jdklog;
				} else {
					SystemLogAdapter systemlog = new SystemLogAdapter();
					adapter = systemlog;
				}
			}
		}
	}

	public static ILog get() {
		return adapter.getLogger(new Throwable().getStackTrace()[1].getClassName());
	}

	public static ILog getLog(Class<?> clazz) {
		return getLog(clazz.getName());
	}

	public static ILog getLog(String className) {
		return adapter.getLogger(className);
	}

	// public static void error(String msg) {
	//
	// }

}
