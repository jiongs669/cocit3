package com.cocit.api.entitydef.field;

import com.cocit.lang.JSON;
import com.jiongsoft.cocit.entity.annotation.CocField;

@CocField(precision = 255)
public class Upload implements IExtField {

	private String path;

	public Upload() {
		this("");
	}

	public Upload(String str) {
		if (str == null)
			this.path = "";
		else
			this.path = str;
	}

	public String getPath() {
		return path;
	}

	public String toString() {
		return path;
	}

	public String toJson() {
		return JSON.toJson(path);
	}

}
