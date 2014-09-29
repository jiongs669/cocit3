package com.jiongsoft.cocit.util.sort;

/**
 * 
 * 利用冒泡排序法对数组排序，数组中元素必须实现了Comparable接口。
 */
public class BubbleSort extends AbstractSort {
	/**
	 * 对数组obj中的元素以冒泡排序算法进行排序
	 */
	public void sort(Object[] obj, String field, boolean nullGT) {
		if (obj == null) {
			throw new NullPointerException("The argument can not be null!");
		}
		Object tmp;
		for (int i = 0; i < obj.length; i++) {
			// 切记，每次都要从第一个开始比。最后的不用再比。
			for (int j = 0; j < obj.length - i - 1; j++) {
				// 对邻接的元素进行比较，如果后面的小，就交换
				try {
					if (compare(getValue(obj[j], field), getValue(obj[j + 1], field), nullGT) > 0) {
						tmp = obj[j];
						obj[j] = obj[j + 1];
						obj[j + 1] = tmp;
					}
				} catch (Throwable ex) {
					log.warn("", ex);
				}
			}
		}
	}
}
