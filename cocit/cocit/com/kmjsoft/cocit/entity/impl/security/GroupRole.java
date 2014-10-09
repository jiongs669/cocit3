package com.kmjsoft.cocit.entity.impl.security;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_ADMIN;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_SYSADMIN_USER_ROLE;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import java.util.Date;

import javax.persistence.Entity;

import com.kmjsoft.cocit.entity.DataEntity;
import com.kmjsoft.cocit.entity.security.IGroupRole;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "后台帐号角色", catalog = BIZCATA_ADMIN, SN = ORDER_SYSADMIN_USER_ROLE, isBuildin = false//
, actions = { @CocAction(name = "新增分组", type = TYPE_BZFORM_NEW, mode = "c")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = { @CocColumn(name = "角色名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(propName = "type") //
		, @CocColumn(propName = "parent") //
		, @CocColumn(name = "角色描述", propName = "desc") //
		, @CocColumn(name = "创建时间", propName = "created", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "v:S *:N") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "v:S *:N") //
}) }// end groups
, jsonData = "UserRole.data.js"//
)
public class GroupRole extends DataEntity implements IGroupRole {
	private String groupGuid;

	private String roleGuid;

	protected Date expiredFrom;

	protected Date expiredTo;

	public String getGroupGuid() {
		return groupGuid;
	}

	public void setGroupGuid(String userGuid) {
		this.groupGuid = userGuid;
	}

	public String getRoleGuid() {
		return roleGuid;
	}

	public void setRoleGuid(String roleGuid) {
		this.roleGuid = roleGuid;
	}

	public Date getExpiredFrom() {
		return expiredFrom;
	}

	public void setExpiredFrom(Date expiredFrom) {
		this.expiredFrom = expiredFrom;
	}

	public Date getExpiredTo() {
		return expiredTo;
	}

	public void setExpiredTo(Date expiredTo) {
		this.expiredTo = expiredTo;
	}
}
