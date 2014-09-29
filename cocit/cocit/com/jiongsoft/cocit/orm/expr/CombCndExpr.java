package com.jiongsoft.cocit.orm.expr;

/**
 * 组合表达式：用来描述条件组合
 * <p>
 * 用关系运算符AND/OR/NOT于将条件表达式组合在一起成为SQL语句的where部分。
 * 
 * @author yongshan.ji
 */
public class CombCndExpr extends CndExpr {

	private CombType type;
	private CndExpr expr;
	private CndExpr expr2;

	public CombCndExpr(CndExpr expr, CombType type, CndExpr expr2) {
		this.type = type;
		this.expr = expr;
		this.expr2 = expr2;
	}

	public CndExpr getExpr() {
		return expr;
	}

	public CndExpr getExpr2() {
		return expr2;
	}

	public CombType getType() {
		return type;
	}
}
