package com.kmjsoft.cocit.entityengine.module;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.jiongsoft.cocit.entitydef.field.IRuntimeField;
import com.jiongsoft.cocit.lang.DemsyException;
import com.jiongsoft.cocit.lang.Nodes;
import com.jiongsoft.cocit.lang.Option;
import com.kmjsoft.cocit.entity.module.IEntityAction;
import com.kmjsoft.cocit.entity.module.IEntityColumn;
import com.kmjsoft.cocit.entity.module.IEntityColumnGroup;
import com.kmjsoft.cocit.entity.module.IEntityModule;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.orm.expr.CndExpr;

/**
 * 业务系统引擎
 * 
 * @author yongshan.ji
 */
public interface IEntityModuleManager {

	void clearCache();

	List<Class<?>> listTypes();

	IEntityModule setupSystemFromDB(ITenant soft, String tableName);

	List<IEntityModule> setupSystemFromPackage(ITenant soft) throws DemsyException;

	IEntityModule setupSystemFromClass(ITenant soft, Class classOfEntity) throws DemsyException;

	void parseSystemByAnnotation(Class klass, IEntityModule system);

	List setupFromPackage(ITenant soft);

	/**
	 * 获取业务实体扩展类
	 * 
	 * @param system
	 *            业务系统
	 * @return 扩展类全称
	 */
	public abstract String getExtendClassName(IEntityModule system);

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
	// Map<String, IFieldDataType> getFieldTypes();

	// Map<Long, IFieldDataType> getFieldTypesById();

	/**
	 * 获取业务系统列表
	 * 
	 * @return 业务系统集合
	 */
	List<? extends IEntityModule> getSystems(ITenant soft);

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
	IEntityModule getSystem(Long systemID);

	// IBizSystem getSystem(IDemsySoft soft, String systemCode);

	/**
	 * 获取业务系统字段分组列表
	 * 
	 * @param system
	 *            业务系统
	 * @return 字段分组的集合
	 */
	List<? extends IEntityColumnGroup> getFieldGroups(IEntityModule system);

	void validateSystems(ITenant soft) throws DemsyException;

	/**
	 * 只编译业务系统，但不 copy 类文件到/WEB-INF/classes下
	 * 
	 * @param system
	 * @return
	 * @throws DemsyException
	 */
	List<String> compileSystem(IEntityModule system) throws DemsyException;

	/**
	 * 获取业务系统实体类
	 * 
	 * @param system
	 *            业务系统对象
	 * @return 实体类
	 * @throws DemsyException
	 *             编译业务实体类出错将抛出编译错误异常
	 */
	Class getType(IEntityModule system) throws DemsyException;

	Class getStaticType(String system);

	/**
	 * 获取业务系统自身引用的递归树节点属性。
	 * 
	 * @param system
	 *            业务系统
	 * @return 属性名称
	 */
	IEntityColumn getFieldOfUnSelfTree(IEntityModule system);

	IEntityColumn getFieldOfSelfTree(IEntityModule system);

	/**
	 * 获取系统字段：不受安全系统的限制。
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统字段列表
	 */
	List<? extends IEntityColumn> getFields(IEntityModule system);

	List<? extends IEntityColumn> getFields(IEntityColumnGroup group);

	List<? extends IEntityColumn> getFieldsOfEnabled(IEntityModule system);

	List<? extends IEntityColumn> getFieldsOfEnabled(IEntityColumnGroup group);

	List<? extends IEntityColumn> getFieldsOfGrid(IEntityModule system, String fields);

	/**
	 * @param system
	 * @return
	 */
	List<? extends IEntityColumn> getFieldsOfSystemFK(IEntityModule system, Class fkType);

	/**
	 * 获取系统外键字段：即外键引用其他业务系统
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	List<? extends IEntityColumn> getFieldsOfSystemFK(IEntityModule system);

	/**
	 * 获取所有外键字段：包括外键系统引用和字典字段
	 * 
	 * @param system
	 *            业务系统
	 * @return
	 */
	List<? extends IEntityColumn> getFieldsOfFK(IEntityModule system);

	List<? extends IEntityColumn> getFieldsOfNavi(IEntityModule system);

	Map<String, IEntityColumn> getFieldsMap(IEntityModule system);

	Map<String, IEntityColumn> getFieldsMap(List<? extends IEntityColumn> list);

	/**
	 * 获取引用了指定系统的字段
	 * 
	 * @param system
	 *            业务系统
	 * @return 系统被哪些外间字段引用？
	 */
	List<? extends IEntityColumn> getFieldsOfExport(IEntityModule system);

	List<? extends IEntityModule> getSystemsOfSlave(IEntityModule system);

	List<? extends IEntityColumn> getFieldsOfSlave(IEntityModule system);

	boolean isSlave(IEntityModule system);

	// List<? extends IBizField> getChildren(IBizField field);

	boolean isNumber(IEntityColumn field);

	boolean isInteger(IEntityColumn field);

	boolean isRichText(IEntityColumn field);

	boolean isText(IEntityColumn field);

	boolean isDate(IEntityColumn field);

	boolean isString(IEntityColumn field);

	boolean isUpload(IEntityColumn field);

	boolean isImage(IEntityColumn field);

	boolean isMultiUpload(IEntityColumn field);

	boolean isMultiImage(IEntityColumn field);

	boolean isSubSystem(IEntityColumn field);

	boolean isFakeSubSystem(IEntityColumn field);

	boolean isBuildin(IEntityModule system, String prop);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	boolean isManyToOne(IEntityColumn field);

	/**
	 * 判断字段是否为一对一的字段
	 * 
	 * @return
	 */
	abstract boolean isOneToOne(IEntityColumn field);

	/**
	 * 判断字段是否为一对多的字段
	 * 
	 * @return
	 */
	abstract boolean isOneToMany(IEntityColumn field);

	/**
	 * 判断字段是否为多对多的字段
	 * 
	 * @return
	 */
	abstract boolean isManyToMany(IEntityColumn field);

	/**
	 * 判断字段是否为多对一的字段
	 * 
	 * @return
	 */
	boolean isV1Dic(IEntityColumn field);

	boolean isBoolean(IEntityColumn field);

	boolean isEnabled(IEntityColumn field);

	boolean isGridField(IEntityColumn field);

	boolean isSystemFK(IEntityColumn field);

	boolean isFieldRef(IEntityColumn field);

	String getPropName(IEntityColumn field);

	int getGridWidth(IEntityColumn field);

	int getPrecision(IEntityColumn field);

	Class getGenericType(IEntityColumn field) throws DemsyException;

	Class getType(IEntityColumn field) throws DemsyException;

	String getMode(IEntityColumn field, IEntityAction entityAction, boolean mustPriority, String defaultMode);

	String getMode(IEntityColumnGroup group, IEntityAction entityAction);

	Nodes makeNaviNodes(IEntityModule system, String idField, boolean removeSelfLeaf);

	Nodes makeOptionNodes(IEntityColumn field, String mode, Object data, String idField);

	List<String> makeCascadeExpr(Object obj, IEntityColumn field, String mode);

	Option[] getOptions(IEntityColumn field);

	String[] getCascadeMode(IEntityColumn field, Object data);

	int getModeValue(String mode);

	Map<String, String> getMode(IEntityModule system, IEntityAction entityAction, Object data);

	void validate(IEntityModule system, IEntityAction entityAction, Object data, Map<String, String> fieldMode) throws DemsyException;

	void loadFieldValue(Object obj, IEntityModule system);

	IEntityColumn getField(Long fieldID);

	IEntityModule getSystem(String systemCode);

	String getUiMode(String mode);

	Class getSystemClass(String sysCode);

	Class getType(String sysCode);

	/**
	 * 生成运行时自定义字段
	 * 
	 * @param runtimeCustom
	 * @return
	 */
	List<? extends IEntityColumn> makeFields(IRuntimeField runtimeCustom);

	IEntityAction getAction(Long systemID, String opMode);
}
