// $codepro.audit.disable
package com.jiongsoft.cocit.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jiongsoft.cocit.orm.Orm;
import com.jiongsoft.cocit.orm.expr.Expr;

/**
 * 业务事件
 * 
 * @author yongshan.ji
 */
public class ActionEvent<T> {
	private Map param = new HashMap();

	// 方法参数
	private T entity;

	private Class entityType;

	private Serializable entityID;

	private Expr expr;

	// 操作返回值
	private Object returnValue;

	private Orm orm;

	/**
	 * 获取事件实体对象，事件实体可以是单个数据实体、实体集合、查询分页等。
	 * 
	 * @return
	 */
	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Map getParam() {
		return param;
	}

	public void addParam(String key, Object value) {
		this.param.put(key, value);
	}

	public Class getEntityType() {
		return entityType;
	}

	public void setEntityType(Class klass) {
		this.entityType = klass;
	}

	public Expr getExpr() {
		return expr;
	}

	public void setExpr(Expr expr) {
		this.expr = expr;
	}

	public Serializable getEntityID() {
		return entityID;
	}

	public void setEntityID(Serializable id) {
		this.entityID = id;
	}

	public <X> X getReturnValue() {
		return (X) returnValue;
	}

	public void setReturnValue(Object resultEntity) {
		this.returnValue = resultEntity;
	}

	public Orm getOrm() {
		return orm;
	}

	public void setOrm(Orm orm) {
		this.orm = orm;
	}

}
