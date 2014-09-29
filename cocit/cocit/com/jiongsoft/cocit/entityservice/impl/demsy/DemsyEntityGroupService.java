// $codepro.audit.disable unnecessaryCast
package com.jiongsoft.cocit.entityservice.impl.demsy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cocit.Demsy;
import com.cocit.engine.BizEngine;
import com.cocit.entity.entitydef.SystemDataGroup;
import com.jiongsoft.cocit.entityservice.FieldGroupService;
import com.jiongsoft.cocit.entityservice.FieldService;

public class DemsyEntityGroupService implements FieldGroupService {
	private SystemDataGroup entity;

	private List<FieldService> dataFields;

	DemsyEntityGroupService(SystemDataGroup e) {
		this.entity = e;
		dataFields = new ArrayList();
	}

	void addField(FieldService f) {
		this.dataFields.add(f);
	}

	// @Override
	// public Properties getExtProps() {
	// return entity.getProperties();
	// }

	@Override
	public Long getID() {
		return entity.getId();
	}

	@Override
	public String getName() {
		return entity.getName();
	}

	@Override
	public boolean isDisabled() {
		return entity.isDisabled();
	}

	@Override
	public Date getOperatedDate() {
		return entity.getOperatedDate();
	}

	@Override
	public String getOperatedUser() {
		return entity.getOperatedUser();
	}

	// @Override
	// public <T> T get(String propName, T defaultReturn) {
	// String value = entity.get(propName);
	//
	// if (value == null)
	// return defaultReturn;
	// if (defaultReturn == null)
	// return (T) value;
	//
	// Class valueType = defaultReturn.getClass();
	//
	// try {
	// return (T) StringUtil.castTo(value, valueType);
	// } catch (Throwable e) {
	// Log.warn("", e);
	// }
	//
	// return defaultReturn;
	// }

	@Override
	public String getMode(String opCode) {

		return ((BizEngine) Demsy.entityDefEngine).parseMode(opCode, entity.getMode());

	}

	// @Override
	// public EntityTableService getDataTable() {
	// IBizSystem g = entity.getSystem();
	// if (g == null)
	// return null;
	//
	// return fc.makeDataTable(g.getId());
	// }

	@Override
	public List<FieldService> getEntityFields() {
		return this.dataFields;
	}

	public SystemDataGroup getEntity() {
		return entity;
	}
}
