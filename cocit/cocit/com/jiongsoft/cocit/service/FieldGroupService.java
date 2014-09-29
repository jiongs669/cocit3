package com.jiongsoft.cocit.service;

import java.util.List;

import com.jiongsoft.cocit.entity.FieldGroupEntity;

/**
 * 分组服务类：为字段分组提供一对一的服务。
 * 
 * <UL>
 * <LI>代表一个运行时的自定义数据分组，通常由定义在数据库中的数据实体解析而来；
 * <LI>与数据表的关系：每个数据分组只能隶属于一个数据表；
 * <LI>与数据字段的关系：每个数据分组可以包含多个数据字段；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public interface FieldGroupService extends EntityService<FieldGroupEntity> {

	/**
	 * 根据操作码计算数据分组编辑模式：该分组下的所有字段都将继承数据分组的编辑模式。
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
	String getMode(String opCode);

	// EntityTableService getDataTable();

	/**
	 * 获取该数据分组中的所有数据字段。
	 * 
	 * @return
	 */
	List<FieldService> getEntityFields();
}
