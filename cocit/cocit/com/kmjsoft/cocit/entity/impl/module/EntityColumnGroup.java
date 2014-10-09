package com.kmjsoft.cocit.entity.impl.module;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_UDF_CONSOLE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_FIELD_GROUP;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_BZUDF_FIELD_GROUP;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmjsoft.cocit.entity.NamedEntity;
import com.kmjsoft.cocit.entity.module.IEntityColumnGroup;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "字段分组", GUID = BIZSYS_BZUDF_FIELD_GROUP, catalog = BIZCATA_UDF_CONSOLE, SN = ORDER_BZUDF_FIELD_GROUP, isBuildin = true//
, actions = { @CocAction(name = "新增分组", type = TYPE_BZFORM_NEW, mode = "c")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = { @CocColumn(name = "分组名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "分组编号", propName = "code") //
		, @CocColumn(name = "所属系统", propName = "system", fkEntity = BIZSYS_BZUDF_SYSTEM, isFkChild = true, mode = "c:M *:S") //
		, @CocColumn(name = "人工顺序", propName = "orderby") //
		, @CocColumn(name = "字段模式", propName = "mode") //
		, @CocColumn(name = "表单列数", propName = "columns") //
		, @CocColumn(name = "停止状态", propName = "disabled", isDimension = true, options = "1:停用,0:启用") //
		, @CocColumn(name = "分组描述", propName = "desc") //
		, @CocColumn(name = "创建时间", propName = "created", mode = "*:P") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "*:P") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:P") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class EntityColumnGroup extends NamedEntity implements IEntityColumnGroup {

	@ManyToOne
	protected String moduleGuid;

	@Column(length = 255)
	protected String mode;

	public String getModuleGuid() {
		return moduleGuid;
	}

	public void setModuleGuid(String moduleGuid) {
		this.moduleGuid = moduleGuid;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
