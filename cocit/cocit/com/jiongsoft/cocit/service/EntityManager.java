package com.jiongsoft.cocit.service;

import java.util.List;

import com.jiongsoft.cocit.orm.expr.CndExpr;

/**
 * 实体管理器：每个软件实体拥有一个实体管理器。
 * <p>
 * <UL>
 * <LI>执行相关操作前将会进行数据校验；
 * <LI>将会调用业务插件并执行相应的逻辑；
 * </UL>
 * 
 * @author yongshan.ji
 * 
 * @param <T>实体类泛型
 */
public interface EntityManager<T> {

	public Class getType();

	/**
	 * 保存前需要先对数据校验。
	 * 
	 * @param entity
	 * @param opMode
	 * @return @ 保存前校验失败将抛出异常
	 */
	public int save(T entity, String opMode);

	public int delete(T entity, String opMode);

	public int delete(Long entityID, String opMode);

	public int delete(Long[] entityIDArray, String opMode);

	public T load(Long entityID, String opMode);

	public List<T> query(CndExpr expr, String opMode);

	public int count(CndExpr expr, String opMode);

	/**
	 * 执行同步任务：任务的具体执行过程交给插件完成。
	 * 
	 * @param param
	 *            插件所需参数
	 * @param opMode
	 *            操作模式
	 * @return 返回执行结果
	 */
	public String execTask(Object obj, String opMode);

	/**
	 * 执行异步任务：任务的具体执行过程交给插件完成。
	 * 
	 * @param param
	 *            插件所需参数
	 * @param opMode
	 *            操作模式
	 * @return 返回执行结果
	 */
	public String execAsynTask(Object obj, String opMode);
}
