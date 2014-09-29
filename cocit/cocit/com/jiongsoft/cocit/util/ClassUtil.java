// $codepro.audit.disable unnecessaryCast
package com.jiongsoft.cocit.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Date;

import org.nutz.lang.Mirror;

public abstract class ClassUtil {

	/**
	 * 判断指定的类是否是基本类型？基本类型包括String、Boolean、Number。
	 * 
	 * @param cls
	 * @return
	 */
	public static boolean isPrimitive(Object obj) {
		return isString(obj) || isBoolean(obj) || isInteger(obj) || isLong(obj) || isByte(obj) || isShort(obj) || isDouble(obj) || isFloat(obj);
	}

	public static boolean isDate(Object obj) {
		if (obj == null)
			return false;

		Class cls;
		if (obj instanceof Class)
			cls = (Class) obj;
		else
			cls = obj.getClass();

		return Date.class.isAssignableFrom(cls);
	}

	public static boolean isString(Object obj) {
		if (obj == null)
			return false;

		Class cls;
		if (obj instanceof Class)
			cls = (Class) obj;
		else
			cls = obj.getClass();

		return cls.equals(String.class);
	}

	public static boolean isBoolean(Object obj) {
		if (obj == null)
			return false;

		Class cls;
		if (obj instanceof Class)
			cls = (Class) obj;
		else
			cls = obj.getClass();

		return cls.equals(Boolean.class);
	}

	public static boolean isNumber(Object obj) {
		if (isInteger(obj) || isLong(obj) || isByte(obj) || isShort(obj) || isDouble(obj) || isFloat(obj)) {
			return true;
		}

		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return Number.class.isAssignableFrom(kls);
	}

	public static boolean isFloat(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Float.class);
	}

	public static boolean isDouble(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Double.class);
	}

	public static boolean isByte(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Byte.class);
	}

	public static boolean isShort(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Short.class);
	}

	public static boolean isInteger(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Integer.class);
	}

	public static boolean isLong(Object obj) {
		if (obj == null)
			return false;

		Class kls;
		if (obj instanceof Class)
			kls = (Class) obj;
		else
			kls = obj.getClass();

		return kls.equals(Long.class);
	}

	public static <T> T newInstance(String name, Object... args) {
		if (name == null || name.trim().length() == 0)
			return null;

		Class<T> cls = forName(name);

		return newInstance(cls, args);
	}

	public static <T> T newInstance(Class<T> cls, Object... args) {

		Object object = null;
		if (cls.isArray()) {
			object = Array.newInstance(cls.getComponentType(), 0);
		} else {
			Constructor constructor = null;
			Constructor[] constructors = cls.getDeclaredConstructors();
			for (Constructor c : constructors) {
				Class[] types = c.getParameterTypes();
				if (types.length != args.length)
					continue;

				boolean isMatched = true;
				for (int i = 0; i < args.length; i++) {
					Object arg = args[i];
					if (arg != null && !types[i].isAssignableFrom(arg.getClass())) {
						isMatched = false;
					}
				}
				if (isMatched) {
					constructor = c;
					break;
				}
			}
			if (constructor != null) {
				constructor.setAccessible(true);
				try {
					object = constructor.newInstance(args);
				} catch (Throwable e) {
					throw new CocException(e);
				}
			}
		}

		return (T) object;
	}

	public static Class forName(String name) {
		return forName(name, getDefaultClassLoader());
	}

	public static Class forName(String name, ClassLoader classLoader) {
		name = name.trim();

		// "java.lang.String[]" style arrays
		if (name.endsWith("[]")) {
			String elementClassName = name.substring(0, name.length() - 2);
			Class elementClass = forName(elementClassName, classLoader);

			return Array.newInstance(elementClass, 0).getClass();
		}

		// "[Ljava.lang.String;" style arrays
		if (name.startsWith("[L") && name.endsWith(";")) {
			String elementClassName = name.substring(2, name.length() - 1);
			Class elementClass = forName(elementClassName, classLoader);

			return Array.newInstance(elementClass, 0).getClass();
		}

		try {
			return classLoader.loadClass(name);
		} catch (ClassNotFoundException e) {
			throw new CocException(e);
		}
	}

	private static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		if (cl == null) {
			cl = ClassUtil.class.getClassLoader();
		}

		return cl;
	}

	public static boolean hasField(Class klass, String propName) {
		try {
			return Mirror.me(klass).getField(propName) != null;
		} catch (Throwable e) {
			Log.warn("", e);
			return false;
		}
	}
}
