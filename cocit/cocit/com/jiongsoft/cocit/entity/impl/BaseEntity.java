// $codepro.audit.disable com.instantiations.assist.eclipse.analysis.audit.rule.effectivejava.obeyEqualsContract.obeyGeneralContractOfEquals
package com.jiongsoft.cocit.entity.impl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.jiongsoft.cocit.entity.CoEntity;

/**
 * 组件化自定义实体基类：该类不支持扩展属性。
 * <p>
 * 
 * @author yongshan.ji
 */
public abstract class BaseEntity implements CoEntity {
	@Id
	@Column(name = "_id")
	@GeneratedValue(generator = "SftIdGen", strategy = GenerationType.TABLE)
	@TableGenerator(name = "SftIdGen", table = "DEMSY_00000000", pkColumnName = "id_key", valueColumnName = "next_hi", allocationSize = 1, initialValue = 20)
	protected Long id;

	protected Long softID;

	@Column(updatable = false)
	protected String entityGuid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(Serializable id) {
		this.id = (Long) id;
	}

	public Long getSoftID() {
		return softID;
	}

	public void setSoftID(Long id) {
		this.softID = id;
	}

	public String getEntityGuid() {
		return entityGuid;
	}

	public void setEntityGuid(String entityGuid) {
		this.entityGuid = entityGuid;
	}

	// @Override
	// public <T> T get(String extPropName, T defaultReturn) {
	// return null;
	// }
	//
	// @Override
	// public void set(String extPropName, String extPropValue) {
	// }

	// ***Other
	@Override
	public int hashCode() {
		if (id == null) {
			return super.hashCode();
		}
		return 37 * 17 + id.hashCode();
	}

	@Override
	public boolean equals(Object that) {
		if (that == null)
			return false;

		// 只要GUID相同就认为这两个对象相等
		BaseEntity thatEntity = (BaseEntity) that;
		if (entityGuid != null && thatEntity.entityGuid != null)
			return this.entityGuid.equals(thatEntity.entityGuid);

		// 简单判断参与比较的两个类是否相同
		String thisClass = this.getClass().getName();
		String thatClass = that.getClass().getName();
		// Nutz动态类使用$$作后缀起始标志
		int idx = thisClass.indexOf("$$");
		if (idx > 0)
			thisClass = thisClass.substring(0, idx);
		idx = thatClass.indexOf("$$");
		if (idx > 0)
			thatClass = thatClass.substring(0, idx);

		if (!thisClass.equals(thatClass))
			return false;

		// GUID不存在，则比较ID
		if (id != null && thatEntity.id != null)
			thatEntity.id.equals(id);

		return super.equals(that);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + id;
	}

}
