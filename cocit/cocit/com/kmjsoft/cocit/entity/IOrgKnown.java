package com.kmjsoft.cocit.entity;

/**
 * “组织机构的数据实体”接口：该接口的实现类所映射的数据表用来存储组织机构相关的基础业务数据。
 * 
 * @author Ji Yongshan
 * 
 */
public interface IOrgKnown extends IDataEntity {

	/**
	 * 组织机构GUID：数据所属的组织机构GUID。是业务数据表的一个“逻辑外键”字段，该“逻辑外键”关联到“组织机构表”的“物理主键(dataGuid)”。
	 * 
	 * @return
	 */
	public String getOrgGuid();

	/**
	 * 
	 * 组织机构GUID：数据所属的组织机构GUID。是业务数据表的一个“逻辑外键”字段，该“逻辑外键”关联到“组织机构表”的“物理主键(dataGuid)”。
	 * 
	 * @param id
	 */
	public void setOrgGuid(String id);

}
