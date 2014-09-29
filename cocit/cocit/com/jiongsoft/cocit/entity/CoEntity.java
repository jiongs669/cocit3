package com.jiongsoft.cocit.entity;

import com.jiongsoft.cocit.util.StringUtil;

/**
 * 组件化实体类：每个实体类即代表一张数据库表。(Componentization of Entity)
 * <p>
 * 组件化实体对象：每个实体对象即代表一条数据表记录。
 * 
 * <UL>
 * <LI>每个实体类都将有一个数据库表与之对应；
 * <LI>实体类的每个属性通常对应一个数据表字段；
 * <LI>实体类的所有扩展属性将共用一个数据表字段，扩展属性通常不参与查询；
 * <LI>实体ID：对应主键字段，有系统自动生成，通常不作为业务数据参与业务逻辑的处理，数据导入导出过程中，该字段会发生改变；
 * <LI>实体GUID：是一个全球唯一标识符，用作数据的导入导出，数据导入导出过程中该字段值不会改变，也常用来表示业务数据之间的引用关系；
 * <LI>实体SoftID：表示实体数据所属的软件；应为CoC平台可以同时运行多套软件，软件之间可以将会数据库表，因此用SoftID表示数据所属的软件；
 * <LI>扩展属性：通过{@link #get(String, Object)}和{@link #set(String, String)}方法获取和设置实体对象的扩展属性，扩展属性以文本形式存储，需要的时候将自动转换成指定的类型。
 * 类型包括：String/Long/Integer/Short/Byte/Double/Float/Boolean/Date/Number，如果值类型不属于上面的任何类型，则将扩展字段的文本当作一个JSON文本，并试图将其转换成指定类型的Java对象。更多类型可以参见{@link StringUtil#castTo(String, Class)}
 * </UL>
 * 
 * @author jiongsoft
 * 
 */
public interface CoEntity {
	/**
	 * 获取实体ID，通常由系统自动生成作为主键字段。通常不作为业务数据参与业务逻辑的处理。
	 * <p>
	 * 因为涉及数据的导入导出过程中实体ID（主键）会发生变化，因此使用{@link #getEntityGuid()}表示实体之间的引用关系。
	 * 
	 * @return
	 */
	public Long getId();

	/**
	 * 设置实体ID，即数据库表的主键字段，通常由系统自动生成。
	 * <p>
	 * 因为涉及数据的导入导出过程中实体ID（主键）会发生变化，因此使用{@link #setEntityGuid(String)}表示实体之间的引用关系。
	 * 
	 * @param id
	 */
	public void setId(Long id);

	/**
	 * 获取实体对象所属的软件ID，对应组件化自定义软件实体对象的ID。
	 * <p>
	 * 这里没有与软件实体对象的GUID项对应，是考虑softID会一直作为查询条件。因此没有使用GUID，影响查询效率。
	 * 
	 * @return
	 */
	public Long getSoftID();

	/**
	 * 设置实体对象所属的软件ID
	 * 
	 * @param softID
	 */
	public void setSoftID(Long softID);

	/**
	 * 获取实体对象GUID，该字段值通常由系统自动生成。通常作为业务字段参与业务逻辑的处理，如数据实体表之间的间接引用关系。
	 * <P>
	 * 该字段通常用作数据的导入导出；数据关系的引用等。
	 * <p>
	 * 因为导入导出过程中实体ID{@link #getId()}会发生变化，因此使用该字段值表示实体之间的引用关系。
	 * 
	 * @return
	 */
	public String getEntityGuid();

	/**
	 * 设置实体对象GUID，该字段值通常由系统自动生成。
	 * <P>
	 * 该字段通常用作数据的导入导出；数据关系的引用等。
	 * <p>
	 * 因为导入导出过程中实体ID{@link #getId()}会发生变化，因此使用该字段值表示实体之间的引用关系。
	 * 
	 * @param entityGUID
	 */
	public void setEntityGuid(String entityGUID);

	// /**
	// * 获取实体对象的扩展属性，扩展属性没有单独的数据表字段与之对应，而是将所有扩展属性存入一个字段中。
	// * <p>
	// * 扩展属性通常不作为过滤查询字段。
	// *
	// * @param extPropName
	// * @param defaultReturn
	// * @return
	// */
	// public <T> T get(String extPropName, T defaultReturn);
	//
	// /**
	// * 设置实体对象的扩展属性，扩展属性通常以字符串形式存在，只有在{@link #get(String, T)}的时候，才通过泛型将其转换成对应的类型。
	// *
	// * @param extPropName
	// * @param extPropValue
	// */
	// public void set(String extPropName, String extPropValue);

}
