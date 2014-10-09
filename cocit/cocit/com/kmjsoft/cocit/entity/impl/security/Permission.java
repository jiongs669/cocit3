package com.kmjsoft.cocit.entity.impl.security;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_ADMIN;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_ADMIN_PERMISSION;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_SYSADMIN_PERMISSION;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_EDIT;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZ_DEL;

import java.util.Date;

import javax.persistence.Entity;

import com.kmjsoft.cocit.entity.TenantOwnerEntity;
import com.kmjsoft.cocit.entity.security.IPermission;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "权限管理", GUID = BIZSYS_ADMIN_PERMISSION, catalog = BIZCATA_ADMIN, SN = ORDER_SYSADMIN_PERMISSION, isBuildin = false// , pathPrefix = UrlAPI.URL_NS//
, actions = {
//
		@CocAction(name = "授权", type = TYPE_BZFORM_NEW, mode = "c", pluginName = "com.kmetop.demsy.plugins.security.SavePermission")//
		// @CocOperation(name = "授权", typeCode = TYPE_BZFORM_NEW, mode = "c2", pluginName = "com.kmetop.demsy.plugins.security.SavePermission", template = "getPermissionForm")//
		, @CocAction(name = "编辑", type = TYPE_BZFORM_EDIT, mode = "e", pluginName = "com.kmetop.demsy.plugins.security.SavePermission") //
		, @CocAction(name = "删除", type = TYPE_BZ_DEL, mode = "d", pluginName = "com.kmetop.demsy.plugins.security.SavePermission") //
		, @CocAction(name = "查看", type = TYPE_BZFORM_EDIT, mode = "v") //
}//
, groups = { //
		@CocGroup(name = "基本信息", GUID = "basic"//
		, fields = {
				//
				@CocColumn(propName = "name", name = "权限标题", mode = "*:N v:S c:M c2:M e:M", gridOrder = 1) //
				,
				@CocColumn(propName = "type", name = "权限类型", mode = "*:N v:S c:M c2:M e:M", options = "1:允许,0:禁止", gridOrder = 2) //

				// COCIT V1 授权规则：
				,
				@CocColumn(propName = "userType", name = "用户类型", mode = "*:N v:S c2:M e:M", gridOrder = 3, options = "_soft_administrator:后台管理员,_WebUser:网站会员", desc = "表示该权限被授予哪种类型的用户？如“网站注册用户、后台管理员”等。") //
				,
				@CocColumn(propName = "userRule", name = "授权用户", mode = "*:N v:S c2:E e:E", gridOrder = 4, desc = "表示该权限被授予哪些“用户”？语法规则：可以是“查询表达式”{field_1: [num_1, num_2, ..., num_n], field_2: singleValue, ..., field_n: ['str_1', 'str_2', ..., 'str_n'] }或“用户ID数组”[id-1, id-2, 'user-3', ..., 'user-n']，不填表示所有用户。") //
				,
				@CocColumn(propName = "funcRule", name = "功能权限", mode = "*:N v:S c2:M e:M", gridOrder = 5, desc = "表示“用户群体”可以执行模块中的哪些功能？语法规则：['moduleID:tableID:opModes', 'm-1:t-1:op1,op2,op3', ..., 'm-i:t-i:op1,op2,op-i']，语法举例：['1:2:*', '2:*:v,e,bu,d', '*:*:e', ..., '1', ':2', '::c']。") //
				,
				@CocColumn(propName = "dataRule", name = "数据权限", mode = "*:N v:S c2:E e:E", gridOrder = 6, desc = "表示“用户群体”可以操作模块中的哪些数据？语法规则：可以是“查询表达式”{field_1: [num_1, num_2, ..., num_n], field_2: singleValue, ..., field_n: ['str_1', 'str_2', ..., 'str_n'] }或“数据ID数组”[id-1, id-2, id-3, ..., id-n]，不填表示所有数据。") //
				, @CocColumn(propName = "desc", name = "权限描述", mode = "*:N v:S c:M c2:M e:M", gridOrder = 7, desc = "简要描述该权限被授予哪些人，目的和用途是什么？") //

				// COCIT V1/DEMSY V2 公用权限状态
				, @CocColumn(name = "权限状态", propName = "disabled", mode = "*:N v:S c:E c2:E e:E", options = "0:启用,1:停用", gridOrder = 22) //
				, @CocColumn(name = "有效期自", propName = "expiredFrom", mode = "*:N v:S c:E c2:E e:E", pattern = "yyyy-MM-dd HH:mm:ss", gridOrder = 12) //
				, @CocColumn(name = "有效期至", propName = "expiredTo", mode = "*:N v:S c:E c2:E e:E", pattern = "yyyy-MM-dd HH:mm:ss", gridOrder = 13) //
				, @CocColumn(name = "创建时间", propName = "created", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm:ss", gridOrder = 8) //
				, @CocColumn(name = "创建者帐号", propName = "createdBy", mode = "*:N v:S", gridOrder = 21) //
		//
		}),// end: CocGroup
		@CocGroup(name = "授权规则", GUID = "other"//
		, fields = {
				// DEMSY V2 授权规则：不再使用
				@CocColumn(propName = "users", mode = "*:N c:E") //
				, @CocColumn(propName = "users.module", name = "用户类型", isTransient = true, mode = "*:N") //
				, @CocColumn(propName = "users.rules2", name = "用户规则", isTransient = true, mode = "*:N") //
				, @CocColumn(propName = "datas", mode = "*:N c:E") //
				, @CocColumn(propName = "datas.module", name = "功能模块", isTransient = true, mode = "*:N") //
				, @CocColumn(propName = "datas.rules2", name = "数据权限", isTransient = true, mode = "*:N") //
				, @CocColumn(propName = "actions", name = "模块操作", mode = "*:N") //
		}), // end: CocGroup
}// end: groups
)
public class Permission extends TenantOwnerEntity implements IPermission {
	protected byte principalType;

	protected String principalGuid;

	protected String funMenuGuid;

	protected String actionsRule;

	protected String dataRowsRule;

	protected String dataColumnsRule;

	protected boolean denied;

	protected Date expiredFrom;

	protected Date expiredTo;

	public byte getPrincipalType() {
		return principalType;
	}

	public void setPrincipalType(byte principalType) {
		this.principalType = principalType;
	}

	public String getPrincipalGuid() {
		return principalGuid;
	}

	public void setPrincipalGuid(String principalGuid) {
		this.principalGuid = principalGuid;
	}

	public String getFunMenuGuid() {
		return funMenuGuid;
	}

	public void setFunMenuGuid(String moduleGuid) {
		this.funMenuGuid = moduleGuid;
	}

	public String getActionsRule() {
		return actionsRule;
	}

	public void setActionsRule(String moduleActionsRule) {
		this.actionsRule = moduleActionsRule;
	}

	public String getDataRowsRule() {
		return dataRowsRule;
	}

	public void setDataRowsRule(String dataRowsRule) {
		this.dataRowsRule = dataRowsRule;
	}

	public String getDataColumnsRule() {
		return dataColumnsRule;
	}

	public void setDataColumnsRule(String dataColumnsRule) {
		this.dataColumnsRule = dataColumnsRule;
	}

	public boolean isDenied() {
		return denied;
	}

	public void setDenied(boolean denied) {
		this.denied = denied;
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
