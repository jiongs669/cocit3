package com.cocit.api.entitydef;

import com.cocit.api.security.IAction;

/**
 * <b>业务操作：</b>业务系统的操作，用于完成业务系统的管理功能，是最小的功能点。
 * 
 * @author yongshan.ji
 */
public interface IEntityAction extends IAction {
	boolean isDisabled();
}
