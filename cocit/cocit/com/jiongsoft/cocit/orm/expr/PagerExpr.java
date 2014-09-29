package com.jiongsoft.cocit.orm.expr;

/**
 * 分页表达式
 * 
 * @author yongshan.ji
 */
public class PagerExpr extends Expr {
	public static int DEFAULT_PAGE_SIZE = 20;

	public static int DEFAULT_PAGE_INDEX = 1;

	private int pageSize = DEFAULT_PAGE_SIZE;// 记录大小

	private int pageIndex = DEFAULT_PAGE_INDEX;// 页索引

	public PagerExpr(int pageIndex, int pageSize) {
		if (pageIndex > 0) {
			this.pageIndex = pageIndex;
		}
		if (pageSize > 0) {
			this.pageSize = pageSize;
		}
	}

	/**
	 * 获取也大小
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 获取也索引
	 * 
	 * @return
	 */
	public int getPageIndex() {
		return pageIndex;
	}

}
