package com.jiongsoft.cocit.util.compiler;

import java.io.File;

public class Main {

	private static String java_file_path = "E:/cocit/web/WEB-INF/tmp/BIZSRC/com/kmetop/demsy/comlib/impl/sft/dynamic/system/";

	private static String java_basic_path = "E:/cocit/web/WEB-INF/tmp/BIZSRC";

	private static String ecode = "utf8";

	public void execute() throws Exception {
		DCompile dc = new DCompile();
		File dir = new File(java_file_path);
		File[] files = dir.listFiles();
		for (File file : files) {
			dc.initialize(java_file_path + file.getName(), java_basic_path, ecode);
		}
		dc.compile();
		
		// Class clazz = dc.compile();
		// Object obj = clazz.newInstance();
		// Method method = clazz.getMethod("getName", new Class[] { String.class });
		// String name = (String) method.invoke(obj, "dbdxj");
		// System.out.println(name);
	}

	public static void main(String[] args) {
		Main m = new Main();
		try {
			m.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}