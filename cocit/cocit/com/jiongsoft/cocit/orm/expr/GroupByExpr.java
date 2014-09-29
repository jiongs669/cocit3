package com.jiongsoft.cocit.orm.expr;


/**
 * 分组表达式：用于生成SQL语句中的 group by 部分。
 * 
 * @author yongshan.ji
 */
public class GroupByExpr extends Expr {

	private String prop;

	public GroupByExpr(String prop) {
		this.prop = prop;
	}

	/**
	 * 获取排序字段
	 * 
	 * @return
	 */
	public String getProp() {
		return prop;
	}

	public String toString() {
		return prop;
	}

}
