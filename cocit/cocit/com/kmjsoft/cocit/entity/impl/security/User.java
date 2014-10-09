package com.kmjsoft.cocit.entity.impl.security;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_ADMIN;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_ADMIN_USER;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_SYSADMIN_USER;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.security.IUser;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "后台帐号管理", GUID = BIZSYS_ADMIN_USER, catalog = BIZCATA_ADMIN, SN = ORDER_SYSADMIN_USER, isBuildin = false//
, actions = { @CocAction(name = "添加帐号", type = TYPE_BZFORM_NEW, mode = "c", pluginName = "com.kmetop.demsy.plugins.security.SaveUser")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = { @CocColumn(name = "登录帐号", propName = "code", mode = "c:M *:S") //
		, @CocColumn(name = "账户名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "登录密码", propName = "rawPassword", isPassword = true, gridField = false, mode = "*:N c:M e:E") //
		, @CocColumn(name = "验证密码", propName = "rawPassword2", isPassword = true, gridField = false, mode = "*:N c:M e:E") //
}), @CocGroup(name = "其他信息", GUID = "other"//
, fields = { @CocColumn(propName = "group") //
		, @CocColumn(propName = "role") //
		, @CocColumn(name = "有效期自", propName = "expiredFrom", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocColumn(name = "有效期至", propName = "expiredTo", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocColumn(name = "账户停用", propName = "disabled", options = "0:启用,1:停用") //
		, @CocColumn(name = "密码问题", propName = "pwdQuestion", gridField = false) //
		, @CocColumn(name = "密码答案", propName = "pwdAnswer", gridField = false) //
		, @CocColumn(name = "用户图片", propName = "image", gridField = false, uploadType = "*.jpg;*.gif;*.png") //
		, @CocColumn(name = "用户徽标", propName = "logo", gridField = false, uploadType = "*.jpg;*.gif;*.png") //
		// , @CocField(name = "权限有效期自", property = "permissionExpiredFrom") //
		// , @CocField(name = "权限有效期至", property = "permissionExpiredTo") // /
		// , @CocField(name = "最近登录地址", property = "lastedRemoteAddr", mode =
		// "*:P") //
		// , @CocField(name = "最近登录时间", property = "lastedLoginDate", mode =
		// "v:S *:N")
		// //
		, @CocColumn(name = "用户描述", propName = "desc") //
		// , @CocField(name = "登录次数", property = "loginedCount", mode = "v:S *:N")
		// //
		, @CocColumn(name = "创建时间", propName = "created", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "v:S *:N", pattern = "yyyy-MM-dd HH:mm:ss") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "v:S *:N") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "v:S *:N") //
}) }// end groups
, jsonData = "User.data.js"//
)
public class User extends Principal implements IUser {

	@Column(length = 128)
	protected String password;

	@Transient
	protected String rawPassword;

	@Transient
	protected String rawPassword2;

	@Column(length = 255)
	protected String pwdQuestion;// 忘记密码

	@Column(length = 255)
	protected String pwdAnswer;

	protected String image;//

	protected String logo;//

	protected Date expiredFrom;

	protected Date expiredTo;

	protected boolean locked;

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

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return getDataGuid();
	}

	public void setUsername(String code) {
		super.setDataGuid(code);
		this.encodePwd();
	}

	public void setRawPassword(String pwd) {
		this.rawPassword = pwd;
		this.encodePwd();
	}

	public void setRawPassword2(String rawPassword2) {
		this.rawPassword2 = rawPassword2;
		this.encodePwd();
	}

	private void encodePwd() {
		if (!Str.isEmpty(dataGuid) && !Str.isEmpty(rawPassword) && !Str.isEmpty(rawPassword2)) {
			if (!rawPassword.equals(rawPassword2)) {
				throw new SecurityException("两次密码不一致!");
			}
			this.password = Demsy.security.encrypt(dataGuid, rawPassword);
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRawPassword() {
		return rawPassword;
	}

	public String getRawPassword2() {
		return rawPassword2;
	}

	public String getPwdQuestion() {
		return pwdQuestion;
	}

	public String getPwdAnswer() {
		return pwdAnswer;
	}

	public String getImage() {
		return image;
	}

	public String getLogo() {
		return logo;
	}

	public void setPwdQuestion(String pwdQuestion) {
		this.pwdQuestion = pwdQuestion;
	}

	public void setPwdAnswer(String pwdAnswer) {
		this.pwdAnswer = pwdAnswer;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
