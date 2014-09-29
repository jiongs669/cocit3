package com.cocit.entity.security;

import static com.cocit.api.APIConst.BIZCATA_DEMSY_ADMIN;
import static com.cocit.api.APIConst.BIZSYS_DEMSY_CORP;
import static com.cocit.api.APIConst.BIZSYS_DEMSY_DATASOURCE;
import static com.cocit.api.APIConst.BIZSYS_DEMSY_SOFT;
import static com.cocit.api.APIConst.ORDER_DEMSY_SOFT;
import static com.cocit.biz.BizConst.TYPE_BZFORM_NEW;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.cocit.Demsy;
import com.cocit.api.entitydef.field.Upload;
import com.cocit.api.security.ITenant;
import com.cocit.entity.NamedEntity;
import com.cocit.entity.config.DataSourceConfig;
import com.cocit.lang.Str;
import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;

@Entity
@CocTable(name = "企业软件管理", code = BIZSYS_DEMSY_SOFT, catalog = BIZCATA_DEMSY_ADMIN, orderby = ORDER_DEMSY_SOFT, buildin = true//
, actions = { @CocOperation(name = "新增应用", typeCode = TYPE_BZFORM_NEW, mode = "c")//
		, @CocOperation(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", code = "basic"//
, fields = { @CocField(name = "软件名称", property = "name", mode = "c:M e:M")//
		, @CocField(name = "软件用户", property = "corp", groupBy = true, fkTable = BIZSYS_DEMSY_CORP, mode = "c:M e:M") //
		, @CocField(name = "软件域名", property = "domain") //
		, @CocField(name = "登录帐号", property = "code", mode = "c:M e:M *:S") //
		, @CocField(name = "登录密码", property = "rawPassword", password = true, gridField = false, mode = "*:N c:E e:E") //
		, @CocField(name = "验证密码", property = "rawPassword2", password = true, gridField = false, mode = "*:N c:E e:E") //
		, @CocField(name = "密码问题", property = "pwdQuestion", gridField = false) //
		, @CocField(name = "密码答案", property = "pwdAnswer", gridField = false) //
		, @CocField(name = "有效期自", property = "expiredFrom") //
		, @CocField(name = "有效期至", property = "expiredTo") //
		, @CocField(name = "软件徽标", property = "logo", uploadType = "*.jpg;*.gif;*.png") //
		, @CocField(name = "软件图片", property = "image", uploadType = "*.jpg;*.gif;*.png") //
		, @CocField(name = "软件数据源", property = "dataSource", fkTable = BIZSYS_DEMSY_DATASOURCE) //
		, @CocField(name = "软件描述", property = "desc") //
		, @CocField(name = "停用状态", property = "disabled", options = "1:停用,0:启用") //
		, @CocField(name = "锁定状态", property = "locked", options = "1:锁定,0:未锁定") //
		, @CocField(name = "人工顺序", property = "orderby") //
		, @CocField(name = "内置状态", property = "buildin", mode = "*:N") //
		, @CocField(name = "创建时间", property = "created", mode = "*:P") //
		, @CocField(name = "更新时间", property = "updated", mode = "*:P") //
		, @CocField(name = "创建帐号", property = "createdBy", mode = "*:P") //
		, @CocField(name = "更新帐号", property = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class Tenant extends NamedEntity implements ITenant {

	@ManyToOne
	private SoftEnv corp;// 软件所属的企业

	@ManyToOne
	private DataSourceConfig dataSource;

	@Column(length = 64)
	private String domain;

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

	private void encodePwd() {
		if (!Str.isEmpty(code) && !Str.isEmpty(rawPassword) && !Str.isEmpty(rawPassword2)) {
			if (!rawPassword.equals(rawPassword2)) {
				throw new SecurityException("两次密码不一致!");
			}
			this.password = Demsy.security.encrypt(code, rawPassword);
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

	public SoftEnv getSoftEnv() {
		return corp;
	}

	public void setCorp(SoftEnv enterprise) {
		this.corp = enterprise;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public DataSourceConfig getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSourceConfig dataSource) {
		this.dataSource = dataSource;
	}
}
