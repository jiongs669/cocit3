package com.jiongsoft.cocit.orm;

import java.util.List;

/**
 * 该类的对象用于包装数据库查询结果
 * 
 * @author yongshan.ji
 * 
 */
public abstract class PageResult<T> {
	/**
	 * 总记录数
	 */
	protected int total;

	/**
	 * 页大小
	 */
	protected int pageSize;

	/**
	 * 页索引
	 */
	protected int pageIndex;

	/**
	 * 查询到的数据库记录
	 */
	protected List<T> rows;
}
