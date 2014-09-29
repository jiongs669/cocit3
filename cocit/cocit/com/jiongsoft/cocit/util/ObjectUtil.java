package com.jiongsoft.cocit.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

public abstract class ObjectUtil {

	/**
	 * 判断指定的集合是否为空。参数为 null 或 size() 为0都将范围 true。
	 * 
	 * @param coll
	 * @return
	 */
	public static boolean isNil(Collection coll) {
		return coll == null || coll.size() == 0;
	}

	/**
	 * 获取对象的id字段值，或者直接获取对象的toString值。
	 * 
	 * @param obj
	 * @return
	 */
	public static String idOrtoString(Object obj) {
		if (obj == null)
			return "";

		String ret = "";
		Mirror me = Mirror.me(obj.getClass());
		try {
			if (me.getField("id") != null) {
				Object id = getValue(obj, "id");
				if (id != null)
					ret = id.toString();
			} else {
				ret = obj.toString();
			}
		} catch (NoSuchFieldException e) {
			// Log.warn("", e);
			ret = obj.toString();
		}

		return ret;
	}

	public static String toString(Object obj) {
		if (obj == null)
			return "";

		return obj.toString();
	}

	public static String format(Object value, String pattern) {
		if (pattern != null && pattern.charAt(0) == '*') {
			pattern = pattern.substring(1);
		}
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Date) {
			if (!StringUtil.isNil(pattern)) {
				try {
					return DateUtil.format((Date) value, pattern);
				} catch (Throwable e) {
					Log.warn("", e);
					return DateUtil.formatDateTime((Date) value);
				}
			} else {
				return DateUtil.format((Date) value);
			}
		} else if (value instanceof Number && !StringUtil.isNil(pattern)) {
			// if (Strings.isEmpty(pattern)) {
			// if (value instanceof Long || value instanceof Integer ||
			// value instanceof Short)
			// pattern = "#,##0";
			// else
			// pattern = "#,##0.0#";
			// }
			return new DecimalFormat(pattern).format(new BigDecimal(value.toString()));
		}

		if (value instanceof List) {
			List list = (List) value;
			if (list.size() == 0) {
				return "";
			} else {
				StringBuffer sb = new StringBuffer();
				for (Object obj : list) {
					sb.append(',').append(obj);
				}
				return sb.substring(1);
			}
		}

		return value == null ? "" : value.toString();
	}

	public static <T> T getValue(Object obj, final String path) {
		if (obj == null) {
			return null;
		}

		if (StringUtil.isNil(path))
			return null;

		int dot = path.indexOf(".");
		if (dot > -1) {
			Object subObj = null;
			try {
				if (obj instanceof Map) {
					subObj = ((Map) obj).get(path.substring(0, dot));
				} else {
					subObj = Mirror.me(obj.getClass()).getValue(obj, path.substring(0, dot));
				}
			} catch (Throwable e) {
				Log.warn("", e);
			}
			if (subObj == null) {
				return null;
			}

			return getValue(subObj, path.substring(dot + 1));
		} else {
			try {
				return (T) Mirror.me(obj.getClass()).getValue(obj, path);
			} catch (Throwable e) {
				Log.warn("", e);
				return null;
			}
		}
	}

	public static void setValue(Object obj, String path, Object value) {
		if (obj == null) {
			return;
		}
		Mirror me = Mirror.me(obj);
		try {
			int dot = path.indexOf(".");
			if (dot < 0) {
				me.setValue(obj, path, value);
			} else {
				String prop = path.substring(0, dot);
				Class fldtype = me.getField(prop).getType();
				Mirror fldme = Mirror.me(fldtype);
				Object fldval = fldme.born();
				setValue(fldval, path.substring(dot + 1), value);
				setValue(obj, prop, fldval);
			}
		} catch (NoSuchFieldException e) {
			Log.warn("", e);
		}
	}

}
