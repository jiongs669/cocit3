package com.jiongsoft.cocit.orm.expr;


/**
 * 字段正则表达式：用来描述SQL语句中要查询、修改、添加的字段集合。
 * 
 * @author yongshan.ji
 */
public class FieldRexpr extends Expr {

	private String regExpr;

	private boolean igloreNull;

	public FieldRexpr(String regExpr, boolean igloreNull) {
		this.regExpr = regExpr;
	}

	/**
	 * 获取正则表达式
	 * 
	 * @return
	 */
	public String getRegExpr() {
		return regExpr;
	}

	public boolean isIgloreNull() {
		return igloreNull;
	}

	public String toString() {
		return this.regExpr;
	}

}
