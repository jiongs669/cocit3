package com.jiongsoft.cocit.entity;

import java.util.Date;

import com.jiongsoft.cocit.orm.expr.CndExpr;

/**
 * 权限实体对象：用来存储系统模块和用户的授权信息
 * <p>
 * 
 * @author yongshan.ji
 */
public interface PermissionEntity extends CoEntity {

	boolean isDenied();

	boolean isDisabled();

	Date getExpiredFrom();

	Date getExpiredTo();

	/**
	 * 获取“用户类型”：被授权的用户类型。如“网站注册用户、后台管理员”等。
	 * <p>
	 * 语法规则：用户实体表编号、用户实体表ID、用户实体表GUID
	 * <p>
	 * 可选值：
	 * <UL>
	 * <LI>_soft_administrator：“后台管理员”实体表编号
	 * <LI>_WebUser：“网站注册用户”实体表编号
	 * </UL>
	 * 
	 * @since CoC V1
	 */
	public String getUserType();

	/**
	 * 获取“用户主体”：表示该权限被授予哪些用户主体？
	 * <p>
	 * <UL>
	 * <LI>语法规则：可以是“查询表达式”或“用户ID数组” <code>
	 * {
	 * 	field_1: [num_1, num_2, ..., num_n], 
	 * 	field_2: singleValue,
	 * 	...,
	 * 	field_n: ['str_1', 'str_2', ..., 'str_n']
	 * }
	 *  或
	 * 	[id-1, id-2, 'user-3', ..., 'user-n']
	 * </code></LI>
	 * <LI>语法规则举例： <code>
	 * {
	 * 	department: [1,2,3], 
	 * 	role: 4,
	 *  group: ['Support', 'QA', 'Server']
	 * }
	 * 或
	 * [1, 2, 'test', 'user1', 'user2']
	 * </code></LI>
	 * <LI>空值：表示所有用户
	 * </UL>
	 * 
	 * @since CoC V1
	 */
	public String getUserRule();

	/**
	 * 获取“功能权限”：表示用户主体可以执行模块中的哪些功能？
	 * <p>
	 * 语法规则：['moduleID:tableID:opModes', 'm-1:t-1:op1,op2,op3', ..., 'm-i:t-i:op1,op2,op-i']
	 * <UL>
	 * <LI>moduleID：模块ID、模块编号、模块GUID，不支持多模块；
	 * <LI>tableID：实体表ID、实体表编号、实体表GUID，不支持多实体表；
	 * <LI>opModes：操作ID、操作模式，多操作之间可以用逗号“,”分隔；
	 * </UL>
	 * 语法举例：['1:2:*', '2:*:v,e,bu,d', '*:*:e', ..., '1', ':2', ':3:c']
	 * <UL>
	 * <LI>'1:2:*'——表示实体对象有权执行“模块1:实体表2:所有”操作
	 * <LI>'2:*:v,e,bu,d'——表示实体对象有权执行“模块2:所有实体表:查看、编辑、批量修改、删除”操作
	 * <LI>'*:*:E'——表示实体对象有权执行“所有模块:所有实体表:编辑”操作
	 * <LI>'1'——等价于'1::'，表示实体对象有权访问“模块1”
	 * <LI>':2'——等价于':2:'，表示实体对象有权访问“实体表2”
	 * <LI>'::c'——无效
	 * </UL>
	 * 空值：将被忽略，这是必填项。
	 * 
	 * @since CoC V1
	 */
	public String getFuncRule();

	/**
	 * 获取“数据权限”：表示用户主体可以操作模块中的哪些数据？
	 * <UL>
	 * <LI>语法规则：可以是“查询表达式”或“数据ID数组” <code>
	 * {
	 * 	field_1: [num_1, num_2, ..., num_n], 
	 * 	field_2: singleValue,
	 * 	...,
	 * 	field_n: ['str_1', 'str_2', ..., 'str_n']
	 * }
	 *  或
	 * 	[id-1, id-2, id-3, ..., id-n]
	 * </code></LI>
	 * <LI>语法规则举例： <code>
	 * {
	 * 	catalog: [1,2,3], 
	 * 	type: 4,
	 *  country: ['CHINA', 'USA', 'JEPERNESE']
	 * }
	 * 或
	 * [1, 2, 14, 35, 123]
	 * </code></LI>
	 * <LI>语法规则可以被转换成{@link CndExpr}对象，强制作为数据查询条件；
	 * <LI>空值：表示所有数据
	 * </UL>
	 * 
	 * @since CoC V1
	 */
	public String getDataRule();
}
