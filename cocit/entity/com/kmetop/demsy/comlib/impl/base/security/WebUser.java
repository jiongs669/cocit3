package com.kmetop.demsy.comlib.impl.base.security;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_WEBUSER;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_WEBUSER;

import javax.persistence.Entity;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;
import com.kmetop.demsy.comlib.entity.base.BaseUser;
import com.kmetop.demsy.plugins.security.SaveUser;

@Entity
@CocTable(name = "网站会员管理", code = BIZSYS_ADMIN_WEBUSER, catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_WEBUSER, buildin = false//
, actions = {
//
		@CocOperation(name = "添加", typeCode = TYPE_BZFORM_NEW, mode = "c", plugin = SaveUser.class)//
		, @CocOperation(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", plugin = SaveUser.class) //
		, @CocOperation(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
// , @CocOperation(name = "添加员工帐号", typeCode = TYPE_BZFORM_NEW, mode = "c1", pluginName = "com.kmetop.demsy.plugins.security.SaveStaffUser", info = "注册成功！审核通过后方可登录。", error = "注册失败！请检查错误信息。")//
// , @CocOperation(name = "审核员工", typeCode = TYPE_BZFORM_EDIT_N, mode = "bu1") //
}//
, groups = {//
@CocGroup(name = "账户信息", code = "logininfo"//
, fields = {
//
		@CocField(name = "会员类型", property = "userType", mode = "c:E c1:E e:E e1:E v:S *:N") //
		, @CocField(name = "用户名称", property = "name", mode = "c:M c1:M e:M e1:M v:S *:N", gridOrder = 1, privacy = true)//
		, @CocField(name = "登录帐号", property = "code", mode = "c:M c1:M e:R e1:R v:S *:N", gridOrder = 2, privacy = true, desc = "只能有数字、字母、下划线组成") //
		, @CocField(name = "登录密码", property = "rawPassword", mode = "c:M c1:M e:E e1:E *:N", password = true, gridField = false, desc = "密码至少8个字符") //
		, @CocField(name = "验证密码", property = "rawPassword2", mode = "c:M c1:M e:E e1:E *:N", password = true, gridField = false, desc = "必须与登录密码相同") //
		, @CocField(name = "密码问题", property = "pwdQuestion", mode = "c:M c1:M e:M e1:M v:S *:N", gridField = false) //
		, @CocField(name = "密码答案", property = "pwdAnswer", mode = "c:M c1:M e:M e1:M v:S *:N", gridField = false) //
		, @CocField(name = "用户头像", property = "image", mode = "c:E c1:E e:E e1:E v:S", gridField = false, uploadType = "*.jpg;*.gif;*.png") //
		, @CocField(name = "个人签名", property = "desc", mode = "c:E c1:E e:E e1:E v:S *:N", gridOrder = 9) //
})
//
		, @CocGroup(name = "联系方式", code = "ContactInfo"//
		, fields = {
				//
				@CocField(property = "tel", gridOrder = 3, mode = "c:M c1:M e:M e1:M v:S *:N") //
				, @CocField(property = "qq", gridOrder = 4, mode = "c:M c1:M e:M e1:M v:S *:N") //
				, @CocField(property = "email", gridOrder = 5, mode = "c:E c1:E e:E e1:E v:S *:N") //
				, @CocField(property = "postCode", gridOrder = 6, mode = "c:E c1:E e:E e1:E v:S *:N") //
				, @CocField(property = "postAddress", gridOrder = 7, mode = "c:E c1:E e:E e1:E v:S *:N") //
				, @CocField(property = "msn", gridOrder = 8, mode = "c:E c1:E e:E e1:E v:S *:N") //
		})
		//
		, @CocGroup(name = "员工信息", code = "staffinfo"//
		, fields = {
				//
				@CocField(property = "realName", cascadeMode = "userType:1:M,userType:0:N", mode = "c1:M e1:M v:P *:N") //
				, @CocField(property = "orgName", cascadeMode = "userType:1:M,userType:0:N", mode = "c1:M e1:M v:P *:N") //
				, @CocField(property = "jobNumber", cascadeMode = "userType:1:M,userType:0:N", mode = "c1:M e1:M v:P *:N") //
		})
		//
		, @CocGroup(name = "其他信息", code = "OtherInfo"//
		, fields = {
				//
				// , @CocField(name = "登录次数", property = "loginedCount", mode = "v:S *:N")//
				@CocField(name = "账户状态", property = "disabled", mode = "bu1:E v:S *:N", options = "0:启用,1:停用") //
				, @CocField(name = "有效期自", property = "expiredFrom", mode = "c1:N e1:N", pattern = "yyyy-MM-dd HH:mm:ss") //
				, @CocField(name = "有效期至", property = "expiredTo", mode = "c1:N e1:N", pattern = "yyyy-MM-dd HH:mm:ss") //
				// , @CocField(name = "权限有效期自", property = "permissionExpiredFrom")//
				// , @CocField(name = "权限有效期至", property = "permissionExpiredTo")//
				// , @CocField(name = "最近登录地址", property = "lastedRemoteAddr", mode = "*:P")//
				// , @CocField(name = "最近登录时间", property = "lastedLoginDate", mode = "v:S *:N")//
				, @CocField(name = "注册时间", property = "created", mode = "v:S *:N", gridOrder = 10, pattern = "yyyy-MM-dd HH:mm") //
				, @CocField(name = "注册IP", property = "createdIP", mode = "v:S *:N") //
				, @CocField(name = "更新时间", property = "updated", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
				, @CocField(name = "创建帐号", property = "createdBy", mode = "v:S *:N") //
				, @CocField(name = "更新帐号", property = "updatedBy", mode = "v:S *:N") //
		})
//
}// end groups
)
public class WebUser extends BaseUser {
	@CocField(name = "会员类型", options = "0:外部会员,1:内部员工")
	private byte userType;

	/*
	 * 企业内部员工
	 */
	@CocField(name = "员工姓名", desc = "企业员工真实姓名")
	private String realName;

	@CocField(name = "员工部门", desc = "企业员工所在的部门")
	private String orgName;

	@CocField(name = "员工工号", desc = "企业员工工号")
	private String jobNumber;

	/*
	 * 联系方式
	 */
	@CocField(name = "手机号码")
	private String tel;

	@CocField(name = "QQ号码")
	private String qq;

	@CocField(name = "邮箱地址")
	private String email;

	@CocField(name = "邮政编码")
	private String postCode;

	@CocField(name = "通讯地址")
	private String postAddress;

	@CocField(name = "MSN")
	private String msn;

	public byte getUserType() {
		return userType;
	}

	public void setUserType(byte userType) {
		this.userType = userType;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPostAddress() {
		return postAddress;
	}

	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
}
