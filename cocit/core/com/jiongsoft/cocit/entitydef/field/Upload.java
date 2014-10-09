package com.jiongsoft.cocit.entitydef.field;

import com.jiongsoft.cocit.lang.JSON;
import com.kmjsoft.cocit.orm.annotation.CocColumn;

@CocColumn(precision = 255)
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
