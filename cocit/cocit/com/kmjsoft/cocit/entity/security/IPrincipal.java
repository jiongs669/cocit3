package com.kmjsoft.cocit.entity.security;

import com.kmjsoft.cocit.entity.INamedEntity;
import com.kmjsoft.cocit.entity.ITenantOwnerEntity;

public interface IPrincipal extends INamedEntity, ITenantOwnerEntity {

	/**
	 * 关联GUID：逻辑外键，可以关联到员工、岗位、组织机构等。
	 * <p>
	 * <UL>
	 * <LI>如果主体类型是人员{@link IUser}：则该字段关联到{@link IUser#getDataGuid()}字段；
	 * <LI>如果主体类型是组{@link IGroup}：则该字段关联到{@link IGroup#getDataGuid()}字段；
	 * <LI>如果主体类型是角色{@link IRole}：则该字段关联到{@link IRole#getDataGuid()}字段；
	 * </UL>
	 * 
	 * @return
	 */
	String getReferencedGuid();

}
