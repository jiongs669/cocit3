package com.jiongsoft.cocit.orm.expr;


/**
 * 排序表达式：用来描述SQL语句中的 order by 部分。
 * 
 * @author yongshan.ji
 */
public class OrderExpr extends Expr {

	private OrderType type;

	private String prop;

	public OrderExpr(String prop, OrderType type) {
		this.prop = prop;
		this.type = type;
	}

	/**
	 * 获取排序字段
	 * 
	 * @return
	 */
	public String getProp() {
		return prop;
	}

	public OrderType getType() {
		return type;
	}

}
