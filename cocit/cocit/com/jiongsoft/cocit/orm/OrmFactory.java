package com.jiongsoft.cocit.orm;

/**
 * ORM工厂：用于获取{@link Orm}对象。因为每个软件可以使用不同的数据库配置，因此通过该工厂来获取当前软件所使用的数据库ORM对象。
 * 
 * @author jiongsoft
 * 
 */
public interface OrmFactory {

	Orm getOrm();
}
