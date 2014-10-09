package com.kmjsoft.cocit.entity.impl.security;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_ADMIN;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_ADMIN_MODULE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_DATASOURCE;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_SYSADMIN_MODULE;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmjsoft.cocit.entity.TreeEntity;
import com.kmjsoft.cocit.entity.security.IFunMenu;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "功能模块设置", GUID = BIZSYS_ADMIN_MODULE, catalog = BIZCATA_ADMIN, SN = ORDER_SYSADMIN_MODULE, isBuildin = true//
, actions = { @CocAction(name = "新增模块", type = TYPE_BZFORM_NEW, mode = "c", disabled = true)//
		, @CocAction(name = "批量修改", type = TYPE_BZFORM_EDIT_N, mode = "bu")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = {
//
		@CocColumn(name = "模块名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "模块编号", propName = "code") //
		, @CocColumn(name = "路径前缀", propName = "pathPrefix") //
		, @CocColumn(name = "模块类型", propName = "type", isDimension = true, mode = "c:M e:M", options = "[" + //
				"{value:'90',text:'文件夹'}\n" + //
				",{value:'1',text:'静态模块'}\n" + //
				",{value:'2',text:'业务模块'}\n" + //
				// ",{value:'3',text:'网站栏目'}\n" + //
				// ",{value:'4',text:'报表模块'}\n" + //
				// ",{value:'5',text:'流程模块'}\n" + //
				",{value:'6',text:'动态模块'}\n" + //
				"]") //
		, @CocColumn(name = "软件名称", propName = "softName", mode = "*:P") //
		, @CocColumn(name = "绑定组件", propName = "refName", mode = "*:P") //
		, @CocColumn(name = "停用状态", propName = "disabled", isDimension = true, mode = "bu:E", options = "1:停用,0:启用", gridField = false) //
		, @CocColumn(name = "隐藏状态", propName = "hidden", isDimension = true, mode = "bu:E", options = "1:隐藏,0:显示", gridField = false) //
}),//
		@CocGroup(name = "模块属性设置", GUID = "properties"//
		, fields = {
				//
				@CocColumn(name = "上级模块", propName = "parent", fkEntity = BIZSYS_ADMIN_MODULE, mode = "bu:E", cascadeMode = "type:1,2,3,4,5,6:M type:90:E", options = "['type eq 90']"), //
				@CocColumn(name = "业务系统", propName = "refSystem", fkEntity = BIZSYS_BZUDF_SYSTEM, gridField = false, isDimension = true, cascadeMode = "type:2:M"), //
				@CocColumn(name = "模块数据源", propName = "dataSource", fkEntity = BIZSYS_DEMSY_DATASOURCE, cascadeMode = "type:2:E", gridField = false),//
				@CocColumn(name = "模块路径", propName = "path", cascadeMode = "type:1:E", gridField = false) //
		}) //
		, @CocGroup(name = "其他属性设置", GUID = "other"//
		, fields = {
				//
				@CocColumn(name = "模块操作", propName = "refActions", gridField = false) //
				, @CocColumn(name = "模块徽标", propName = "logo", uploadType = "*.jpg;*.gif;*.png", gridField = false) //
				, @CocColumn(name = "模块图片", propName = "image", uploadType = "*.jpg;*.gif;*.png", gridField = false) //
				, @CocColumn(name = "窗体模版", propName = "template", desc = "用于展现业务操作界面", gridField = false) //
				, @CocColumn(name = "模块描述", propName = "desc", gridField = false) //
				, @CocColumn(name = "内置状态", propName = "buildin", isDimension = true, mode = "*:N", gridField = false) //
				, @CocColumn(propName = "refSystemExtends") //
				, @CocColumn(propName = "refSystemClass") //
				, @CocColumn(name = "创建时间", propName = "created", mode = "*:P", pattern = "yyyy-MM-dd HH:mm:ss") //
				, @CocColumn(name = "更新时间", propName = "updated", mode = "*:P", pattern = "yyyy-MM-dd HH:mm:ss") //
				, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:P") //
				, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:P") //
		}) //

}// end groups
, jsonData = "Module.data.js"// end root
)
public class FunMenu extends TreeEntity implements IFunMenu {

	@ManyToOne
	protected String systemGuid;

	@ManyToOne
	protected String dataSourceGuid;

	protected String path;

	protected String logo;

	protected String image;

	protected int type;

	protected String referencedGuid;

	protected String referencedName;

	protected String referencedActionsRule;

	@Column(length = 256)
	protected String uiTemplate;

	@Column(length = 10)
	protected String pathPrefix;

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getReferencedGuid() {
		return referencedGuid;
	}

	public void setReferencedGuid(String referencedGuid) {
		this.referencedGuid = referencedGuid;
	}

	public String getReferencedName() {
		return referencedName;
	}

	public void setReferencedName(String referencedName) {
		this.referencedName = referencedName;
	}

	public String getReferencedActionsRule() {
		return referencedActionsRule;
	}

	public void setReferencedActionsRule(String referencedActionsRule) {
		this.referencedActionsRule = referencedActionsRule;
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
