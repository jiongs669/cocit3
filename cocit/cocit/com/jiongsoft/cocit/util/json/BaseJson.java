package com.jiongsoft.cocit.util.json;

import java.io.Writer;

/**
 * Json 工具类
 * 
 * @author yongshan.ji
 * 
 */
public abstract class BaseJson {
	public static BaseJson DEFAULT = new NutJson();

	public static void setDefault(BaseJson proxy) {
		if (proxy != null)
			DEFAULT = proxy;
	}

	public abstract String toJson(Object obj);

	public abstract void toJson(Writer writer, Object obj);

	public abstract <T> T fromJson(String json);

	public abstract <T> T fromJson(Class<T> cls, String json);
}
