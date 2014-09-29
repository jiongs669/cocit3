package com.kmetop.demsy.comlib.impl.base.lib;

import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_EDIT;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZFORM_NEW;
import static com.kmetop.demsy.biz.BizConst.TYPE_BZ_DEL;
import static com.kmetop.demsy.comlib.LibConst.BIZCATA_ADMIN;
import static com.kmetop.demsy.comlib.LibConst.BIZSYS_ADMIN_CONFIG;
import static com.kmetop.demsy.comlib.LibConst.ORDER_SYSADMIN_CONFIG;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;
import com.kmetop.demsy.comlib.entity.ISoftConfig;
import com.kmetop.demsy.comlib.impl.BizComponent;
import com.kmetop.demsy.lang.Str;

@Entity
@CocTable(name = "系统参数设置", code = BIZSYS_ADMIN_CONFIG, catalog = BIZCATA_ADMIN, orderby = ORDER_SYSADMIN_CONFIG, buildin = true//
, actions = {
//
		@CocOperation(name = "新增", typeCode = TYPE_BZFORM_NEW, mode = "c", pluginName = "com.kmetop.demsy.plugins.lib.ClearSoftConfigCache")//
		, @CocOperation(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", pluginName = "com.kmetop.demsy.plugins.lib.ClearSoftConfigCache") //
		, @CocOperation(name = "删除", typeCode = TYPE_BZ_DEL, mode = "d", pluginName = "com.kmetop.demsy.plugins.lib.ClearSoftConfigCache") //
		, @CocOperation(name = "查看", typeCode = TYPE_BZFORM_EDIT, mode = "v") //
}//
, groups = { @CocGroup(name = "基本信息", code = "basic"//
, fields = {
//
		@CocField(property = "parent", mode = "c:E e:E", gridOrder = 5) //
		, @CocField(name = "配置项名称", property = "name", mode = "c:M e:M", gridOrder = 1)//
		, @CocField(name = "配置项编号", property = "code", mode = "c:M e:M", gridOrder = 2) //
		, @CocField(name = "配置项内容", property = "value", mode = "c:M e:M", privacy = true, gridOrder = 3) //
		, @CocField(name = "配置项说明", property = "desc", gridOrder = 4) //
// , @CocField(name = "更新时间", property = "updated", mode = "*:P") //
// , @CocField(name = "创建时间", property = "created", mode = "*:P") //
// , @CocField(name = "更新帐号", property = "updatedBy", mode = "*:P") //
// , @CocField(name = "创建帐号", property = "createdBy", mode = "*:P") //
}) }// end groups
, jsonData = "SoftConfig.data.js"//
)
public class SoftConfig extends BizComponent implements ISoftConfig {
	@Column(length = 256)
	private String value;

	@ManyToOne
	@CocField(name = "配置项分组")
	private SoftConfig parent;

	@OneToMany(mappedBy = "parent")
	protected List<SoftConfig> children;

	public List<SoftConfig> getChildren() {
		return children;
	}

	public void setChildren(List<SoftConfig> children) {
		this.children = children;
	}

	public SoftConfig getParent() {
		return parent;
	}

	public void setParent(SoftConfig parent) {
		this.parent = parent;
	}

	public String getValue() {
		if (Str.isEmpty(value)) {
			return super.get("value");
		}
		return value;
	}

	public void setValue(String value) {
		if (value == null)
			value = "";

		if (value.length() > 127) {
			super.set("value", value);
			this.value = null;
		} else {
			super.set("value", null);
			this.value = value;
		}
	}

	@Override
	public String getKey() {
		return getCode();
	}
}
