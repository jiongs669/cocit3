package com.jiongsoft.cocit.orm;

import java.io.Serializable;
import java.util.List;

import com.jiongsoft.cocit.orm.expr.CndExpr;
import com.jiongsoft.cocit.orm.expr.NullCndExpr;

/**
 * 对象关系数据库映射接口：用于管理和查询数据库数据表，包括：增、删、查、改数据库表记录。
 * 
 * @author yongshan.ji
 * 
 */
public interface Orm {
	/**
	 * <b>保存实体对象</b>
	 * <p>
	 * 1. 保存的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 如果实体数据已经存在则对数据作修改操作，否则插入一条新记录到数据表中；
	 * <p>
	 * 3. 不存在的Many/ManyMany关联对象将会被自动保存；
	 * <p>
	 * 4. 只有匹配正则表达式的字段才会被保存，如果未指定字段正则表达式，则所有字段都将被保存；
	 * <p>
	 * 5. 多个正则表达式之间按“|”关系处理；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式(如:“id|name”)
	 * @return 新增和修改的总记录数
	 */
	public int save(Object obj, NullCndExpr fieldRexpr);

	public int save(Object obj);

	/**
	 * <b>删除实体对象</b>
	 * <p>
	 * 1. 删除的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 关联的Many/ManyMany数据“不会”被自动删除；
	 * 
	 * @param obj
	 *            实体对象
	 * @return 成功删除的记录数
	 */
	public int delete(Object obj);

	/**
	 * <b>删除实体对象</b>
	 * <p>
	 * 1. 只删除实体ID与指定ID相同的实体对象；
	 * <p>
	 * 2. 关联的Many/ManyMany数据“不会”被自动删除；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param id
	 *            实体ID
	 * @return 成功删除的记录数
	 */
	public int delete(Class classOfEntity, Serializable id);

	/**
	 * <b>批量删除</b>
	 * <p>
	 * 1. 删除满足条件的实体数据，如果没有指定删除条件，则将清空整张表；
	 * <p>
	 * 2. 关联的Many/ManyMany数据“不会”被自动删除；
	 * <p>
	 * 3. 多个表达式中的条件表达式按“AND”关系处理；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            表达式
	 * @return 成功删除的记录数
	 */
	public int deleteMore(Class classOfEntity, CndExpr expr);

	public int deleteMore(Class classOfEntity);

	public <T> T load(Class<T> classOfEntity, Serializable id);

	/**
	 * 获取第一条满足条件的记录
	 * 
	 * @param classOfEntity
	 * @param expr
	 * @return
	 */
	public <T> T get(Class<T> classOfEntity, CndExpr expr);

	/**
	 * <b>查询分页实体集</b>
	 * <p>
	 * 1. 如果未指定查询条件，则将对整表进行查询;
	 * <p>
	 * 2. 只有满足正则表达式的字段才会被绑定，如果未指定字段表达式，则所有字段都将被绑定。
	 * <p>
	 * 3. 多个表达式中：条件表达式按“AND”关系处理；字段表达式按“|”关系处理；排序、分组、分页等表达式来自第一个表达式；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            表达式
	 * @return 分页结果集
	 */
	public <T> List<T> query(Class<T> classOfEntity, CndExpr expr);

}
