// $codepro.audit.disable unnecessaryCast
package com.jiongsoft.cocit.service.impl.demsy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.jiongsoft.cocit.service.FieldService;
import com.jiongsoft.cocit.service.FieldGroupService;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.StringUtil;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.impl.sft.system.SystemDataGroup;
import com.kmetop.demsy.engine.BizEngine;

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

	@Override
	public Properties getExtProps() {
		return entity.getDynaProp();
	}

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
	public String getInfo() {
		return entity.getDesc();
	}

	@Override
	public Date getCreatedDate() {
		return entity.getCreated();
	}

	@Override
	public String getCreatedUser() {
		return entity.getCreatedBy();
	}

	@Override
	public Date getLatestModifiedDate() {
		return entity.getUpdated();
	}

	@Override
	public String getLatestModifiedUser() {
		return entity.getUpdatedBy();
	}

	@Override
	public <T> T get(String propName, T defaultReturn) {
		String value = entity.get(propName);

		if (value == null)
			return defaultReturn;
		if (defaultReturn == null)
			return (T) value;

		Class valueType = defaultReturn.getClass();

		try {
			return (T) StringUtil.castTo(value, valueType);
		} catch (Throwable e) {
			Log.warn("", e);
		}

		return defaultReturn;
	}

	@Override
	public String getMode(String opCode) {

		return ((BizEngine) Demsy.bizEngine).parseMode(opCode, entity.getMode());

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

	@Override
	public int getSequence() {
		return entity.getOrderby();
	}

	public SystemDataGroup getEntity() {
		return entity;
	}
}
