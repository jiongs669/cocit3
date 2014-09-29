package com.kmetop.demsy.comlib.impl.base.security;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_PERMISSION;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_PERMISSION;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jiongsoft.cocit.entity.PermissionEntity;
import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocField2;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;
import com.jiongsoft.cocit.util.StringUtil;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.field.Dataset;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.impl.sft.system.SFTSystem;
import com.kmetop.demsy.comlib.security.IPermission;

@Entity
@CocTable(name = "系统权限管理", code = BIZSYS_ADMIN_PERMISSION, catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_PERMISSION, buildin = false// , pathPrefix = UrlAPI.URL_NS//
, actions = {
//
		@CocOperation(name = "授权", typeCode = TYPE_BZFORM_NEW, mode = "c", pluginName = "com.kmetop.demsy.plugins.security.SavePermission")//
		// @CocOperation(name = "授权", typeCode = TYPE_BZFORM_NEW, mode = "c2", pluginName = "com.kmetop.demsy.plugins.security.SavePermission", template = "getPermissionForm")//
		, @CocOperation(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", pluginName = "com.kmetop.demsy.plugins.security.SavePermission") //
		, @CocOperation(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d", pluginName = "com.kmetop.demsy.plugins.security.SavePermission") //
		, @CocOperation(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
}//
, groups = { //
@CocGroup(name = "基本信息", code = "basic"//
, fields = {
//
		@CocField(property = "name", name = "权限标题", mode = "*:N v:S c:M c2:M e:M", gridOrder = 1) //
		, @CocField(property = "type", name = "权限类型", mode = "*:N v:S c:M c2:M e:M", options = "1:允许,0:禁止", gridOrder = 2) //

		// COCIT V1 授权规则：
		, @CocField(property = "userType", name = "用户类型", mode = "*:N v:S c2:M e:M", gridOrder = 3, options = "_soft_administrator:后台管理员,_WebUser:网站会员", desc = "表示该权限被授予哪种类型的用户？如“网站注册用户、后台管理员”等。") //
		, @CocField(property = "userRule", name = "授权用户", mode = "*:N v:S c2:E e:E", gridOrder = 4, desc = "表示该权限被授予哪些“用户”？语法规则：可以是“查询表达式”{field_1: [num_1, num_2, ..., num_n], field_2: singleValue, ..., field_n: ['str_1', 'str_2', ..., 'str_n'] }或“用户ID数组”[id-1, id-2, 'user-3', ..., 'user-n']，不填表示所有用户。") //
		, @CocField(property = "funcRule", name = "功能权限", mode = "*:N v:S c2:M e:M", gridOrder = 5, desc = "表示“用户群体”可以执行模块中的哪些功能？语法规则：['moduleID:tableID:opModes', 'm-1:t-1:op1,op2,op3', ..., 'm-i:t-i:op1,op2,op-i']，语法举例：['1:2:*', '2:*:v,e,bu,d', '*:*:e', ..., '1', ':2', '::c']。") //
		, @CocField(property = "dataRule", name = "数据权限", mode = "*:N v:S c2:E e:E", gridOrder = 6, desc = "表示“用户群体”可以操作模块中的哪些数据？语法规则：可以是“查询表达式”{field_1: [num_1, num_2, ..., num_n], field_2: singleValue, ..., field_n: ['str_1', 'str_2', ..., 'str_n'] }或“数据ID数组”[id-1, id-2, id-3, ..., id-n]，不填表示所有数据。") //
		, @CocField(property = "desc", name = "权限描述", mode = "*:N v:S c:M c2:M e:M", gridOrder = 7, desc = "简要描述该权限被授予哪些人，目的和用途是什么？") //

		// COCIT V1/DEMSY V2 公用权限状态
		, @CocField(name = "权限状态", property = "disabled", mode = "*:N v:S c:E c2:E e:E", options = "0:启用,1:停用", gridOrder = 22) //
		, @CocField(name = "有效期自", property = "expiredFrom", mode = "*:N v:S c:E c2:E e:E", pattern = "yyyy-MM-dd HH:mm:ss", gridOrder = 12) //
		, @CocField(name = "有效期至", property = "expiredTo", mode = "*:N v:S c:E c2:E e:E", pattern = "yyyy-MM-dd HH:mm:ss", gridOrder = 13) //
		, @CocField(name = "创建时间", property = "created", mode = "*:N v:S", pattern = "yyyy-MM-dd HH:mm:ss", gridOrder = 8) //
		, @CocField(name = "创建者帐号", property = "createdBy", mode = "*:N v:S", gridOrder = 21) //
//
}),// end: CocGroup
		@CocGroup(name = "授权规则", code = "other"//
		, fields = {
				// DEMSY V2 授权规则：不再使用
				@CocField(property = "users", mode = "*:N c:E") //
				, @CocField(property = "users.module", name = "用户类型", isTransient = true, mode = "*:N") //
				, @CocField(property = "users.rules2", name = "用户规则", isTransient = true, mode = "*:N") //
				, @CocField(property = "datas", mode = "*:N c:E") //
				, @CocField(property = "datas.module", name = "功能模块", isTransient = true, mode = "*:N") //
				, @CocField(property = "datas.rules2", name = "数据权限", isTransient = true, mode = "*:N") //
				, @CocField(property = "actions", name = "模块操作", mode = "*:N") //
		}), // end: CocGroup
}// end: groups
)
public class Permission extends BizComponent implements IPermission, PermissionEntity {
	protected Date expiredFrom;

	protected Date expiredTo;

	protected Boolean type;

	/*
	 * COCIT版本授权表达式
	 */
	/**
	 * @see PermissionEntity#getUserType()
	 */
	@Column(length = 64)
	protected String userType;

	/**
	 * @see PermissionEntity#getUserRule()
	 */
	@Column(length = 512)
	protected String userRule;

	/**
	 * @see PermissionEntity#getFuncRule()
	 */
	@Column(length = 512)
	protected String funcRule;

	/**
	 * @see PermissionEntity#getDataRule()
	 */
	@Column(length = 2000)
	protected String dataRule;

	/*
	 * DEMSY版本授权表达式
	 */
	/**
	 * 以JSON文本存储并转换成对象
	 * 
	 * @deprecated COC平台不再使用
	 */
	@CocField(name = "用户", mode = "*:N c:E"//
	, children = {
			//
			@CocField2(property = "moduleGuid", name = "用户类型", options = "['refSystemExtends eq BaseUser']", order = 1)//
			, @CocField2(property = "rules", name = "用户名称", order = 2) //
	})
	protected Dataset users;

	/**
	 * 以JSON文本存储并转换成对象
	 * 
	 * @deprecated COC平台不再使用
	 */
	@CocField(name = "模块", mode = "*:N c:E"//
	, children = {
			//
			@CocField2(property = "moduleGuid", name = "功能模块", order = 1)//
			, @CocField2(property = "rules", name = "数据权限", order = 2) //
	})
	protected Dataset datas;

	/**
	 * 授权操作
	 * 
	 * @deprecated COC平台不再使用
	 */
	protected String actions;

	//
	// public String getInfo() {
	// StringBuffer sb = new StringBuffer();
	//
	// if (users != null) {
	// if (!Str.isEmpty(users.getModuleGuid()))
	// sb.append(users.getModule());
	//
	// if (!Str.isEmpty(users.getRules2()))
	// sb.append("(").append(users.getRules2()).append(")");
	// }
	// if (type != null && !type)
	// sb.append("【禁止操作】");
	// else
	// sb.append("【允许操作】");
	// if (datas != null) {
	// if (!Str.isEmpty(datas.getModuleGuid())) {
	// sb.append("功能模块(").append(datas.getModule()).append(")");
	// }
	//
	// if (!Str.isEmpty(datas.getRules2()))
	// sb.append("数据(").append(datas.getRules2()).append(")");
	// }
	//
	// return sb.toString();
	// }

	public Date getExpiredFrom() {
		return expiredFrom;
	}

	public Date getExpiredTo() {
		return expiredTo;
	}

	public boolean isDenied() {
		return type != null && type == false;
	}

	public Boolean getType() {
		return type;
	}

	public void setExpiredFrom(Date expiredFrom) {
		this.expiredFrom = expiredFrom;
	}

	public void setExpiredTo(Date expiredTo) {
		this.expiredTo = expiredTo;
	}

	public void setType(Boolean denied) {
		this.type = denied;
	}

	public Dataset getUsers() {
		return users;
	}

	public Dataset getDatas() {
		return datas;
	}

	public void setUsers(Dataset users) {
		this.users = users;
	}

	public void setDatas(Dataset datas) {
		this.datas = datas;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getUserType() {
		if (userType == null && users != null) {
			String moduleGUID = users.getModuleGuid();
			if (!StringUtil.isNil(moduleGUID)) {
				Module module = (Module) Demsy.moduleEngine.getModule(moduleGUID);
				if (module != null) {
					SFTSystem sys = module.getRefSystem();
					userType = sys.getEntityGuid();
				}

			}
		}
		return userType;
	}

	public void setUserType(String userTable) {
		this.userType = userTable;
	}

	public String getUserRule() {
		if (userRule == null && users != null) {
			userRule = users.getRules();
		}
		return userRule;
	}

	public void setUserRule(String userFilter) {
		this.userRule = userFilter;
	}

	public String getFuncRule() {
		if (funcRule == null && datas != null) {
			funcRule = datas.getModuleGuid();
		}
		return funcRule;
	}

	public void setFuncRule(String opFilter) {
		this.funcRule = opFilter;
	}

	public String getDataRule() {
		if (dataRule == null && datas != null) {
			dataRule = datas.getRules();
		}
		return dataRule;
	}

	public void setDataRule(String dataFilter) {
		this.dataRule = dataFilter;
	}

}
