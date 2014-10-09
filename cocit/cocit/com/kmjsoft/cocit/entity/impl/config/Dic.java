package com.kmjsoft.cocit.entity.impl.config;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_BASE;

import javax.persistence.Entity;

import com.kmjsoft.cocit.entity.TreeEntity;
import com.kmjsoft.cocit.entity.config.IDic;
import com.kmjsoft.cocit.entityengine.manager.BizConst;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "字典数据维护", GUID = "Dic", catalog = BIZCATA_BASE, SN = 101, isBuildin = true//
, actions = { @CocAction(jsonData = "CommonBizAction.data.js"), //
		@CocAction(name = "设置", type = BizConst.TYPE_BZ_AUTO_MAKED_UPDATE_MENUS, mode = "set") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = {
//
		@CocColumn(propName = "category") //
		, @CocColumn(name = "名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "编号", propName = "code", mode = "c:M e:M")//
		, @CocColumn(propName = "extCode") //
		, @CocColumn(name = "描述", propName = "desc", gridField = false) //
		, @CocColumn(name = "停用状态", propName = "disabled", isDimension = true, mode = "set:E", options = "1:停用,0:启用") //
		, @CocColumn(name = "人工顺序", propName = "orderby", mode = "*:N v:P", gridField = false) //
		, @CocColumn(name = "创建时间", propName = "created", mode = "*:N v:P") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:N v:P", gridField = false) //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "*:N v:P") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:N v:P", gridField = false) //

}) //
}// end groups
)
public class Dic extends TreeEntity implements IDic {

}
