package com.kmjsoft.cocit.entityengine.definition;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jiongsoft.cocit.entitydef.field.IRuntimeField;
import com.jiongsoft.cocit.lang.DemsyException;
import com.jiongsoft.cocit.lang.Nodes;
import com.jiongsoft.cocit.lang.Option;
import com.kmjsoft.cocit.entity.definition.IEntityDefinition;
import com.kmjsoft.cocit.entity.definition.IFieldDataType;
import com.kmjsoft.cocit.entity.definition.IEntityField;
import com.kmjsoft.cocit.entity.definition.IFieldGroup;
import com.kmjsoft.cocit.entity.security.IAction;
import com.kmjsoft.cocit.entity.security.ISystemTenant;
import com.kmjsoft.cocit.orm.expr.CndExpr;

/**
 * 业务系统引擎
 * 
 * @author yongshan.ji
 */
public interface IEntityDefManager {

	void clearCache();

	List<Class<?>> listTypes();

	IEntityDefinition setupSystemFromDB(ISystemTenant soft, String tableName);

	List<IEntityDefinition> setupSystemFromPackage(ISystemTenant soft) throws DemsyException;

	IEntityDefinition setupSystemFromClass(ISystemTenant soft, Class classOfEntity) throws DemsyException;

	void parseSystemByAnnotation(Class klass, IEntityDefinition system);

	List setupFromPackage(ISystemTenant soft);

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
	List setupFromJson(ISystemTenant soft, Class<?> type);

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
	<T> List<T> setupFromJson(ISystemTenant soft, Class<T> type, String json);

	/**
	 * 从文件夹中导入JSON数据，文件名即为类名。
	 * 
	 * @param soft
	 * @param folder
	 * @return
	 */
	int importFromJson(ISystemTenant soft, String folder);

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
	int exportToJson(ISystemTenant soft, String folder, CndExpr expr) throws IOException;

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
	List<? extends IEntityDefinition> getSystems(ISystemTenant soft);

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

	void validateSystems(ISystemTenant soft) throws DemsyException;

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
	IEntityField getFieldOfUnSelfTree(IEntityDefinition system);

	IEntityField getFieldOfSelfTree(IEntityDefinition system);

	/**
	 * 获取系统字段：不受安全系统的限制。
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统字段列表
	 */
	List<? extends IEntityField> getFields(IEntityDefinition system);

	List<? extends IEntityField> getFields(IFieldGroup group);

	List<? extends IEntityField> getFieldsOfEnabled(IEntityDefinition system);

	List<? extends IEntityField> getFieldsOfEnabled(IFieldGroup group);

	List<? extends IEntityField> getFieldsOfGrid(IEntityDefinition system, String fields);

	/**
	 * @param system
	 * @return
	 */
	List<? extends IEntityField> getFieldsOfSystemFK(IEntityDefinition system, Class fkType);

	/**
	 * 获取系统外键字段：即外键引用其他业务系统
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	List<? extends IEntityField> getFieldsOfSystemFK(IEntityDefinition system);

	/**
	 * 获取所有外键字段：包括外键系统引用和字典字段
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	List<? extends IEntityField> getFieldsOfFK(IEntityDefinition system);

	List<? extends IEntityField> getFieldsOfNavi(IEntityDefinition system);

	Map<String, IEntityField> getFieldsMap(IEntityDefinition system);

	Map<String, IEntityField> getFieldsMap(List<? extends IEntityField> list);

	/**
	 * 获取引用了指定系统的字段
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统被哪些外间字段引用？
	 */
	List<? extends IEntityField> getFieldsOfExport(IEntityDefinition system);

	List<? extends IEntityDefinition> getSystemsOfSlave(IEntityDefinition system);

	List<? extends IEntityField> getFieldsOfSlave(IEntityDefinition system);

	boolean isSlave(IEntityDefinition system);

	// List<? extends IBizField> getChildren(IBizField field);

	boolean isNumber(IEntityField field);

	boolean isInteger(IEntityField field);

	boolean isRichText(IEntityField field);

	boolean isText(IEntityField field);

	boolean isDate(IEntityField field);

	boolean isString(IEntityField field);

	boolean isUpload(IEntityField field);

	boolean isImage(IEntityField field);

	boolean isMultiUpload(IEntityField field);

	boolean isMultiImage(IEntityField field);

	boolean isSubSystem(IEntityField field);

	boolean isFakeSubSystem(IEntityField field);

	boolean isBuildin(IEntityDefinition system, String prop);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	boolean isManyToOne(IEntityField field);

	/**
	 * 判断字段是否为一对一的字段
	 * 
	 * @return
	 */
	abstract boolean isOneToOne(IEntityField field);

	/**
	 * 判断字段是否为一对多的字段
	 * 
	 * @return
	 */
	abstract boolean isOneToMany(IEntityField field);

	/**
	 * 判断字段是否为多对多的字段
	 * 
	 * @return
	 */
	abstract boolean isManyToMany(IEntityField field);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	boolean isV1Dic(IEntityField field);

	boolean isBoolean(IEntityField field);

	boolean isEnabled(IEntityField field);

	boolean isGridField(IEntityField field);

	boolean isSystemFK(IEntityField field);

	boolean isFieldRef(IEntityField field);

	String getPropName(IEntityField field);

	int getGridWidth(IEntityField field);

	int getPrecision(IEntityField field);

	Class getGenericType(IEntityField field) throws DemsyException;

	Class getType(IEntityField field) throws DemsyException;

	String getMode(IEntityField field, IAction action, boolean mustPriority, String defaultMode);

	String getMode(IFieldGroup group, IAction action);

	Nodes makeNaviNodes(IEntityDefinition system, String idField, boolean removeSelfLeaf);

	Nodes makeOptionNodes(IEntityField field, String mode, Object data, String idField);

	List<String> makeCascadeExpr(Object obj, IEntityField field, String mode);

	Option[] getOptions(IEntityField field);

	String[] getCascadeMode(IEntityField field, Object data);

	int getModeValue(String mode);

	Map<String, String> getMode(IEntityDefinition system, IAction action, Object data);

	void validate(IEntityDefinition system, IAction action, Object data, Map<String, String> fieldMode) throws DemsyException;

	void loadFieldValue(Object obj, IEntityDefinition system);

	IEntityField getField(Long fieldID);

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
	List<? extends IEntityField> makeFields(IRuntimeField runtimeCustom);

	IAction getAction(Long systemID, String opMode);
}
