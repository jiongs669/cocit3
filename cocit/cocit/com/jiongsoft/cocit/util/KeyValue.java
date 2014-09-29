package com.jiongsoft.cocit.util;

import java.util.Properties;

/**
 * Key Value 配对值，如下拉框选择项中，key即为Label。
 * 
 * @author jiongs753
 * 
 * @param <T>
 */
public class KeyValue {
	private String key;

	private String value;

	private Properties extProps;

	public static KeyValue make(String key, String value) {
		return new KeyValue(key, value);
	}

	private KeyValue(String key, String value) {
		this.key = key;
		this.value = value;

		extProps = new Properties();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public <T> T get(String propName, T defaultReturn) {
		String value = extProps.getProperty(propName);

		if (value == null)
			return defaultReturn;
		if (defaultReturn == null)
			return null;

		Class valueType = defaultReturn.getClass();

		try {
			return (T) StringUtil.castTo(value, valueType);
		} catch (Throwable e) {
			Log.warn("", e);
		}

		return defaultReturn;
	}

	public KeyValue set(String propName, String value) {
		extProps.put(propName, value);

		return this;
	}
}
