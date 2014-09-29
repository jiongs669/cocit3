package com.cocit.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cocit.api.entitydef.IEntityDefinition;
import com.cocit.api.entitydef.IFieldDefinition;
import com.cocit.api.entitydef.IFieldGroup;
import com.cocit.api.entitydef.IFieldDataType;
import com.cocit.api.entitydef.field.IRuntimeField;
import com.cocit.api.security.IAction;
import com.cocit.api.security.ITenant;
import com.cocit.lang.DemsyException;
import com.cocit.lang.Nodes;
import com.cocit.lang.Option;
import com.jiongsoft.cocit.orm.expr.CndExpr;

/**
 * 业务系统引擎
 * 
 * @author yongshan.ji
 */
public interface IEntityDefEngine {

	void clearCache();

	List<Class<?>> listTypes();

	IEntityDefinition setupSystemFromDB(ITenant soft, String tableName);

	List<IEntityDefinition> setupSystemFromPackage(ITenant soft) throws DemsyException;

	IEntityDefinition setupSystemFromClass(ITenant soft, Class classOfEntity) throws DemsyException;

	void parseSystemByAnnotation(Class klass, IEntityDefinition system);

	List setupFromPackage(ITenant soft);

	/**
	 * 获取业务实体扩展类
	 * 
	 * @param system
	 *            业务系统
	 * @return 扩展类全称
	 */
	public abstract String getExtendClassName(IEntityDefinition system);

	/**
	 * 从类注释中安装业务系统数据
	 * 
	 * @param soft
	 * @param type
	 * @return
	 */
	List setupFromJson(ITenant soft, Class<?> type);

	/**
	 * 安装JSON数据到业务系统中
	 * 
	 * @param soft
	 * @param type
	 *            业务系统类
	 * @param json
	 *            JSON格式的业务数据
	 * @return
	 */
	<T> List<T> setupFromJson(ITenant soft, Class<T> type, String json);

	/**
	 * 从文件夹中导入JSON数据，文件名即为类名。
	 * 
	 * @param soft
	 * @param folder
	 * @return
	 */
	int importFromJson(ITenant soft, String folder);

	/**
	 * 导出满足条件的数据到文件夹，文件格式为JSON
	 * 
	 * @param soft
	 * @param folder
	 * @param expr
	 *            条件表达式
	 * @return 导出了多少条数据
	 * @throws IOException
	 */
	int exportToJson(ITenant soft, String folder, CndExpr expr) throws IOException;

	/**
	 * 获取业务字段类型库，key为字段编码。
	 * 
	 * @return
	 */
	Map<String, IFieldDataType> getFieldTypes();

	Map<Long, IFieldDataType> getFieldTypesById();

	/**
	 * 获取业务系统列表
	 * 
	 * @return 业务系统集合
	 */
	List<? extends IEntityDefinition> getSystems(ITenant soft);

	/**
	 * 根据业务系统ID获取业务系统对象
	 * <OL>
	 * <LI>如果业务系统ID是String型，将按编号查找业务系统；
	 * <LI>如果业务系统ID是Number型，将按ID查找业务系统；
	 * </OL>
	 * 
	 * @param systemID
	 *            业务系统ID
	 * @return 业务系统
	 */
	IEntityDefinition getSystem(Long systemID);

	// IBizSystem getSystem(IDemsySoft soft, String systemCode);

	/**
	 * 获取业务系统字段分组列表
	 * 
	 * @param system
	 *            业务系统
	 * @return 字段分组的集合
	 */
	List<? extends IFieldGroup> getFieldGroups(IEntityDefinition system);

	void validateSystems(ITenant soft) throws DemsyException;

	/**
	 * 只编译业务系统，但不 copy 类文件到/WEB-INF/classes下
	 * 
	 * @param system
	 * @return
	 * @throws DemsyException
	 */
	List<String> compileSystem(IEntityDefinition system) throws DemsyException;

	/**
	 * 获取业务系统实体类
	 * 
	 * @param system
	 *            业务系统对象
	 * @return 实体类
	 * @throws DemsyException
	 *             编译业务实体类出错将抛出编译错误异常
	 */
	Class getType(IEntityDefinition system) throws DemsyException;

	Class getStaticType(String system);

	/**
	 * 获取业务系统自身引用的递归树节点属性。
	 * 
	 * @param system
	 *            业务系统
	 * @return 属性名称
	 */
	IFieldDefinition getFieldOfUnSelfTree(IEntityDefinition system);

	IFieldDefinition getFieldOfSelfTree(IEntityDefinition system);

	/**
	 * 获取系统字段：不受安全系统的限制。
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统字段列表
	 */
	List<? extends IFieldDefinition> getFields(IEntityDefinition system);

	List<? extends IFieldDefinition> getFields(IFieldGroup group);

	List<? extends IFieldDefinition> getFieldsOfEnabled(IEntityDefinition system);

	List<? extends IFieldDefinition> getFieldsOfEnabled(IFieldGroup group);

	List<? extends IFieldDefinition> getFieldsOfGrid(IEntityDefinition system, String fields);

	/**
	 * @param system
	 * @return
	 */
	List<? extends IFieldDefinition> getFieldsOfSystemFK(IEntityDefinition system, Class fkType);

	/**
	 * 获取系统外键字段：即外键引用其他业务系统
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	List<? extends IFieldDefinition> getFieldsOfSystemFK(IEntityDefinition system);

	/**
	 * 获取所有外键字段：包括外键系统引用和字典字段
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	List<? extends IFieldDefinition> getFieldsOfFK(IEntityDefinition system);

	List<? extends IFieldDefinition> getFieldsOfNavi(IEntityDefinition system);

	Map<String, IFieldDefinition> getFieldsMap(IEntityDefinition system);

	Map<String, IFieldDefinition> getFieldsMap(List<? extends IFieldDefinition> list);

	/**
	 * 获取引用了指定系统的字段
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统被哪些外间字段引用？
	 */
	List<? extends IFieldDefinition> getFieldsOfExport(IEntityDefinition system);

	List<? extends IEntityDefinition> getSystemsOfSlave(IEntityDefinition system);

	List<? extends IFieldDefinition> getFieldsOfSlave(IEntityDefinition system);

	boolean isSlave(IEntityDefinition system);

	// List<? extends IBizField> getChildren(IBizField field);

	boolean isNumber(IFieldDefinition field);

	boolean isInteger(IFieldDefinition field);

	boolean isRichText(IFieldDefinition field);

	boolean isText(IFieldDefinition field);

	boolean isDate(IFieldDefinition field);

	boolean isString(IFieldDefinition field);

	boolean isUpload(IFieldDefinition field);

	boolean isImage(IFieldDefinition field);

	boolean isMultiUpload(IFieldDefinition field);

	boolean isMultiImage(IFieldDefinition field);

	boolean isSubSystem(IFieldDefinition field);

	boolean isFakeSubSystem(IFieldDefinition field);

	boolean isBuildin(IEntityDefinition system, String prop);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	boolean isManyToOne(IFieldDefinition field);

	/**
	 * 判断字段是否为一对一的字段
	 * 
	 * @return
	 */
	abstract boolean isOneToOne(IFieldDefinition field);

	/**
	 * 判断字段是否为一对多的字段
	 * 
	 * @return
	 */
	abstract boolean isOneToMany(IFieldDefinition field);

	/**
	 * 判断字段是否为多对多的字段
	 * 
	 * @return
	 */
	abstract boolean isManyToMany(IFieldDefinition field);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	boolean isV1Dic(IFieldDefinition field);

	boolean isBoolean(IFieldDefinition field);

	boolean isEnabled(IFieldDefinition field);

	boolean isGridField(IFieldDefinition field);

	boolean isSystemFK(IFieldDefinition field);

	boolean isFieldRef(IFieldDefinition field);

	String getPropName(IFieldDefinition field);

	int getGridWidth(IFieldDefinition field);

	int getPrecision(IFieldDefinition field);

	Class getGenericType(IFieldDefinition field) throws DemsyException;

	Class getType(IFieldDefinition field) throws DemsyException;

	String getMode(IFieldDefinition field, IAction action, boolean mustPriority, String defaultMode);

	String getMode(IFieldGroup group, IAction action);

	Nodes makeNaviNodes(IEntityDefinition system, String idField, boolean removeSelfLeaf);

	Nodes makeOptionNodes(IFieldDefinition field, String mode, Object data, String idField);

	List<String> makeCascadeExpr(Object obj, IFieldDefinition field, String mode);

	Option[] getOptions(IFieldDefinition field);

	String[] getCascadeMode(IFieldDefinition field, Object data);

	int getModeValue(String mode);

	Map<String, String> getMode(IEntityDefinition system, IAction action, Object data);

	void validate(IEntityDefinition system, IAction action, Object data, Map<String, String> fieldMode) throws DemsyException;

	void loadFieldValue(Object obj, IEntityDefinition system);

	IFieldDefinition getField(Long fieldID);

	IEntityDefinition getSystem(String systemCode);

	String getUiMode(String mode);

	Class getSystemClass(String sysCode);

	Class getType(String sysCode);

	/**
	 * 生成运行时自定义字段
	 * 
	 * @param runtimeCustom
	 * @return
	 */
	List<? extends IFieldDefinition> makeFields(IRuntimeField runtimeCustom);

	IAction getAction(Long systemID, String opMode);
}
