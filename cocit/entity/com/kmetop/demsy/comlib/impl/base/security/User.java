package com.kmetop.demsy.comlib.impl.base.security;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_USER;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_USER;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;
import com.kmetop.demsy.comlib.entity.base.BaseUser;
import com.kmetop.demsy.comlib.security.IAdminUser;

@Entity
@CocTable(name = "后台帐号管理", code = BIZSYS_ADMIN_USER, catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_USER, buildin = false//
, actions = { @CocOperation(name = "添加帐号", typeCode = TYPE_BZFORM_NEW, mode = "c", pluginName = "com.kmetop.demsy.plugins.security.SaveUser")//
                , @CocOperation(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", code = "basic"//
, fields = { @CocField(name = "登录帐号", property = "code", mode = "c:M *:S") //
                , @CocField(name = "账户名称", property = "name", mode = "c:M e:M")//
                , @CocField(name = "登录密码", property = "rawPassword", password = true, gridField = false, mode = "*:N c:M e:E") //
                , @CocField(name = "验证密码", property = "rawPassword2", password = true, gridField = false, mode = "*:N c:M e:E") //
}), @CocGroup(name = "其他信息", code = "other"//
, fields = { @CocField(property = "group") //
                , @CocField(property = "role") //
                , @CocField(name = "有效期自", property = "expiredFrom", pattern = "yyyy-MM-dd HH:mm:ss") //
                , @CocField(name = "有效期至", property = "expiredTo", pattern = "yyyy-MM-dd HH:mm:ss") //
                , @CocField(name = "账户停用", property = "disabled", options = "0:启用,1:停用") //
                , @CocField(name = "密码问题", property = "pwdQuestion", gridField = false) //
                , @CocField(name = "密码答案", property = "pwdAnswer", gridField = false) //
                , @CocField(name = "用户图片", property = "image", gridField = false, uploadType = "*.jpg;*.gif;*.png") //
                , @CocField(name = "用户徽标", property = "logo", gridField = false, uploadType = "*.jpg;*.gif;*.png") //
                // , @CocField(name = "权限有效期自", property = "permissionExpiredFrom") //
                // , @CocField(name = "权限有效期至", property = "permissionExpiredTo") // /
                // , @CocField(name = "最近登录地址", property = "lastedRemoteAddr", mode =
                // "*:P") //
                // , @CocField(name = "最近登录时间", property = "lastedLoginDate", mode =
                // "v:S *:N")
                // //
                , @CocField(name = "用户描述", property = "desc") //
                // , @CocField(name = "登录次数", property = "loginedCount", mode = "v:S *:N")
                // //
                , @CocField(name = "创建时间", property = "created", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
                , @CocField(name = "更新时间", property = "updated", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
                , @CocField(name = "创建帐号", property = "createdBy", mode = "v:S *:N") //
                , @CocField(name = "更新帐号", property = "updatedBy", mode = "v:S *:N") //
}) }// end groups
, jsonData = "User.data.js"//
)
public class User extends BaseUser implements IAdminUser {

	@CocField(name = "最近访问")
	private String latestUrl;

	@ManyToOne
	@CocField(name = "用户分组")
	private Group group;

	@ManyToOne
	@CocField(name = "用户角色")
	// @CocField(name = "用户角色", options = "['inner eq 0']", disabledNavi = true)
	private UserRole role;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getLatestUrl() {
		return latestUrl;
	}

	public void setLatestUrl(String latestUrl) {
		this.latestUrl = latestUrl;
	}

}
