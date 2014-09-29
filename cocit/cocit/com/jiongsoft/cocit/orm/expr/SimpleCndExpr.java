package com.jiongsoft.cocit.orm.expr;


/**
 * 条件表达式： 用于描述SQL查询语句的where条件部分
 * 
 * @author yongshan.ji
 */
public class SimpleCndExpr extends CndExpr {

	private CndType type;

	private String prop;

	private Object value;

	private Object value2;

	private void init(String prop, CndType type, Object value) {
		this.type = type;
		this.prop = prop;
		this.value = value;
	}

	public SimpleCndExpr(String prop, CndType type, Object value) {
		this.init(prop, type, value);
	}

	public String getProp() {
		return prop;
	}

	public Object getValue() {
		return value;
	}

	public Object getValue2() {
		return value2;
	}

	public CndType getType() {
		return type;
	}

	public void setValue2(Object value2) {
		this.value2 = value2;
	}

}
