package com.cocit.api.entitydef.field;

import com.cocit.lang.JSON;
import com.cocit.lang.Str;
import com.jiongsoft.cocit.entity.annotation.CocField;

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
