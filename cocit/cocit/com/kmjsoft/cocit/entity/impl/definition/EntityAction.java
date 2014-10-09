package com.kmjsoft.cocit.entity.impl.definition;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_UDF_CONSOLE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_ACTION;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_LIB_ACTION;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_BZUDF_ACTION;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmjsoft.cocit.entity.TreeEntity;
import com.kmjsoft.cocit.entity.definition.IEntityAction;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

@Entity
@CocEntity(name = "业务操作", GUID = BIZSYS_BZUDF_ACTION, catalog = BIZCATA_UDF_CONSOLE, SN = ORDER_BZUDF_ACTION, isBuildin = true//
, actions = { @CocAction(name = "新增操作", type = TYPE_BZFORM_NEW, mode = "c")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { @CocGroup(name = "基本信息", GUID = "basic"//
, fields = { @CocColumn(name = "操作名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "操作编号", propName = "code") //
		, @CocColumn(name = "人工顺序", propName = "orderby") //
		, @CocColumn(name = "业务系统", propName = "system", fkEntity = BIZSYS_BZUDF_SYSTEM, isFkChild = true, mode = "c:M e:M") //
		, @CocColumn(name = "操作组件", propName = "actionLib", isDimension = true, fkEntity = BIZSYS_DEMSY_LIB_ACTION) //
		, @CocColumn(name = "模式编码", propName = "mode", desc = "用于识别执行该操作时各个字段的显示模式") //
		, @CocColumn(name = "类型代码", propName = "typeCode", desc = "该业务代码用于识别操作类型") //
		, @CocColumn(name = "业务插件", propName = "plugin", desc = "执行操作时将同时调用业务插件中的方法") //
		, @CocColumn(name = "按钮徽标", propName = "logo", uploadType = "*.jpg;*.gif;*.png") //
		, @CocColumn(name = "按钮图片", propName = "image", uploadType = "*.jpg;*.gif;*.png") //
		, @CocColumn(name = "窗体模版", propName = "template", desc = "用于展现业务操作界面") //
		, @CocColumn(name = "成功提示", propName = "info", desc = "执行业务操作成功后，弹出这里设置的提示信息") //
		, @CocColumn(name = "警告提示", propName = "warn", desc = "执行业务操作时，遇到警告则提示该信息") //
		, @CocColumn(name = "错误提示", propName = "error", desc = "执行业务操作出错时，弹出这里设置的提示信息") //
		, @CocColumn(name = "参数设置", propName = "params", desc = "参数格式: prop1=value1&prop2=value2...&propn=valuen") //
		, @CocColumn(propName = "targetUrl") //
		, @CocColumn(propName = "targetWindow") //
		, @CocColumn(name = "停用状态", propName = "disabled", isDimension = true, options = "1:停用,0:启用") //
		, @CocColumn(name = "操作描述", propName = "desc") //
		, @CocColumn(name = "创建时间", propName = "created", mode = "*:P") //
		, @CocColumn(name = "更新时间", propName = "updated", mode = "*:P") //
		, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:P") //
		, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:P") //
}) }// end groups
)
public class EntityAction extends TreeEntity implements IEntityAction {
	@ManyToOne
	private String definitionGuid;

	@Column(length = 255)
	@CocColumn(name = "链接地址")
	private String targetUrl;

	@Column(length = 16)
	@CocColumn(name = "业务窗口", isDimension = true, options = "_blank:新窗口,_self:自身窗口,_fixed:固定窗口")
	protected String targetWindow;

	protected Integer typeCode;

	@Column(length = 16)
	protected String mode;

	@Column(length = 255)
	protected String plugin;

	protected String logo;

	protected String image;

	@Column(length = 255)
	protected String params;

	private String pageTemplate;

	@Column(length = 255)
	private String info;

	@Column(length = 255)
	private String error;

	@Column(length = 255)
	private String warn;

	public Integer getTypeCode() {
		return typeCode;
	}

	public String getMode() {
		return mode;
	}

	public String getPlugin() {
		return plugin;
	}

	public String getLogo() {
		return logo;
	}

	public String getImage() {
		return image;
	}

	public void setTypeCode(int method) {
		this.typeCode = method;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPageTemplate() {
		return pageTemplate;
	}

	public String getInfo() {
		return info;
	}

	public String getError() {
		return error;
	}

	public String getWarn() {
		return warn;
	}

	public void setPageTemplate(String template) {
		this.pageTemplate = template;
	}

	public void setInfo(String successInfo) {
		this.info = successInfo;
	}

	public void setError(String errorInfo) {
		this.error = errorInfo;
	}

	public void setWarn(String warnInfo) {
		this.warn = warnInfo;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public String getTargetWindow() {
		return targetWindow;
	}

	public void setTargetWindow(String targetWindow) {
		this.targetWindow = targetWindow;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getDefinitionGuid() {
		return definitionGuid;
	}

	public void setDefinitionGuid(String definitionGuid) {
		this.definitionGuid = definitionGuid;
	}

}
