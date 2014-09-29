package com.jiongsoft.cocit.util.sort;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.jiongsoft.cocit.util.log.ILog;
import com.jiongsoft.cocit.util.log.Logs;

public abstract class AbstractSort implements SortStrategy {
	ILog log = Logs.get();

	public void sort(List list, String field, boolean nullGT) {
		if (list == null) {
			return;
		}
		Object[] array = list.toArray();
		this.sort(array, field, nullGT);
		list.clear();
		for (Object obj : array) {
			list.add(obj);
		}
	}

	public <T> T getValue(Object obj, final String path) {
		if (obj == null) {
			return null;
		}

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
				log.warn("", e);
			}
			if (subObj == null) {
				return null;
			}

			return (T) getValue(subObj, path.substring(dot + 1));
		} else {
			try {
				return (T) Mirror.me(obj.getClass()).getValue(obj, path);
			} catch (Throwable e) {
				log.warn("", e);
				return null;
			}
		}
	}

	public double compare(Object obj1, Object obj2, boolean nullGT) {
		if (obj1 == null && obj2 == null) {
			return 0;
		} else if (obj1 == null) {
			if (nullGT)
				return 1;
			return -1;
		} else if (obj2 == null) {
			if (nullGT)
				return -1;
			return 1;
		}
		if (obj1 instanceof Number && obj2 instanceof Number) {
			return ((Number) obj1).doubleValue() - ((Number) obj2).doubleValue();
		}
		if (obj1 instanceof Date && obj2 instanceof Date) {
			return ((Date) obj1).getTime() - ((Date) obj2).getTime();
		}
		return obj1.toString().compareTo(obj2.toString());
	}
}
