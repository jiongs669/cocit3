package com.kmjsoft.cocit.entity.impl.definition;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_UDF_CONSOLE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_CATALOG;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_BZUDF_CATALOG;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Entity;

import com.kmjsoft.cocit.entity.TreeEntity;
import com.kmjsoft.cocit.entity.definition.IEntityCatalog;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "系统分类", GUID = BIZSYS_BZUDF_CATALOG, catalog = BIZCATA_UDF_CONSOLE, SN = ORDER_BZUDF_CATALOG, isBuildin = true//
, actions = { @CocAction(name = "新增分类", type = TYPE_BZFORM_NEW, mode = "c", pluginName = "com.kmetop.demsy.plugins.biz.CreateBizCatalog")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = { @CocColumn(name = "分类名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "分类编号", propName = "code") //
		, @CocColumn(name = "上级分类", propName = "parent", fkEntity = BIZSYS_BZUDF_CATALOG) //
		, @CocColumn(name = "上级分类", propName = "parent.name", mode = "*:N") //
		, @CocColumn(name = "人工顺序", propName = "orderby") //
		, @CocColumn(name = "分类描述", propName = "desc") //
		, @CocColumn(name = "内置状态", propName = "buildin", isDimension = true, mode = "*:N") //
		, @CocColumn(name = "创建时间", propName = "created", mode = "*:P") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "*:P") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:P") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:P") //
}) }// end groups
, jsonData = "BizCatalog.data.js"//
)
public class EntityCatalog extends TreeEntity implements IEntityCatalog {
}
