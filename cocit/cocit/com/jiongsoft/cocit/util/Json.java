package com.jiongsoft.cocit.util;

import java.io.Writer;
import java.util.List;

import com.jiongsoft.cocit.util.json.BaseJson;

/**
 * Json 工具类
 * 
 * @author yongshan.ji
 * 
 */
public abstract class Json {
	private static BaseJson json = BaseJson.DEFAULT;

	public static String toJson(Object obj) {
		return json.toJson(obj);
	}

	public static void toJson(Writer writer, Object obj) {
		json.toJson(writer, obj);
	}

	public static List fromJson(String str) {
		return json.fromJson(str);
	}

	public static <T> T fromJson(Class<T> type, String str) {
		return json.fromJson(type, str);
	}
}
