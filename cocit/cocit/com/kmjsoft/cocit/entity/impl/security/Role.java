package com.kmjsoft.cocit.entity.impl.security;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_ADMIN;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_SYSADMIN_USER_GROUP;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.kmjsoft.cocit.entity.security.IRole;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "后台帐号分组", catalog = BIZCATA_ADMIN, SN = ORDER_SYSADMIN_USER_GROUP, isBuildin = false//
, actions = { @CocAction(name = "新增分组", type = TYPE_BZFORM_NEW, mode = "c")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = { @CocColumn(name = "分组名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "分组编号", propName = "code", mode = "c:M e:M") //
		, @CocColumn(propName = "parent") //
		, @CocColumn(name = "分组描述", propName = "desc") //
		, @CocColumn(name = "创建时间", propName = "created", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "v:S *:N") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "v:S *:N") //
}) }// end groups
)
public class Role extends Principal implements IRole {

	protected String parentGuid;

	@Column(length = 128)
	protected String parentName;

	public String getParentGuid() {
		return parentGuid;
	}

	public void setParentGuid(String parentGuid) {
		this.parentGuid = parentGuid;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}
