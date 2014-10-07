package com.kmjsoft.cocit.entity.impl.entitydef;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_UDF_CONSOLE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_CATALOG;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_BZUDF_SYSTEM;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_EDIT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmjsoft.cocit.bizplugin.system.SystemPlugins;
import com.kmjsoft.cocit.entity.BaseNamedEntity;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.orm.annotation.CocField;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocOperation;
import com.kmjsoft.cocit.orm.annotation.CocTable;

@Entity
@CocTable(name = "系统自定义", pathPrefix = "/coc", code = BIZSYS_BZUDF_SYSTEM, catalog = BIZCATA_UDF_CONSOLE, orderby = ORDER_BZUDF_SYSTEM, buildin = true//
, actions = {
//
		@CocOperation(name = "定制系统", typeCode = 9101, mode = "bc", targetWindow = "_blank", template = "/system/AddSystem.jsp", plugin = SystemPlugins.AddSystems.class)//
		, @CocOperation(name = "添加", typeCode = 101, mode = "b", plugin = SystemPlugins.AddSystem.class)//
		, @CocOperation(name = "编辑", typeCode = TYPE_BZFORM_EDIT, mode = "e", plugin = SystemPlugins.EditSystem.class) //
		, @CocOperation(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", code = "basic"//
, fields = {
//
		@CocField(name = "系统名称", property = "name", mode = "c:M e:M")//
		, @CocField(name = "系统编号", property = "code") //
		, @CocField(name = "路径前缀", property = "pathPrefix", gridField = true) //
		, @CocField(name = "显示顺序", property = "orderby") //
		, @CocField(name = "系统分类", property = "catalog", groupBy = true, fkTable = BIZSYS_BZUDF_CATALOG) //
}), @CocGroup(name = "扩展信息", code = "ext"//
, fields = {
//
		@CocField(name = "窗体模版", property = "template", gridField = false) //
		, @CocField(name = "映射实体类", property = "mappingClass", gridField = false, mode = "*:P") //
		, @CocField(name = "实体类扩展", property = "extendClass", gridField = false) //
		, @CocField(name = "映射数据表", property = "mappingTable", gridField = false, mode = "*:P") //
		, @CocField(name = "排序表达式", property = "sortExpr") //
		, @CocField(name = "系统描述", property = "desc") //
		, @CocField(property = "layout") //
		, @CocField(name = "停用状态", property = "disabled", disabledNavi = true, options = "1:停用,0:启用") //
		, @CocField(name = "内置状态", property = "buildin", disabledNavi = true, mode = "*:N") //
		, @CocField(name = "父系统", property = "parent", disabledNavi = true, mode = "*:N") //
		, @CocField(name = "实体GUID", property = "entityGuid", mode = "*:N v:P") //
		, @CocField(name = "创建时间", property = "created", mode = "*:N v:P") //
		, @CocField(name = "更新时间", property = "updated", mode = "*:N v:P") //
		, @CocField(name = "创建帐号", property = "createdBy", mode = "*:N v:P") //
		, @CocField(name = "更新帐号", property = "updatedBy", mode = "*:N v:P") //
}) }// end groups
)
public class SFTSystem extends BaseNamedEntity implements IEntityDefinition {

	@ManyToOne
	private BizCatalog catalog;

	@ManyToOne
	protected SFTSystem parent;

	@CocField(name = "表单布局", options = "0:列表结构,1:纵向TAB结构")
	protected byte layout;

	@Column(length = 512)
	@CocField(name = "排序表达式", desc = "【grid:updated desc, tree:name asc】表示在GRID中默认按updated倒排序，在导航树中按name正排序。")
	protected String sortExpr;

	private String entityOwnerField;

	private String mappingTable;

	private String mappingClass;

	private String extendClass;

	private String template;

	private String pathPrefix;

	/*
	 * 老系统字段
	 */
	// @OneToOne
	// private SystemResource resource;// 系统对应的菜单资源

	// public SystemResource getResource() {
	// return resource;
	// }
	//
	// public void setResource(SystemResource resource) {
	// this.resource = resource;
	// }

	@Override
	public String getEntityOwnerField() {
		return entityOwnerField;
	}

	public void setEntityOwnerField(String value) {
		entityOwnerField = value;
	}

	@Override
	public String getDataTableName() {
		return mappingTable;
	}

	public void setMappingTable(String value) {
		mappingTable = value;
	}

	public String getEntityClass() {
		return mappingClass;
	}

	public void setMappingClass(String value) {
		mappingClass = value;
	}

	@Column(length = 256)
	public String getExtendClass() {
		return extendClass;
	}

	public void setExtendClass(String value) {
		extendClass = value;
	}

	public void setTemplate(String value) {
		template = value;
	}

	public String getUiTemplate() {
		return template;
	}

	public BizCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(BizCatalog catalog) {
		this.catalog = catalog;
	}

	public byte getUiType() {
		return layout;
	}

	public void setLayout(byte layout) {
		this.layout = layout;
	}

	public String getPathPrefix() {
		return pathPrefix;
	}

	public void setPathPrefix(String v) {
		pathPrefix = v;
	}

	public String getDataSortExpr() {
		return sortExpr;
	}

	public void setSortExpr(String orderFields) {
		this.sortExpr = orderFields;
	}

}
