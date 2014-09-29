// $codepro.audit.disable unnecessaryCast
package com.jiongsoft.cocit.service.impl.demsy;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.jiongsoft.cocit.service.ModuleService;
import com.jiongsoft.cocit.service.TableService;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.StringUtil;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.base.security.Module;

public class DemsyModuleService implements ModuleService {
	private Module entity;

	private TableService mainDataTable;

	private List<TableService> childrenDataTables;

	DemsyModuleService(Module e, TableService refrencedDataTable) {
		this.entity = e;
		this.mainDataTable = refrencedDataTable;
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
	public int getSequence() {
		return entity.getOrderby();
	}

	@Override
	public String getLogo() {
		Upload u = entity.getLogo();
		return u == null ? null : u.toString();
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
	public TableService getTable() {
		return mainDataTable;
	}

	public List<TableService> getChildrenTables() {
		return childrenDataTables;
	}

	public void setChildrenDataTables(List<TableService> childrenDataTables) {
		this.childrenDataTables = childrenDataTables;
	}

	public Module getEntity() {
		return entity;
	}

	@Override
	public List<ModuleService> getChildrenModules() {
		return null;
	}

	@Override
	public int getType() {
		return entity.getType();
	}

}