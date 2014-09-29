package com.jiongsoft.cocit.entity;

/**
 * 表实体对象：用来存储软件中用到数据表信息。
 * 
 * @author jiongsoft
 * 
 */
public interface TableEntity extends CoEntity {
	/**
	 * 获取排序字段。数据格式：
	 * <p>
	 * grid:updated desc, tree:name asc
	 * <p>
	 * 含义：在GRID中默认按updated倒排序，在导航树中按name正排序。
	 * 
	 * @return
	 */
	public String getSortExpr();
}
