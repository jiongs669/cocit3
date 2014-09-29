package com.jiongsoft.cocit.entity;

/**
 * 网站栏目实体对象：用来对网站信息发布的内容进行分类。
 * 
 * @author jiongsoft
 * 
 */
public interface WebCatalogEntity extends CoEntity {
	/**
	 * 获取栏目编码
	 * <UL>
	 * <LI>栏目编码：用来唯一标识一个栏目；
	 * <LI>编码规则：年月加两位序号(yyyyMM-xx)，如：“201309-01,201309-02”；
	 * <LI>发布网站信息时：该字段值可以被自动作为网站内容中的冗余字段，以便创建快速查询表达式，避免多余的数据库操作；
	 * </UL>
	 * 
	 * @return
	 */
	public String getCode();

	public String getName();
}
