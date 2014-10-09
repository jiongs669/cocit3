package com.kmjsoft.cocit.entity.impl.security;

import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Entity;

import com.kmjsoft.cocit.entity.EntityConst;
import com.kmjsoft.cocit.entity.NamedEntity;
import com.kmjsoft.cocit.entity.security.ISystem;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "系统管理", GUID = EntityConst.BIZSYS_DEMSY_CORP, catalog = EntityConst.BIZCATA_DEMSY_ADMIN, SN = EntityConst.ORDER_DEMSY_CORP, isBuildin = true//
, actions = { @CocAction(name = "新增企业", type = TYPE_BZFORM_NEW, mode = "c")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = { @CocColumn(name = "企业名称", propName = "name")//
		, @CocColumn(name = "企业编号", propName = "code")//
		, @CocColumn(name = "人工顺序", propName = "orderby") //
		, @CocColumn(name = "软件描述", propName = "desc") //
		, @CocColumn(name = "内置状态", propName = "buildin", mode = "*:N") //
		, @CocColumn(name = "创建时间", propName = "created", mode = "*:P") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "*:P") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:P") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class SystemEntity extends NamedEntity implements ISystem {

}
