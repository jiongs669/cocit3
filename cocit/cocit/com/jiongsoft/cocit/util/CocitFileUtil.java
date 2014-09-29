package com.jiongsoft.cocit.util;

import java.io.File;
import java.util.Date;

import javax.servlet.ServletContext;

public abstract class CocitFileUtil {
	public static void renameExecutableFiles(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					renameExecutableFiles(file);
					continue;
				}
				String fileName = file.getName().toLowerCase();
				if (fileName.endsWith(".exe")//
						|| fileName.endsWith(".bat")//
						|| fileName.endsWith(".php")//
						|| fileName.endsWith(".asp")//
						|| fileName.endsWith(".aspx")//
				) {

					file.renameTo(new File(dir.getAbsolutePath() + "/" + file.getName() + ".cocit_unknown"));
					System.err.println("RenameExecutableFiles: " + file.getAbsolutePath());
				}
			}
		}
	}

	public static void clearResinLogFiles(File dir) {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					clearResinLogFiles(file);
				} else {

					String name = file.getName().toLowerCase();
					String path = file.getAbsolutePath();
					if (path.indexOf("resin-") > -1 && name.indexOf("err") > -1 && name.indexOf(".log") > -1) {
						file.delete();
						System.err.println("clearResinLogFiles: " + file.getAbsolutePath());
					}

					if (path.indexOf("\\logs\\stdout-") > -1 && name.indexOf(".log") > -1) {
						file.delete();
						System.err.println("clearResinLogFiles: " + file.getAbsolutePath());
					}

					if (path.indexOf("\\logs\\stderr-") > -1 && name.indexOf(".log") > -1) {
						file.delete();
						System.err.println("clearResinLogFiles: " + file.getAbsolutePath());
					}
				}
			}
		}

	}

	public static void renameUnknownFiles(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					renameUnknownFiles(file);
					continue;
				}

				file.renameTo(new File(dir.getAbsolutePath() + "/" + file.getName() + ".cocit_unknown"));
			}
		}
		System.err.println("renameUnknownFiles: " + dir.getAbsolutePath());
	}

	/**
	 * 清理非法程序文件
	 * 
	 * @param context
	 */
	public static void clear(ServletContext context) {
		// 清理磁盘空间
		String rootPath = context.getRealPath("/");

		// 清理根目录
		File rootDir = new File(rootPath);
		File[] rootFiles = rootDir.listFiles();
		for (File file : rootFiles) {
			String name = file.getName();
			if (name.startsWith("jCocit")//
					|| name.equals("jdemsy")//
					|| name.equals("scripts2")//
					|| name.equals("themes2")//
					|| name.equals("thumbs")//
					|| name.equals("upload")//
					|| name.equals("WEB-INF")//
					|| name.startsWith("www_")//
					|| name.endsWith("_demsy_cn")//
					|| name.endsWith("_kmetop_com")//
					|| name.endsWith("_cocit_cn")//
					|| name.endsWith("_jiongsoft_com")//
					|| name.startsWith("__unknown__")//
					|| file.isFile()//
									//
			) {
				continue;
			}

			if (file.isDirectory()) {
				renameUnknownFiles(file);
				file.renameTo(new File(rootDir + "/__unknown__" + DateUtil.format(new Date(), "yyyyMMdd-HHmmss_") + name));
				System.err.println("renameUnknownFiles: " + file.getAbsolutePath());
			} else {
				if (!name.equals("index.html")//
						&& !name.equals("QQEIM2355058881.txt")//
						&& !name.equals("zpindex2.jsp")//
				//
				) {
					file.renameTo(new File(rootDir + "/__unknown__" + DateUtil.format(new Date(), "yyyyMMdd-HHmmss_") + name));
					System.err.println("renameUnknownFiles: " + file.getAbsolutePath());
				}
			}
		}

		// patch目录
		FileUtil.deleteAll(new File(rootPath + "/upload/patch"));

		// 清理日志目录
		//FileUtil.deleteAll(new File(rootPath + "/WEB-INF/logs"));
		FileUtil.deleteAll(new File(rootPath + "/WEB-INF/tmp"));

		//
		renameExecutableFiles(rootDir.getParentFile());

		//clearResinLogFiles(new File("D:\\Cluster"));
	}
}
