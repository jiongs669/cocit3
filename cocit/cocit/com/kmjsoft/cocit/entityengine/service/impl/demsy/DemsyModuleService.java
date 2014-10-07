// $codepro.audit.disable unnecessaryCast
package com.kmjsoft.cocit.entityengine.service.impl.demsy;

import java.util.Date;
import java.util.List;

import com.jiongsoft.cocit.entitydef.field.Upload;
import com.kmjsoft.cocit.entity.impl.security.Module;
import com.kmjsoft.cocit.entityengine.service.ModuleService;
import com.kmjsoft.cocit.entityengine.service.TableService;

public class DemsyModuleService implements ModuleService {
	private Module entity;

	private TableService mainDataTable;

	private List<TableService> childrenDataTables;

	DemsyModuleService(Module e, TableService refrencedDataTable) {
		this.entity = e;
		this.mainDataTable = refrencedDataTable;
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
	public String getLogo() {
		Upload u = entity.getLogo();
		return u == null ? null : u.toString();
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