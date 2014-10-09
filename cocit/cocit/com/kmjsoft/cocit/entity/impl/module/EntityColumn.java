package com.kmjsoft.cocit.entity.impl.module;

import static com.kmjsoft.cocit.entity.EntityConst.BIZCATA_UDF_CONSOLE;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_FIELD;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_FIELD_GROUP;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_BZUDF_SYSTEM;
import static com.kmjsoft.cocit.entity.EntityConst.BIZSYS_DEMSY_LIB_FIELD;
import static com.kmjsoft.cocit.entity.EntityConst.ORDER_BZUDF_FIELD;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_EDIT_N;
import static com.kmjsoft.cocit.entityengine.manager.BizConst.TYPE_BZFORM_NEW;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.kmjsoft.cocit.entity.NamedEntity;
import com.kmjsoft.cocit.entity.module.IEntityColumn;
import com.kmjsoft.cocit.orm.annotation.CocColumn;
import com.kmjsoft.cocit.orm.annotation.CocGroup;
import com.kmjsoft.cocit.orm.annotation.CocAction;
import com.kmjsoft.cocit.orm.annotation.CocEntity;

/**
 * 表单字段显示模式
 * <p>
 * 字段显示模式：用空格分隔，与子系统数据操作中指定的动作模式组合使用。
 * <p>
 * M: Must 必需的
 * <p>
 * E: Edit 可编辑的 (即可读写)
 * <p>
 * I: Inspect 检查（带有一个隐藏字段存放其值）
 * <p>
 * S: Show 显示（但不带隐藏字段）
 * <p>
 * N: None 不显示
 * <p>
 * P: Present 如果该字段有值就显示，否则如果没有值就不显示该字段
 * <p>
 * H: Hidden 隐藏 (不显示，但有一个隐藏框存在)
 * <p>
 * R: Read only 只读
 * <p>
 * D: Disable 禁用
 * <p>
 * 举例说明：
 * <p>
 * v:I——查看数据时，该字段处于检查模式
 * <p>
 * e:E——编辑数据时，字段可编辑
 * <p>
 * bu:N——批量修改数据时，字段不可见
 */
@Entity
@CocEntity(name = "业务字段", GUID = BIZSYS_BZUDF_FIELD, catalog = BIZCATA_UDF_CONSOLE, SN = ORDER_BZUDF_FIELD, isBuildin = true//
, actions = { @CocAction(name = "新增字段", type = TYPE_BZFORM_NEW, mode = "c")//
		, @CocAction(name = "批量修改", type = TYPE_BZFORM_EDIT_N, mode = "bu")//
		, @CocAction(jsonData = "CommonBizAction.data.js") //
}//
, groups = { //
@CocGroup(name = "基本信息", GUID = "basic"//
, fields = {
//
		@CocColumn(name = "字段名称", propName = "name", mode = "c:M e:M", gridOrder = 1, desc = "字段业务名称")//
		, @CocColumn(name = "字段编号", propName = "code", mode = "c:M e:M", gridOrder = 2, desc = "由数字、字母、下划线组成，且只能有数字、字母开头") //
		, @CocColumn(name = "字段属性", propName = "propName") //
		, @CocColumn(name = "人工顺序", propName = "orderby", gridOrder = 6) //
		, @CocColumn(name = "所属系统", propName = "system", fkEntity = BIZSYS_BZUDF_SYSTEM, isDimension = true, isFkChild = true, mode = "*:S bu:N c:M e:M") //
		, @CocColumn(name = "所属分组", propName = "dataGroup", fkEntity = BIZSYS_BZUDF_FIELD_GROUP, isDimension = true, cascadeMode = "system:*:system", mode = "bu:N c:M e:M *:S", gridOrder = 3) //
		, @CocColumn(name = "字段类型", propName = "type", fkEntity = BIZSYS_DEMSY_LIB_FIELD, isFkChild = false, isDimension = true, mode = "*:S bu:N c:M e:M", options = "['version eq 2']", gridOrder = 4) //
		, @CocColumn(name = "字段模式", propName = "mode", gridOrder = 5) //
		, @CocColumn(name = "级联模式", propName = "cascadeMode") //
		, @CocColumn(name = "GRID表头", propName = "gridField", isDimension = true, mode = "bu:E", options = "1:显示,0:不显示") //
		, @CocColumn(propName = "privacy", name = "隐私字段", options = "1:隐私,0:公开", mode = "*:S") //
})// end group
		, @CocGroup(name = "字段属性设置", GUID = "properties"//
		, fields = {
				// GRID
				@CocColumn(name = "表头顺序", propName = "gridOrder", cascadeMode = "gridField:1:E") //
				, @CocColumn(name = "表头宽度", propName = "gridWidth", cascadeMode = "gridField:1:E") //
				// 系统引用
				, @CocColumn(name = "引用系统", propName = "refrenceSystem", fkEntity = BIZSYS_BZUDF_SYSTEM, isDimension = true, cascadeMode = "type:7,System:M", mode = "*:S c:M e:M") //
				, @CocColumn(name = "引用字段", propName = "refrenceFields", mode = "*:S cE e:E") //
				, @CocColumn(name = "从属系统", propName = "mappingToMaster", isDimension = true, cascadeMode = "type:7,System:E") //
				, @CocColumn(name = "数据多选", propName = "sysMultiple", gridField = false, isDimension = true, options = "1:多选,0:单选", cascadeMode = "type:7,System:E") //
				, @CocColumn(name = "数据冗余", propName = "sysRedundancy", gridField = false, isDimension = true, cascadeMode = "type:7,System:E") // })
				// 数值
				, @CocColumn(name = "显示格式", propName = "pattern", gridField = false, cascadeMode = "type:2,5,Date,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				, @CocColumn(name = "字段精度", propName = "precision", gridField = false, cascadeMode = "type:1,2,String,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				, @CocColumn(name = "小数位数", propName = "scale", gridField = false, cascadeMode = "type:2,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				, @CocColumn(name = "密码输入", propName = "password", gridField = false, isDimension = true, cascadeMode = "type:1,String:E") //
				, @CocColumn(name = "字段校验", propName = "regexpMask", gridField = false, cascadeMode = "type:1,String:E") //
				, @CocColumn(name = "计量单位", propName = "uomOption", gridField = false, cascadeMode = "type:2,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				, @CocColumn(name = "计量单位选项", propName = "uomOptions", gridField = false, cascadeMode = "type:2,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal:E") //
				//
				, @CocColumn(name = "字典选项", propName = "options", gridField = false, cascadeMode = "type:1,2,13,String,Long,Integer,Double,Float,Byte,Short,BigInteger,BigDecimal,Boolean:E") //
				, @CocColumn(name = "文件类型", propName = "fileType", gridField = false, cascadeMode = "type:4,Upload:M") //
		}) // end groups
		, @CocGroup(name = "其他属性设置", GUID = "other"//
		, fields = {
				//
				@CocColumn(name = "停用状态", propName = "disabled", gridField = false, mode = "bu:E", options = "1:停用,0:启用") //
				, @CocColumn(name = "数据导航", propName = "disabledNavi", gridField = false, isDimension = true, options = "1:隐藏,0:显示") //
				, @CocColumn(name = "默认分组", propName = "groupBy", isDimension = true, gridField = false) //
				, @CocColumn(name = "临时字段", propName = "transientField", isDimension = true, gridField = false) //
				, @CocColumn(name = "字段描述", propName = "desc", desc = "对字段进行详细的描述，说明字段的用途、目的") //
				, @CocColumn(name = "窗体模版", propName = "uiTemplate", gridField = false) //
				, @CocColumn(name = "内置字段", propName = "buildin", isDimension = true, mode = "*:N") //
				, @CocColumn(name = "创建时间", propName = "created", mode = "*:P") //
				, @CocColumn(name = "更新时间", propName = "updated", mode = "*:P") //
				, @CocColumn(name = "创建帐号", propName = "createdBy", mode = "*:P") //
				, @CocColumn(name = "更新帐号", propName = "updatedBy", mode = "*:P") //
		}) // end groups
})
// 为避免多实体一个表的情况，现将SystemData改为AbstractSystemData，从而避免DTYPE字段。
public class EntityColumn extends NamedEntity implements IEntityColumn {

	@ManyToOne
	private String moduleGuid;

	@ManyToOne
	private String groupGuid;

	protected byte dataType;// 数据类型

	@ManyToOne
	private String fkModuleGuid;// “系统引用”类型——所引用的系统

	@ManyToOne
	private String fkColumnGuid;// “字段引用”类型——所引用的系统字段

	private boolean fkMultipleValue;

	protected Integer scale;

	protected Integer precision;

	@Column(length = 255)
	private String mode;

	private String uomOptions;

	private String uomOption;

	private String options;

	private String regexpMask;

	@Column(length = 255)
	private String defaultValue;

	private String pattern;

	private String propName;

	private String uploadType;

	private boolean transientField;

	private boolean gridField;

	private int gridOrder;

	private int gridWidth;

	private boolean mappingToMaster;

	private byte uiType;

	private String uiTemplate;

	private boolean password;

	private boolean disabledDimension;

	@Column(length = 255)
	private String cascadeMode;

	public String getModuleGuid() {
		return moduleGuid;
	}

	public void setModuleGuid(String moduleGuid) {
		this.moduleGuid = moduleGuid;
	}

	public String getGroupGuid() {
		return groupGuid;
	}

	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}

	public byte getDataType() {
		return dataType;
	}

	public void setDataType(byte dataType) {
		this.dataType = dataType;
	}

	public String getFkModuleGuid() {
		return fkModuleGuid;
	}

	public void setFkModuleGuid(String fkModuleGuid) {
		this.fkModuleGuid = fkModuleGuid;
	}

	public String getFkColumnGuid() {
		return fkColumnGuid;
	}

	public void setFkColumnGuid(String fkEntityFieldGuid) {
		this.fkColumnGuid = fkEntityFieldGuid;
	}

	public boolean isFkMultipleValue() {
		return fkMultipleValue;
	}

	public void setFkMultipleValue(boolean fkMultipleValue) {
		this.fkMultipleValue = fkMultipleValue;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUomOptions() {
		return uomOptions;
	}

	public void setUomOptions(String uomOptions) {
		this.uomOptions = uomOptions;
	}

	public String getUomOption() {
		return uomOption;
	}

	public void setUomOption(String uomOption) {
		this.uomOption = uomOption;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getRegexpMask() {
		return regexpMask;
	}

	public void setRegexpMask(String regexpMask) {
		this.regexpMask = regexpMask;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public boolean isTransientField() {
		return transientField;
	}

	public void setTransientField(boolean transientField) {
		this.transientField = transientField;
	}

	public boolean isGridField() {
		return gridField;
	}

	public void setGridField(boolean gridField) {
		this.gridField = gridField;
	}

	public int getGridOrder() {
		return gridOrder;
	}

	public void setGridOrder(int gridOrder) {
		this.gridOrder = gridOrder;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public boolean isMappingToMaster() {
		return mappingToMaster;
	}

	public void setMappingToMaster(boolean mappingToMaster) {
		this.mappingToMaster = mappingToMaster;
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

	public boolean isPassword() {
		return password;
	}

	public void setPassword(boolean password) {
		this.password = password;
	}

	public boolean isDisabledDimension() {
		return disabledDimension;
	}

	public void setDisabledDimension(boolean disabledDimension) {
		this.disabledDimension = disabledDimension;
	}

	public String getCascadeMode() {
		return cascadeMode;
	}

	public void setCascadeMode(String cascadeMode) {
		this.cascadeMode = cascadeMode;
	}
}
