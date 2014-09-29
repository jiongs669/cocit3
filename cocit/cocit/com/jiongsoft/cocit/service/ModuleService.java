package com.jiongsoft.cocit.service;

import java.util.List;

import com.jiongsoft.cocit.entity.ModuleEntity;

/**
 * 模块服务类：服务对象将为模块提供一对一的服务。
 * 
 * <UL>
 * <LI>代表一个运行时的自定义数据模块，通常由定义在数据库中的数据实体解析而来；
 * <LI>与模块的关系：数据模块是模块的一种；
 * <LI>与数据表的关系：每个数据模块只能绑定一个数据表，而每个数据表则可以被绑定到多个数据模块，绑定方式通常为绑定表达式（JSON表达式）；
 * <LI>与实体子表的关系：每个模块除了可以绑定实体主表之外，还可以绑定一个或多个实体子表，实体子表通过外键引用实体主表，所以实体主表与实体子表是主从关系，一主多从。
 * </UL>
 * 
 * <B>绑定表达式JSON对象解析：</b>
 * <p>
 * <CODE>
 * {
 * table: 1,
 * operations:[1, 2, 3],
 * groups:[1, 2, 3],
 * fields:[1, 2, 3],
 * naviTreeFields: [1, 2, 3],
 * gridFields: [],
 * children:[{},{},...{}],
 * }
 * </CODE>
 * <UL>
 * <LI>table: 表示绑定的数据表ID；
 * <LI>operations: 表示绑定后的数据操作，是一个数组，数组中的每个元素为数据操作ID；
 * <LI>groups: 表示绑定后的数据组，是一个数组，数组中的每个元素为数据组ID；
 * <LI>fields: 表示绑定后的数据字段，是一个数组，数组中的每个元素为数据字段ID；
 * <LI>naviTreeFields: 表示绑定后的导航树字段，是一个数组，数组中的每个元素为支持导航树的字段ID；
 * <LI>gridFields: 表示绑定后的Grid字段，是一个数组，数组中的每个元素为支持导航树的字段ID；
 * <LI>children: 表示绑定后的数据子表，是一个数组，数组中的每个元素为一个绑定表达式；
 * </UL>
 * 
 * @author jiongs753
 * 
 * @param <T>
 */
public interface ModuleService extends EntityService<ModuleEntity> {
	int getType();

	/**
	 * 获取模块徽标
	 * 
	 * @return
	 */
	String getLogo();

	/**
	 * 获取实体表，通常可以通过解析数据模块对数据表的“引用表达式”计算出主数据表对象。
	 * <p>
	 * 用于实体管理模块
	 * 
	 * @return
	 */
	TableService getTable();

	/**
	 * 获取实体子表，通常可以通过解析数据模块对数据表的“引用表达式”计算出从数据表对象。一个主数据表可以包含多个从数据表，用来描述一主多从结构。
	 * <p>
	 * 用于实体管理模块
	 * 
	 * @return
	 */
	List<TableService> getChildrenTables();

	/**
	 * 获取子模块，该方法用于Folder模块。
	 * 
	 * @return
	 */
	List<ModuleService> getChildrenModules();

	ModuleEntity getEntity();
}
