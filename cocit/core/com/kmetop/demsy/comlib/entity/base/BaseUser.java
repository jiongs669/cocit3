package com.kmetop.demsy.comlib.entity.base;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;
import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.biz.field.Upload;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.comlib.security.IUser;
import com.kmetop.demsy.lang.Str;

@CocTable(actions = { @CocOperation(name = "注册", typeCode = TYPE_BZFORM_NEW, mode = "c1", pluginName = "com.kmetop.demsy.plugins.security.SaveUser")//
		, @CocOperation(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "账户信息", code = "basicinfo"//
, fields = { @CocField(name = "用户名称", property = "name", mode = "c1:E c:E e:E", gridOrder = 1)//
		, @CocField(name = "登录帐号", property = "code", mode = "c1:M c:M *:S", gridOrder = 2) //
		, @CocField(name = "登录密码", property = "rawPassword", password = true, gridField = false, mode = "*:N c1:M c:M e:E") //
		, @CocField(name = "验证密码", property = "rawPassword2", password = true, gridField = false, mode = "*:N c1:M c:M e:E") //
		, @CocField(name = "密码问题", property = "pwdQuestion", gridField = false) //
		, @CocField(name = "密码答案", property = "pwdAnswer", gridField = false) //
		, @CocField(name = "用户图片", property = "image", gridField = false, uploadType = "*.jpg;*.gif;*.png") //
		, @CocField(name = "创建时间", property = "created", mode = "v:S *:N") //
		, @CocField(name = "更新时间", property = "updated", mode = "v:S *:N") //
		, @CocField(name = "创建帐号", property = "createdBy", mode = "v:S *:N") //
		, @CocField(name = "更新帐号", property = "updatedBy", mode = "v:S *:N") //
}) }// end groups
)
public abstract class BaseUser extends BizComponent implements IUser {
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

	protected Upload image;//

	protected Upload logo;//

	protected Long pwdEncoder;

	protected Boolean locked;

	protected Date expiredFrom = new Date();

	protected Date expiredTo;

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

	public boolean isLocked() {
		return locked != null && locked;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return getCode();
	}

	public Long getPwdEncoder() {
		return pwdEncoder;
	}

	public void setUsername(String code) {
		setCode(code);
	}

	public void setCode(String code) {
		super.setCode(code);
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

	public void setPwdEncoder(Long pwdEncoder) {
		this.pwdEncoder = pwdEncoder;
		this.encodePwd();
	}

	private void encodePwd() {
		if (!Str.isEmpty(code) && !Str.isEmpty(rawPassword) && !Str.isEmpty(rawPassword2)) {
			if (!rawPassword.equals(rawPassword2)) {
				throw new SecurityException("两次密码不一致!");
			}
			this.password = Demsy.security.encrypt(code, rawPassword, getPwdEncoder());
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

	public Upload getImage() {
		return image;
	}

	public Upload getLogo() {
		return logo;
	}

	public void setPwdQuestion(String pwdQuestion) {
		this.pwdQuestion = pwdQuestion;
	}

	public void setPwdAnswer(String pwdAnswer) {
		this.pwdAnswer = pwdAnswer;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public void setLogo(Upload logo) {
		this.logo = logo;
	}

}
