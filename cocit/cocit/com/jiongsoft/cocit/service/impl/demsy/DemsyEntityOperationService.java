// $codepro.audit.disable unnecessaryCast
package com.jiongsoft.cocit.service.impl.demsy;

import java.util.Date;
import java.util.Properties;

import com.jiongsoft.cocit.entity.OperationEntity;
import com.jiongsoft.cocit.service.OperationService;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.StringUtil;
import com.kmetop.demsy.comlib.impl.base.biz.BizAction;

public class DemsyEntityOperationService implements OperationService {

	private BizAction entity;

	// lazy load
	// private List<BizAction> childrenBizActions;

	DemsyEntityOperationService(BizAction e) {
		this.entity = e;
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
	public int getSequence() {
		return entity.getOrderby();
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
	public String getCode() {
		return "" + entity.getTypeCode();
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
		BizAction parent = entity.getParentAction();
		return parent == null ? null : parent.getId();
	}

	@Override
	public String getMode() {
		return entity.getMode();
	}

	@Override
	public OperationEntity getEntity() {
		return entity;
	}

	@Override
	public String getActionPage() {
		return entity.getTemplate();
	}

	@Override
	public String getActionWindow() {
		return entity.getTargetWindow();
	}
}
