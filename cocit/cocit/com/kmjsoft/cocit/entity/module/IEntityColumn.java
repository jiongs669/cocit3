package com.kmjsoft.cocit.entity.module;

import com.kmjsoft.cocit.entity.INamedEntity;

/**
 * 实体字段：用于描述“实体属性、数据表字段、字段UI”等信息。
 * 
 * @author yongshan.ji
 */
public interface IEntityColumn extends INamedEntity {
	public static final byte DATA_TYPE_STRING = 0;

	public static final byte DATA_TYPE_TEXT = 0;

	public static final byte DATA_TYPE_INTEGER = 0;

	public static final byte DATA_TYPE_NUMBER = 0;

	public static final byte DATA_TYPE_BOOLEAN = 0;

	public static final byte DATA_TYPE_DATE = 0;

	/**
	 * 外键字段
	 */
	public static final byte DATA_TYPE_FK = 0;

	/**
	 * 数据冗余
	 */
	public static final byte DATA_TYPE_FK_REDUNDANT = 0;

	public static final byte DATA_TYPE_UPLOAD = 0;

	/**
	 * 实体定义：逻辑外键，关联到“{@link IEntityModule#getDataGuid()}”字段。
	 * <p>
	 * 用于描述该字段属于哪个实体（实体类、实体表）？
	 * 
	 * @return
	 */
	String getModuleGuid();

	void setModuleGuid(String moduleGuid);

	/**
	 * 字段分组：逻辑外键，关联到“{@link IEntityColumnGroup#getDataGuid()}”字段。
	 * <p>
	 * 用于描述该字段属于哪个分组？注：字段和分组必须属于同一个实体。
	 * 
	 * @return
	 */
	String getGroupGuid();

	void setGroupGuid(String fieldGroupGuid);

	/**
	 * 字段数据类型：可选值参见“{@link IEntityColumn#DATA_TYPE_XXXXX}”值。
	 * 
	 * @return
	 */
	byte getDataType();

	void setDataType(byte dataType);

	// -----------------------------------
	// “外键类型字段”描述：
	// -----------------------------------

	/**
	 * 外键实体：逻辑外键，关联到“{@link IEntityModule#getDataGuid()}”字段。
	 * <p>
	 * 
	 * 
	 * @return
	 */
	String getFkModuleGuid();

	void setFkModuleGuid(String fkModuleGuid);

	/**
	 * 此“外键字段”关联到上述实体的哪个字段？不允许直接关联到物理主键字段，只允许关联到逻辑主键字段或其他数据字段。
	 * 
	 * @return
	 */
	String getFkColumnGuid();

	void setFkColumnGuid(String columnGuid);

	/**
	 * 检查外键字段是否支持多值
	 * 
	 * @return
	 */
	boolean isFkMultipleValue();

	//
	// String getRefrenceFields();
	//
	// IEntityField getParent();

	/**
	 * 字段标度或长度：用于文本类型表示字段长度，用于数字类型表示数字的整数部分长度。
	 * 
	 * @return
	 */
	Integer getScale();

	void setScale(Integer scale);

	/**
	 * 字段精度：用于数字类型表示数字的小数部分长度。
	 * 
	 * @return
	 */
	Integer getPrecision();

	void setPrecision(Integer precision);

	/**
	 * <b>字段显示模式(mode)：</b>用空格分隔，与子系统数据操作中指定的动作模式组合使用。
	 * <p>
	 * 格式：[操作模式:显示模式]
	 * <ul>
	 * <li>M: Must 必需的</li>
	 * <li>E: Edit 可编辑的 (即可读写)</li>
	 * <li>I: Inspect 检查（带有一个隐藏字段存放其值）</li>
	 * <li>S: Show 显示（但不带隐藏字段）</li>
	 * <li>N: None 不显示</li>
	 * <li>P: Present 如果该字段有值就显示，否则如果没有值就不显示该字段</li>
	 * <li>H: Hidden 隐藏 (不显示，但有一个隐藏框存在)</li>
	 * <li>R: Read only 只读</li>
	 * <li>D: Disable 禁用</li>
	 * </ul>
	 * <p>
	 * <b> 字段显示模式举例说明(mode)： </b>
	 * <ul>
	 * <li>v:I——查看数据时，该字段处于检查模式</li>
	 * <li>e:E——编辑数据时，字段可编辑</li>
	 * <li>bu:N——批量修改数据时，字段不可见</li>
	 * </ul>
	 */
	String getMode();

	void setMode(String mode);

	/**
	 * 获取“数值类型”字段值计量单位选项，以便录入、编辑数据过程中生成计量单位下拉选项。
	 * <p>
	 * 计量单位选项可以是计量单位类别编号，如距离“distance”；或如下个格式的文本：{"m":"米", "km":"千米"}
	 * 
	 * @return
	 */
	String getUomOptions();

	void setUomOptions(String uomOptions);

	/**
	 * 获取“数值类型”字段计量单位，以便录入编辑数据过程中固定计量单位
	 * <p>
	 * 如果计量单位选项是计量单位类别，则该值可以是指定类别下的计量单位编号。
	 * <p>
	 * 如计量单位使用的是类别编号“distance”(距离)，则计量单位可以限制为“m”(米)。
	 * <p>
	 * 
	 * 
	 * @return
	 */
	String getUomOption();

	void setUomOption(String uomOption);

	/**
	 * 获取“字典类型”字段值选项，以便录入、编辑数据过程中生成单选或多选选项。
	 * <p>
	 * 字段值选项可以是字典类别编号，或如下个格式的文本：{"m":"男", "w":"女"}
	 * 
	 * @return
	 */
	String getOptions();

	void setOptions(String options);

	/**
	 * 获取“文本类型”、“数值类型”类型字段的掩码，即数据格式必须满足正则表达式掩码规则。
	 * <p>
	 * 同时用来校验UI上录入的数据
	 * <p>
	 * 如：邮箱、电话号码、邮政编码等
	 * 
	 * @return
	 */
	String getRegexpMask();

	void setRegexpMask(String regexpMask);

	/**
	 * 默认值表达式
	 * 
	 * @return
	 */
	String getDefaultValue();

	void setDefaultValue(String defaultValue);

	/**
	 * 获取“数值类型”、“日期”、“日期时间”类型字段的显示格式。
	 * <p>
	 * 日期类型如：yyyy-MM-dd(2010-12-31)；(yyyyMMdd)20101231；(yyyy-MM-dd hh:mm:ss)2010-12-31 12:30:25
	 * <p>
	 * 数字类型如：###,###.00
	 * 
	 * @return
	 */
	String getPattern();

	void setPattern(String pattern);

	/**
	 * 获取动态生成的业务实体属性名。
	 * 
	 * @return
	 */
	String getPropName();

	void setPropName(String prop);

	/**
	 * 获取上传类型：上传字段支持的文件类型
	 * 
	 * @return
	 */
	String getUploadType();

	void setUploadType(String uploadType);

	/**
	 * 判断字段是否为临时字段？临时字段对应的数据不会被持久化到数据库中。
	 * 
	 * @return
	 */
	boolean isTransientField();

	void setTransientField(boolean isTransientField);

	/**
	 * 判断该字段是否允许在列表中显示？
	 * 
	 * @return
	 */
	boolean isGridField();

	void setGridField(boolean gridField);

	int getGridOrder();

	void setGridOrder(int gridOrder);

	int getGridWidth();

	void setGridWidth(int gridWidth);

	/**
	 * 判断“系统引用”类型的字段是否允许将该系统映射成引用系统的子系统？
	 * 
	 * @return
	 */
	boolean isMappingToMaster();

	void setMappingToMaster(boolean isMappingToMaster);

	/**
	 * 判断“系统引用”类型的字段是否以Combobox方式产生字段UI？
	 * 
	 * @return
	 */
	byte getUiType();

	void setUiType(byte type);

	String getUiTemplate();

	void setUiTemplate(String template);

	/**
	 * 判断是否以密码方式录入字段值
	 * 
	 * @return
	 */
	boolean isPassword();

	void setPassword(boolean isPassword);

	// /**
	// * 判断“字段引用”类型的字段是否支持数据冗余？
	// *
	// * @return
	// */
	// boolean isSysRedundancy();

	/**
	 * 检查是否禁用“数据维度”，以便快速过滤（挖掘）数据。
	 * 
	 * @return
	 */
	boolean isDisabledDimension();

	void setDisabledDimension(boolean disabledDimension);

	/**
	 * 级联模式：即哪些字段发生变化后，该字段将以何种模式显示。
	 * <p>
	 * 格式: {上级字段值}:{mode}
	 * 
	 * @return
	 */
	String getCascadeMode();

	void setCascadeMode(String cascadeMode);

	// boolean isGroupBy();

}
