package com.jiongsoft.cocit.util;

import java.util.List;

import com.jiongsoft.cocit.util.sort.SortStrategy;

public abstract class SortUtil {

	private static SortStrategy sortStrategy = SortStrategy.defaultStrategy;

	public static void sort(Object[] obj, boolean nullGT) {
		sortStrategy.sort(obj, "orderby", nullGT);
	}

	public static void sort(List obj, boolean nullGT) {
		sortStrategy.sort(obj, "orderby", nullGT);
	}

	public static void sort(Object[] obj, String field, boolean nullGT) {
		sortStrategy.sort(obj, field, nullGT);
	}

	public static void sort(List list, String field, boolean nullGT) {
		sortStrategy.sort(list, field, nullGT);
	}
}
