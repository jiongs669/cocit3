package com.kmjsoft.cocit.entity.impl.definition;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_UDF_CONSOLE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_CATALOG;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_BZUDF_SYSTEM;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_EDIT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmjsoft.cocit.actionplugin.system.SystemPlugins;
import com.kmjsoft.cocit.entity.NamedEntity;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "系统自定义", pathPrefix = "/coc", GUID = BIZSYS_BZUDF_SYSTEM, catalog = BIZCATA_UDF_CONSOLE, SN = ORDER_BZUDF_SYSTEM, isBuildin = true//
, actions = {
//
		@CocAction(name = "定制系统", type = 9101, mode = "bc", targetWindow = "_blank", pageTemplate = "/system/AddSystem.jsp", plugin = SystemPlugins.AddSystems.class)//
		, @CocAction(name = "添加", type = 101, mode = "b", plugin = SystemPlugins.AddSystem.class)//
		, @CocAction(name = "编辑", type = TYPE_BZFORM_EDIT, mode = "e", plugin = SystemPlugins.EditSystem.class) //
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = {
//
		@CocColumn(name = "系统名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "系统编号", propName = "code") //
		, @CocColumn(name = "路径前缀", propName = "pathPrefix", gridField = true) //
		, @CocColumn(name = "显示顺序", propName = "orderby") //
		, @CocColumn(name = "系统分类", propName = "catalog", groupBy = true, fkEntity = BIZSYS_BZUDF_CATALOG) //
}), @CocGroup(name = "扩展信息", GUID = "ext"//
, fields = {
//
		@CocColumn(name = "窗体模版", propName = "template", gridField = false) //
		, @CocColumn(name = "映射实体类", propName = "mappingClass", gridField = false, mode = "*:P") //
		, @CocColumn(name = "实体类扩展", propName = "extendClass", gridField = false) //
		, @CocColumn(name = "映射数据表", propName = "mappingTable", gridField = false, mode = "*:P") //
		, @CocColumn(name = "排序表达式", propName = "sortExpr") //
		, @CocColumn(name = "系统描述", propName = "desc") //
		, @CocColumn(propName = "layout") //
		, @CocColumn(name = "停用状态", propName = "disabled", isDimension = true, options = "1:停用,0:启用") //
		, @CocColumn(name = "内置状态", propName = "buildin", isDimension = true, mode = "*:N") //
		, @CocColumn(name = "父系统", propName = "parent", isDimension = true, mode = "*:N") //
		, @CocColumn(name = "实体GUID", propName = "entityGuid", mode = "*:N v:P") //
		, @CocColumn(name = "创建时间", propName = "created", mode = "*:N v:P") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "*:N v:P") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:N v:P") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:N v:P") //
}) }// end groups
)
public class EntityDefinition extends NamedEntity implements IEntityDefinition {

	@ManyToOne
	private String entityCatalogGuid;

	@Column(length = 512)
	@CocColumn(name = "排序表达式", desc = "【grid:updated desc, tree:name asc】表示在GRID中默认按updated倒排序，在导航树中按name正排序。")
	private String dataSortExpr;

	private String dataTableName;

	private String entityClassName;

	private byte uiType;

	private String uiTemplate;

	private String pathPrefix;

	public String getEntityCatalogGuid() {
		return entityCatalogGuid;
	}

	public void setEntityCatalogGuid(String entityCatalogGuid) {
		this.entityCatalogGuid = entityCatalogGuid;
	}

	public String getDataSortExpr() {
		return dataSortExpr;
	}

	public void setDataSortExpr(String dataSortExpr) {
		this.dataSortExpr = dataSortExpr;
	}

	public String getDataTableName() {
		return dataTableName;
	}

	public void setDataTableName(String dataTableName) {
		this.dataTableName = dataTableName;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}

	public byte getUiType() {
		return uiType;
	}

	public void setUiType(byte uiType) {
		this.uiType = uiType;
	}

	public String getUiTemplate() {
		return uiTemplate;
	}

	public void setUiTemplate(String uiTemplate) {
		this.uiTemplate = uiTemplate;
	}

	public String getPathPrefix() {
		return pathPrefix;
	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

}
