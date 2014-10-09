package com.kmjsoft.cocit.entity;

import java.util.Date;

/**
 * “数据实体”接口：该接口的所有实现类即为实体类，实体类将被ORM框架映射到数据库表，其实体对象都将被映射到数据库表记录。
 * 
 * @author Ji Yongshan
 * 
 */
public interface IDataEntity {
	/**
	 * “数据状态码”之“预置(99999)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>对于“预置(99999)”状态的数据，只能查询使用，不能对其执行update或delete操作，即“永久”之意；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	public static final int STATUS_CODE_BUILDIN = 99999;

	/**
	 * “数据状态码”之“删除(-99999)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>对于“删除(-99999)”状态的数据，表示其已被逻辑删除，不允许其参与任何业务逻辑的处理；
	 * <LI>对于“删除(-99999)”状态的数据，不允许对该数据执行“update”操作；
	 * <LI>对于“删除(-99999)”状态的数据，可以对该数据执行“delete”操作即物理删除；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	public static final int STATUS_CODE_DELETED = -99999;

	/**
	 * “数据状态码”之“停用(-999)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>对于“停用(-999)”状态的数据，不允许其参与任何业务逻辑的处理；
	 * <LI>对于“停用(-999)”状态的数据，可以对该数据执行“update”操作；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	public static final int STATUS_CODE_DISABLED = -999;

	/**
	 * “数据状态码”之“新建(0)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>表示本条数据是新的，insert到数据表之后尚未对其作过任何update操作；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	public static final int STATUS_CODE_NEW = 0;

	/**
	 * “数据状态码”之“归档(999)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>对于“归档(999)”状态的数据，不允许再作任何“update”类操作；
	 * <LI>对于“归档(999)”状态的数据，通常可以被移植到另外一张单独的数据表中，以完成物理层面的归档；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	public static final int STATUS_CODE_ARCHIVED = 999;

	/**
	 * 数据ID：即数据的唯一标识符，是数据的物理主键，没有业务含义。
	 * <UL>
	 * <LI>发生数据移植、上报、合并等业务处理的时候，该字段不参与业务处理。
	 * </UL>
	 * 
	 * @return
	 */
	Long getId();

	/**
	 * 
	 * 数据ID：即数据的唯一标识符，是数据的物理主键，没有业务含义。
	 * <UL>
	 * <LI>发生数据移植、上报、合并等业务处理的时候，该字段不参与业务处理。
	 * </UL>
	 * 
	 * @param id
	 */
	void setId(Long id);

	/**
	 * 数据GUID：即数据的全局唯一标识符，通常作为数据的“逻辑主键”或与“租户GUID”组合后作为数据的“逻辑主键”。
	 * <UL>
	 * <LI>对于租户的数据实体，则用“租户GUID（tenantGuid）”和“数据GUID（dataGuid）”组合字段作为数据的“逻辑主键”；
	 * <LI>发生数据移植、上报、合并等业务处理的时候，合并后的数据表中“逻辑字段”的值是不允许重复的；
	 * </UL>
	 * 
	 * @return
	 */
	String getDataGuid();

	/**
	 * 
	 * @param uid
	 */
	void setDataGuid(String dataGuid);

	/**
	 * 数据版本：用来作为数据版本控制字段，就像CVS、SVN、GIT一样，不允许两个人同时修改一条数据。
	 * 
	 * @return
	 */
	Integer getDataVersion();

	/**
	 * 数据版本：用来作为数据版本控制字段，就像CVS、SVN、GIT一样，不允许两个人同时修改一条数据。
	 * 
	 * @param id
	 */
	void setDataVersion(Integer dataVersion);

	Date getCreatedDate();

	void setCreatedDate(Date date);

	String getCreatedUser();

	void setCreatedUser(String user);

	String getCreatedIP();

	void setCreatedIP(String ip);

	/**
	 * 获取“操作时间”：即最近对本条数据执行操作是什么时候？
	 * 
	 * @return
	 */
	Date getUpdatedDate();

	void setUpdatedDate(Date date);

	/**
	 * 获取“操作用户”：即最近对本条数据执行操作的是谁？
	 * 
	 * @return 用户登录帐号
	 */
	String getUpdatedUser();

	void setUpdatedUser(String user);

	String getUpdatedIP();

	void setUpdatedIP(String ip);

	/**
	 * 获取数据状态码。
	 * <p>
	 * 数据状态码：用来描述数据的当前状态。
	 * <UL>
	 * <LI>“新建（{@link #STATUS_CODE_NEW}）”状态：即该数据自insert到数据表之后，尚未对该数据执行过“update”操作。
	 * <LI>“预置（{@link #STATUS_CODE_BUILDIN}）”状态：即系统预置数据。用户只能使用；不能对该数据执行“update”或“delete”操作。
	 * <LI>“删除（{@link #STATUS_CODE_DELETED}）”状态：表示该数据已被逻辑删除。不允许该数据参与任何业务逻辑的处理；不允许对该数据执行“update”操作；但可以对该数据执行“delete”操作即物理删除。
	 * <LI>“停用（{@link #STATUS_CODE_DISABLED}）”状态：即已被停用的数据。不允许该数据参与任何业务逻辑的处理；但可以对该数据执行“update”操作。
	 * <LI>“归档（{@link #STATUS_CODE_ARCHIVED}）”状态：即已被归档的数据。不允许对该数据执行“update”操作；不允许对该数据执行“delete”操作即永久删除；但可以该数据可以被移植到另外一张单独的数据表中即物理归档。
	 * <LI>对基础数据的维护类操作，可以根据操作名称的不同，有区别地选择 1 到 99 之间的值作为数据的状态码。
	 * <LI>处理业务逻辑的过程中，对数据所作“正向的、正面的、积极的”update操作（如业务流程的前进状态），其状态码介于 1 到 99 之间。
	 * <LI>处理业务逻辑的过程中，对数据所作“反向的、反面的、消极的”update操作（如业务流程的返回状态），其状态码介于 -1 到 -99 之间。
	 * <LI>只能对“删除（{@link #STATUS_CODE_DELETED}）”状态的数据执行“delete”操作即永久删除，物理归档除外。
	 * <LI>“操作时间（operatedDate）”描述的是什么时间执行了操作？“操作用户（operatedUser）”描述了是谁执行了操作？“状态码（statusCode）”则可以用来描述执行的是什么操作？
	 * </UL>
	 * 
	 * @return 数据状态码
	 */
	int getStatusCode();

	/**
	 * 设置数据状态码。
	 * <p>
	 * 数据状态码：用来描述数据的当前状态。
	 * <UL>
	 * <LI>“新建（{@link #STATUS_CODE_NEW}）”状态：即该数据自insert到数据表之后，尚未对该数据执行过“update”操作。
	 * <LI>“预置（{@link #STATUS_CODE_BUILDIN}）”状态：即系统预置数据。用户只能使用；不能对该数据执行“update”或“delete”操作。
	 * <LI>“删除（{@link #STATUS_CODE_DELETED}）”状态：表示该数据已被逻辑删除。不允许该数据参与任何业务逻辑的处理；不允许对该数据执行“update”操作；但可以对该数据执行“delete”操作即物理删除。
	 * <LI>“停用（{@link #STATUS_CODE_DISABLED}）”状态：即已被停用的数据。不允许该数据参与任何业务逻辑的处理；但可以对该数据执行“update”操作。
	 * <LI>“归档（{@link #STATUS_CODE_ARCHIVED}）”状态：即已被归档的数据。不允许对该数据执行“update”操作；不允许对该数据执行“delete”操作即永久删除；但可以该数据可以被移植到另外一张单独的数据表中即物理归档。
	 * <LI>对基础数据的维护类操作，可以根据操作名称的不同，有区别地选择 1 到 99 之间的值作为数据的状态码。
	 * <LI>处理业务逻辑的过程中，对数据所作“正向的、正面的、积极的”update操作（如业务流程的前进状态），其状态码介于 1 到 99 之间。
	 * <LI>处理业务逻辑的过程中，对数据所作“反向的、反面的、消极的”update操作（如业务流程的返回状态），其状态码介于 -1 到 -99 之间。
	 * <LI>只能对“删除（{@link #STATUS_CODE_DELETED}）”状态的数据执行“delete”操作即永久删除，物理归档除外。
	 * <LI>“操作时间（operatedDate）”描述的是什么时间执行了操作？“操作用户（operatedUser）”描述了是谁执行了操作？“状态码（statusCode）”则可以用来描述执行的是什么操作？
	 * </UL>
	 * 
	 * @param code
	 *            数据状态码
	 */
	void setStatusCode(int statusCode);

	/**
	 * 检查“实体数据状态{@link #getStatusCode()}”是否等于“预置（{@link #STATUS_CODE_BUILDIN}）”状态？
	 * 
	 * @return
	 */
	boolean isBuildin();

	/**
	 * 检查“实体数据状态{@link #getStatusCode()}”是否等于“停用（{@link #STATUS_CODE_DISABLED}）”状态？
	 * 
	 * @return
	 */
	boolean isDisabled();

	/**
	 * 检查“实体数据状态{@link #getStatusCode()}”是否等于“删除（{@link #STATUS_CODE_DELETED}）”状态？
	 * 
	 * @return
	 */
	boolean isDeleted();

	/**
	 * 检查“实体数据状态{@link #getStatusCode()}”是否等于“归档（{@link #STATUS_CODE_ARCHIVED}）”状态？
	 * 
	 * @return
	 */
	boolean isArchived();
}
