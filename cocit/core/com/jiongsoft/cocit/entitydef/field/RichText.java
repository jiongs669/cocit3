package com.jiongsoft.cocit.entitydef.field;

import com.jiongsoft.cocit.lang.JSON;
import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.orm.annotation.CocField;

@CocField(columnDefinition = "text")
public class RichText implements IExtField {
	private String text;

	public RichText() {
		this("");
	}

	public RichText(String t) {
		this.text = t;
	}

	public String toString() {
		if (Str.isEmpty(text)) {
			return "";
		}
		return text;
	}

	public String toJson() {
		return JSON.toJson(text);
	}
}
