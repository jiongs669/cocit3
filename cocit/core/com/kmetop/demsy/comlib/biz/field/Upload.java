package com.kmetop.demsy.comlib.biz.field;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.kmetop.demsy.lang.JSON;

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
