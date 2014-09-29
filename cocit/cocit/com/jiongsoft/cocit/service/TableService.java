package com.jiongsoft.cocit.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.jiongsoft.cocit.entity.TableEntity;
import com.jiongsoft.cocit.util.CocException;
import com.jiongsoft.cocit.util.Tree;

/**
 * 实体表服务类：对象将为实体表提供一对一的服务。
 * <UL>
 * <LI>代表一个运行时的自定义数据表，通常由定义在数据库中的数据实体解析而来；
 * <LI>与数据模块的关系：每个数据表可以被绑定到多个数据模块，但每个数据模块只能绑定一个数据表；
 * <LI>与数据分组的关系：每个数据表可以包含多个数据分组；
 * <LI>与数据字段的关系：每个数据表可以包含多个数据字段；
 * <LI>与数据子表的关系：每个数据表可以包含多个数据子表，用于描述一主多从结构的数据关系；
 * <LI>与数据父表的关系：每个数据表可以隶属于多个数据父表（如：自定义数据字段表既是自定义数据组的子表，也是自定义数据表的子表）；
 * <LI>与数据操作的关系：每个数据表可以包含多个数据操作，但每个数据操作只能隶属于一个数据表；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public interface TableService extends EntityService<TableEntity> {

	// /**
	// * 获取该数据表的“子数据表”。
	// *
	// * @return
	// */
	// List<EntityTableService> getChildrenDataTables();

	/**
	 * 获取该数据表中的数据分组
	 * 
	 * @return
	 */
	List<FieldGroupService> getEntityGroups();

	/**
	 * 获取该数据表中的所有数据字段。
	 * 
	 * @return
	 */
	List<FieldService> getEntityFields();

	Map<String, FieldService> getEntityFieldsPropMap();

	/**
	 * 获取该数据表中的数据操作，用于生存操作菜单。
	 * 
	 * @return
	 */
	List<OperationService> getEntityOperations();

	/**
	 * 获取该数据表中用作导航树的字段。
	 * 
	 * @return
	 */
	List<FieldService> getEntityFieldsForNaviTree();

	/**
	 * 获取该数据表中用作Grid列的字段。
	 * 
	 * @return
	 */
	List<FieldService> getEntityFieldsForGrid();

	Tree getEntityNaviData();
	
	Tree getEntityTreeData();

	/**
	 * 验证指定的数据是否合法
	 * 
	 * @param opMode
	 * @param entity
	 * @throws CocException
	 */
	void validateEntityData(String opMode, Object entityData) throws CocException;

	/**
	 * 将excel数据解析成实体数据列表。
	 * 
	 * @param excel
	 * @return
	 */
	List parseEntityDataFrom(File excel);
}
