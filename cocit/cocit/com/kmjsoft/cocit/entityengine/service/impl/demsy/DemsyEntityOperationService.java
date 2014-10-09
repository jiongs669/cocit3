// $codepro.audit.disable unnecessaryCast
package com.kmjsoft.cocit.entityengine.service.impl.demsy;

import java.util.Date;

import com.kmjsoft.cocit.entity.impl.module.EntityAction;
import com.kmjsoft.cocit.entity.module.IEntityAction;
import com.kmjsoft.cocit.entityengine.service.OperationService;

public class DemsyEntityOperationService implements OperationService {

	private EntityAction entity;

	// lazy load
	// private List<BizAction> childrenBizActions;

	DemsyEntityOperationService(EntityAction e) {
		this.entity = e;
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
	public String getCode() {
		return "" + entity.getTypeCode();
	}

	@Override
	public Date getOperatedDate() {
		return entity.getUpdatedDate();
	}

	@Override
	public String getOperatedUser() {
		return entity.getUpdatedUser();
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

	// @Override
	// public List<EntityOperationService> getChildrenBizOperations() {
	// if (childrenBizActions == null)
	// return null;
	//
	// List<EntityOperationService> ret = new ArrayList();
	//
	// for (BizAction action : childrenBizActions) {
	// ret.add(new DemsyEntityOperationService(action));
	// }
	//
	// return ret;
	// }
	//
	// public void setChildrenBizActions(List<BizAction> children) {
	// this.childrenBizActions = children;
	// }

	// @Override
	// public String getLogo() {
	// String logoPath = null;
	// Upload logo = entity.getLogo();
	// if (logo == null || Str.isEmpty(logo.toString()))
	// logoPath = Demsy.appconfig.get("imagepath.actionlib") + "/" + entity.getMode() + ".gif";
	// else
	// logoPath = logo.toString();
	//
	// return logoPath;
	// }

	@Override
	public Long getParentID() {
		EntityAction parent = entity.getParentAction();
		return parent == null ? null : parent.getId();
	}

	@Override
	public String getMode() {
		return entity.getMode();
	}

	@Override
	public IEntityAction getEntity() {
		return entity;
	}

	@Override
	public String getActionPage() {
		return entity.getPageTemplate();
	}

	@Override
	public String getActionWindow() {
		return entity.getTargetWindow();
	}
}
