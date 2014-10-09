package com.kmjsoft.cocit.entity.impl.security;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_DEMSY_ADMIN;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_CORP;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_DATASOURCE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_SOFT;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_DEMSY_SOFT;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.kmjsoft.cocit.entity.NamedEntity;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "企业软件管理", GUID = BIZSYS_DEMSY_SOFT, catalog = BIZCATA_DEMSY_ADMIN, SN = ORDER_DEMSY_SOFT, isBuildin = true//
, actions = { @CocAction(name = "新增应用", type = TYPE_BZFORM_NEW, mode = "c")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = { @CocColumn(name = "软件名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "软件用户", propName = "corp", groupBy = true, fkEntity = BIZSYS_DEMSY_CORP, mode = "c:M e:M") //
		, @CocColumn(name = "软件域名", propName = "domain") //
		, @CocColumn(name = "登录帐号", propName = "code", mode = "c:M e:M *:S") //
		, @CocColumn(name = "登录密码", propName = "rawPassword", isPassword = true, gridField = false, mode = "*:N c:E e:E") //
		, @CocColumn(name = "验证密码", propName = "rawPassword2", isPassword = true, gridField = false, mode = "*:N c:E e:E") //
		, @CocColumn(name = "密码问题", propName = "pwdQuestion", gridField = false) //
		, @CocColumn(name = "密码答案", propName = "pwdAnswer", gridField = false) //
		, @CocColumn(name = "有效期自", propName = "expiredFrom") //
		, @CocColumn(name = "有效期至", propName = "expiredTo") //
		, @CocColumn(name = "软件徽标", propName = "logo", uploadType = "*.jpg;*.gif;*.png") //
		, @CocColumn(name = "软件图片", propName = "image", uploadType = "*.jpg;*.gif;*.png") //
		, @CocColumn(name = "软件数据源", propName = "dataSource", fkEntity = BIZSYS_DEMSY_DATASOURCE) //
		, @CocColumn(name = "软件描述", propName = "desc") //
		, @CocColumn(name = "停用状态", propName = "disabled", options = "1:停用,0:启用") //
		, @CocColumn(name = "锁定状态", propName = "locked", options = "1:锁定,0:未锁定") //
		, @CocColumn(name = "人工顺序", propName = "orderby") //
		, @CocColumn(name = "内置状态", propName = "buildin", mode = "*:N") //
		, @CocColumn(name = "创建时间", propName = "created", mode = "*:P") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "*:P") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:P") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class Tenant extends NamedEntity implements ITenant {

	private String systemGuid;

	private String dataSourceGuid;

	protected Date expiredFrom;

	protected Date expiredTo;

	@Column(length = 64)
	private String domain;

	public String getSystemGuid() {
		return systemGuid;
	}

	public void setSystemGuid(String systemGuid) {
		this.systemGuid = systemGuid;
	}

	public String getDataSourceGuid() {
		return dataSourceGuid;
	}

	public void setDataSourceGuid(String dataSourceGuid) {
		this.dataSourceGuid = dataSourceGuid;
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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
