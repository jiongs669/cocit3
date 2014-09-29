package com.jiongsoft.cocit.entity;

/**
 * 网站内容实体对象：用来存储网站信息发布的内容。
 * 
 * @author jiongsoft
 * 
 */
public interface WebContentEntity extends CoEntity {
	/**
	 * 获取网站栏目实体对象：
	 * 
	 * @return
	 */
	public WebCatalogEntity getCatalog();

	/**
	 * 获取栏目编码：该字段属于冗余字段，内容来自{@link WebCatalogEntity#getCode}。
	 * <p>
	 * 用于快速查询网站栏目中的内容。
	 * 
	 * @return
	 */
	public String getCatalogCode();

	public void setCatalogCode(String code);

	public String getName();

	/**
	 * 获取详细内容文本
	 * 
	 * @return
	 */
	public String getContentText();
	
	public String getImagePath();

}
