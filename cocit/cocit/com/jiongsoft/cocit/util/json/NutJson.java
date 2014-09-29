package com.jiongsoft.cocit.util.json;

import java.io.Writer;

import org.nutz.json.Json;

public class NutJson extends BaseJson {

	@Override
	public String toJson(Object obj) {
		return Json.toJson(obj);
	}

	@Override
	public void toJson(Writer writer, Object obj) {
		Json.toJson(writer, obj);
	}

	@Override
	public <T> T fromJson(String json) {
		return (T) Json.fromJson(json);
	}

	@Override
	public <T> T fromJson(Class<T> type, String json) {
		return Json.fromJson(type, json);
	}

}
