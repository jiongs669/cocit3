// $codepro.audit.disable unnecessaryCast
package com.jiongsoft.cocit.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public abstract class StringUtil {

	/**
	 * 验证护照
	 * <p>
	 * 因私普通护照号码格式有:14/15+7位数,G+8位数；
	 * <p>
	 * 因公普通的是:P.+7位数；
	 * <p>
	 * 公务的是：S.+7位数 或者 S+8位数；
	 * <p>
	 * 以D开头的是外交护照.D=diplomatic
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isPassport(String id) {
		String pattern = "(P\\d{7})|(G\\d{8})";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(id);

		return m.matches();
	}

	/**
	 * 检查其他身份ID
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isOtherNID(String id) {
		if (isNil(id))
			return false;

		int len = id.trim().length();

		// 护照号码
		if (len >= 7 && len <= 10) {
			// char ch = id.trim().toUpperCase().charAt(0);
			// if (ch >= 'A' && ch <= 'Z') {
			// if (len <= 8)
			// return true;
			// } else {
			// if (len == 10)
			// return true;
			// }

			return true;
		}

		return false;
	}

	/**
	 * 检查身份证号码
	 * 
	 * @param id_number
	 * @return
	 */
	public static boolean isNID(String id) {
		if (isNil(id))
			return false;

		String pattern = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(id);

		int[] weight = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char[] checkDigit = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };

		int sum = 0;
		if (m.matches()) {
			for (int i = 0; i < 17; i++) {
				int b = Integer.parseInt(id.substring(i, i + 1));
				int a = weight[i];
				sum = a * b + sum;
			}
			int mod = sum % 11;

			String s = "" + checkDigit[mod];
			String last = id.substring(id.length() - 1);

			return s.equalsIgnoreCase(last);
		}

		return false;
	}

	/**
	 * 检查手机号码
	 * 
	 * @param tel
	 * @return
	 */
	public static boolean isMobile(String tel) {
		Pattern pattern = Pattern.compile("\\d{11}$");// Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher matcher = pattern.matcher(tel);

		return matcher.matches();
	}

	public static String escapeHTML(String input) {
		if (input == null) {
			return "";
		}
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}

	public static String toHtml(String value) {
		if (value == null) {
			return "";
		}
		return value//
				.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")//
				.replace("<script>", "&lt;script&gt;")//
				.replace("</script>", "&lt;/script&gt;")//
				.replace("  ", "&nbsp;&nbsp;")//
				.replace("\r\n", "<br />")//
				.replace("\n", "<br />")//
				.replace("\r", "<br />")//
		;
	}

	/**
	 * 判断字符串是否为空或一串空白？
	 * 
	 * @param str
	 * @return 参数为null或空白串都将返回true，否则返回false。
	 */
	public static boolean isNil(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 剪切字符串两端的空白。
	 * 
	 * @param str
	 * @return 参数为null将返回空串"", 否则返回str.trim();
	 */
	public static String trim(String str) {
		if (str == null)
			return "";

		return str.trim();
	}

	public static String[] toArray(String str) {
		return toArray(str, null);
	}

	public static String[] toArray(String str, String token) {
		List<String> list = toList(str, token);

		String[] array = new String[list.size()];

		for (int i = list.size() - 1; i >= 0; i--) {
			array[i] = list.get(i);
		}

		return array;
	}

	public static List<String> toList(String str) {
		return toList(str, null);
	}

	public static List<String> toList(String str, String token) {
		List<String> list = new ArrayList();

		if (isNil(str)) {
			return list;
		}

		if (isNil(token))
			token = ";, ";

		if (token.length() == 1) {
			int idx = str.indexOf(token);
			while (idx > -1) {
				list.add(str.substring(0, idx));
				str = str.substring(idx + 1);
				idx = str.indexOf(token);
			}
			list.add(str);
		} else {
			StringTokenizer st = new StringTokenizer(str, token);
			while (st.hasMoreElements()) {
				list.add((String) st.nextElement());
			}
		}

		return list;
	}

	/**
	 * Hex解密
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String decodeHex(String str) {
		if (str == null)
			return null;

		// return new String(BinaryCodec.fromAscii(str.toCharArray()));

		try {
			return new String(Hex.decodeHex(str.toCharArray()));
		} catch (DecoderException e) {
			Log.warn("", e);
			return null;
		}
	}

	/**
	 * Hex加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeHex(String str) {
		if (str == null)
			return null;

		// return new String(BinaryCodec.toAsciiBytes(str.getBytes()));
		return new String(Hex.encodeHex(str.getBytes()));
	}

	/**
	 * 将指定的值转换成特定类型的对象，可以是String/Long/Integer/Short/Byte/Double/Float/Boolean/Date/Number。
	 * <p>
	 * 如果值类型不属于上面的任何类型，则将value当作一个JSON文本，并试图将其转换成指定类型的java对象。
	 * 
	 * @param text
	 *            文本字符串值
	 * @param defaultReturn
	 *            返回的默认值
	 * @return 转换后的值对象
	 */
	public static <T> T castTo(String text, T defaultReturn) {

		if (text == null)
			return defaultReturn;
		if (defaultReturn == null)
			return (T) text;

		Class valueType = defaultReturn.getClass();

		try {
			T ret = (T) castTo(text, valueType);
			if (ret != null)
				return ret;
		} catch (Throwable e) {
			Log.error("将文本转换成指定的Java对象失败！text=%s, valueType=%s, defaultReturn=%s", text, valueType, defaultReturn, e);
		}

		return defaultReturn;
	}

	/**
	 * 将指定的值转换成特定类型的对象，可以是String/Long/Integer/Short/Byte/Double/Float/Boolean/Date/Number。
	 * <p>
	 * 如果值类型不属于上面的任何类型，则将value当作一个JSON文本，并试图将其转换成指定类型的java对象。
	 * 
	 * @param text
	 *            文本字符串值
	 * @param valueType
	 *            需要转换的值类型
	 * @return 转换后的值对象
	 */
	public static <T> T castTo(String text, Class<T> valueType) {
		if (text == null || valueType == null)
			return null;

		if (isNil(text))
			return null;

		if (valueType.equals(String.class))
			return (T) text;
		if (valueType.equals(Long.class))
			return (T) Long.valueOf(text);
		if (valueType.equals(Integer.class))
			return (T) Integer.valueOf(text);
		if (valueType.equals(Short.class))
			return (T) Short.valueOf(text);
		if (valueType.equals(Byte.class))
			return (T) Byte.valueOf(text);
		if (valueType.equals(Double.class))
			return (T) Double.valueOf(text);
		if (valueType.equals(Float.class))
			return (T) Float.valueOf(text);
		if (valueType.equals(Boolean.class))
			return (T) Boolean.valueOf(text);
		if (Date.class.isAssignableFrom(valueType))
			return (T) DateUtil.parse(text);
		if (Number.class.isAssignableFrom(valueType))
			return ClassUtil.newInstance(valueType, text);

		return Json.fromJson(valueType, text);
	}

	public static String join(String[] arr, String sep, boolean ignoreNil) {
		StringBuffer sb = new StringBuffer();

		for (String str : arr) {
			if (ignoreNil && isNil(str))
				continue;

			sb.append(sep).append(str);
		}

		return sb.length() > 0 ? sb.substring(1) : "";
	}

	public static boolean isNumber(String number) {
		try {
			Long.parseLong(number);
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	public static void validatePassword(String pwssword) {
		int minLength = 8;
		int maxLength = 16;
		if (pwssword.length() < minLength || pwssword.length() > maxLength) {
			throw new CocException("密码长度必须为8-16位！");
		}
		// if (newPassword.toLowerCase().equals(newPassword)) {
		// throw new DemsyException("密码必须包含至少一个大写字母！");
		// }
		//
		// if (newPassword.toUpperCase().equals(newPassword)) {
		// throw new DemsyException("密码必须包含至少一个小写字母！");
		// }
		//
		// boolean containsNumber = false;
		// for (int i = newPassword.length() - 1; i >= 0; i--) {
		// char c = newPassword.charAt(i);
		// if (c >= '0' && c <= '9') {
		// containsNumber = true;
		// break;
		// }
		// }
		// if (!containsNumber) {
		// throw new DemsyException("密码必须包含至少一个数字！");
		// }

		// boolean containsSymbols = false;
		// for (int i = newPassword.length() - 1; i >= 0; i--) {
		// char c = newPassword.charAt(i);
		// if (!(c >= '0' && c <= '9')// is not number
		// && !(c >= 'a' && c <= 'z')// is not lower case
		// && !(c >= 'A' && c <= 'Z')// is not lower case
		// && !(c == ' ')// is not space
		// ) {
		// containsSymbols = true;
		// break;
		// }
		// }
		// if (!containsSymbols) {
		// throw new DemsyException("密码必须包含至少一个特殊字符（出数字、字母以外的字符）！");
		// }

	}
}
