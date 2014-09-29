package com.jiongsoft.cocit.service;

import java.util.Map;

import com.jiongsoft.cocit.entity.FieldEntity;
import com.jiongsoft.cocit.util.KeyValue;

/**
 * CoC自定义数据实体字段，也称“组件化自定义数据实体字段”、“自定义数据实体字段”、“数据实体字段”、“实体字段”、“数据字段”等。
 * 
 * 
 * <UL>
 * <LI>代表一个运行时的自定义数据字段，通常由定义在数据库中的数据实体解析而来；
 * <LI>与数据分组的关系：每个数据字段只能隶属于一个数据分组；
 * <LI>与数据表的关系：每个数据字段只能隶属于一个数据表；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public interface FieldService extends EntityService<FieldEntity> {

	/**
	 * 字符串字段：长度在255以内为字符串字段，长度超过255的为文本字段。
	 * <UL>
	 * <LI>通过{@link #getPrecision()}判断字符串的长度；
	 * </UL>
	 */
	public static final byte TYPE_STRING = 0;

	/**
	 * 数字字段：
	 * <UL>
	 * <LI>通过{@link #getPrecision()}判断整数部分的位数；
	 * <LI>通过{@link #getScale()}判断小数部分的位数；
	 * </UL>
	 */
	public static final byte TYPE_NUMBER = 1;

	/**
	 * 日期字段:
	 */
	public static final byte TYPE_DATE = 2;

	/**
	 * 文本字段：长度在255以内为字符串字段；长度超过255的为文本字段，如果未指定文本字段长度，则为不限长度，在数据库中字段类型为text。
	 */
	public static final byte TYPE_TEXT = 3;

	/**
	 * BOOL字段
	 */
	public static final byte TYPE_BOOL = 4;

	/**
	 * 上传字段
	 */
	public static final byte TYPE_UPLOAD = 5;

	/**
	 * 外键字段
	 */
	public static final byte TYPE_FK = 6;

	/**
	 * 富文本字段：这种类型的字段在界面上将显示成网页编辑器；在数据库中字段类型为text。
	 */
	public static final byte TYPE_RICH_TEXT = 7;

	/**
	 * 获取字段对应的属性名称，用于从bean中获取字段数据。
	 * <p>
	 * 对于外键字段{@link #TYPE_FK}而言，字段属性是一个嵌套属性，如：user 是外键字段的属性，关联到用户信息表；user.orgname 表示获取用户的单位名称。
	 * 
	 * @return
	 */
	String getPropName();

	/**
	 * 用在“数值类型{@link #TYPE_NUMBER}”字段中，用来表示小数点位数。
	 * 
	 * @return
	 */
	Integer getScale();

	/**
	 * 用在“数值类型{@link #TYPE_NUMBER}”和“字符串类型{@link #TYPE_STRING}”中，表示整数位数或字符串长度。
	 * 
	 * @return
	 */
	Integer getPrecision();

	/**
	 * 获取数据字段类型，以 TYPE_XXX 开头的静态变量。
	 * 
	 * @return
	 */
	byte getType();

	/**
	 * 根据操作码计算字段编辑模式：
	 * <ul>
	 * <li>M: Must 必填</li>
	 * <li>E: Edit 可编辑 (即可读写)</li>
	 * <li>I: Inspect 检查（带有一个隐藏字段存放其值）</li>
	 * <li>S: Show 显示（但不带隐藏字段）</li>
	 * <li>N: None 不显示</li>
	 * <li>P: Present 如果该字段有值就显示，否则如果没有值就不显示该字段</li>
	 * <li>H: Hidden 隐藏 (不显示，但有一个隐藏框存在)</li>
	 * <li>R: Read only 只读</li>
	 * <li>D: Disable 禁用</li>
	 * </ul>
	 */
	String getMode(String opMode);

	/**
	 * 获取字段掩码：
	 * <UL>
	 * <LI>日期类型{@link #TYPE_DATE}：如 yyyy-MM-dd(2010-12-31)；(yyyyMMdd)20101231；(yyyy-MM-dd hh:mm:ss)2010-12-31 12:30:25
	 * <LI>数字类型{@link #TYPE_NUMBER}：如 #,###.00
	 * <LI>字符串类型{@link #TYPE_STRING}：如邮箱地址(\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*;)*
	 * </UL>
	 * 
	 * @return
	 */
	String getPattern();

	/**
	 * 判断字符串字段{@link #TYPE_NUMBER}是否是一个密码字段？密码字段在UI界面上不显示明文。
	 * <P>
	 * 该方法只对字符串字段生效。
	 * 
	 * @return
	 */
	boolean isPassword();

	/**
	 * 判断是否将该字段作为数据实体对象的toString()方法的返回值？
	 * <p>
	 * 该方法通常用在“字符串{@link #TYPE_STRING}”字段中。
	 * 
	 * @return
	 */
	boolean isToString();

	/**
	 * 获取字典字段选项列表，用于生成字段UI的下拉框、多选列表、单选列表等。
	 * <p>
	 * 该方法可以用于：字符串字段{@link #TYPE_NUMBER}、数字字段{@link #TYPE_STRING}、bool字段{@link #TYPE_BOOL}、日期字段{@link #TYPE_DATE}等。
	 * 
	 * @return
	 */
	KeyValue[] getDicOptions();

	Map<Object, KeyValue> getDicOptionsMap();

	/**
	 * 判断该字段是否显示在Grid中？
	 * 
	 * @return
	 */
	boolean isGridField();

	/**
	 * 获取该字段显示在Grid中的顺序。
	 * 
	 * @return
	 */
	int getGridOrder();

	/**
	 * 获取该字段显示在Grid中的宽度。
	 * 
	 * @return
	 */
	int getGridWidth();

	/**
	 * 获取该外键字段{@link #TYPE_FK}引用到的“外键数据表”。
	 * 
	 * @return
	 */
	TableService getFkEntityTable();

	/**
	 * 判断该外键字段{@link #TYPE_FK}的“主数据表”是否作为“外键数据表”的从属表？
	 * <UL>
	 * <LI>主数据表：该字段所属的数据表；
	 * <LI>外键数据表：该字段引用的数据表；
	 * </UL>
	 * 
	 * @return <UL>
	 *         <LI>true: 表示“主数据表”将作为“外键数据表”的从属表，并显示在一主多从结构的从属Tabs中；
	 *         <LI>false: 反之则不显示；
	 *         </UL>
	 */
	boolean isChildEntity();

	/**
	 * 判断该外键字段{@link #TYPE_FK}是否是多对多的关联？
	 * <UL>
	 * <LI>主数据表：该字段所属的数据表；
	 * <LI>外键数据表：该字段引用的数据表；
	 * </UL>
	 * 
	 * @return
	 */
	boolean isManyToMany();

	/**
	 * 判断该外键字段{@link #TYPE_FK}的“主数据表”是否将“外键数据表”中的数据显示在导航树中？用于主数据Grid的快速过滤字段。
	 * <UL>
	 * <LI>主数据表：该字段所属的数据表；
	 * <LI>外键数据表：该字段引用的数据表；
	 * </UL>
	 * 
	 * @return <UL>
	 *         <LI>true: 表示“外键数据表”数据将显示在“主数据表”UI界面中左边的导航树中；
	 *         <LI>false: 反之则不显示；
	 *         </UL>
	 */
	boolean isDisabledNavi();

	/**
	 * 判断该字段是否是一个级联字段？
	 * <P>
	 * 级联字段：在UI界面编辑、添加数据时，如果该字段值发生改变，将会重新刷新整个表单。
	 * 
	 * @return
	 */
	boolean isCascading();

	/**
	 * 获取“上传字段”支持的上传文件类型。
	 * <p>
	 * 如：[".gif",".png",".jpg"]
	 * 
	 * @return
	 */
	String[] getUploadType();
	
	String getUiTemplate();

	/**
	 * 格式化字段值的显示文本。
	 * <UL>
	 * <LI>字典字段：返回字典字段的Text；
	 * <LI>数值字段：使用pattern进行格式化；
	 * <LI>日期字段：同上
	 * </UL>
	 * 
	 * @param fieldValue
	 *            字段值
	 * @return
	 */
	String format(Object fieldValue);

	/**
	 * 根据给定的字段值，获取对应的字典项。
	 * <p>
	 * 如果不是字典字段，将返回null。
	 * 
	 * @param fieldValue
	 * @return
	 */
	KeyValue getDicOption(Object fieldValue);
}
