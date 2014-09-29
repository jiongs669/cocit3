package com.fds.entity;

/**
 * 
 * 实体基础类：该类的所有子类都将是实体类，即可以被ORM框架自动映射到数据库表。
 * 
 * @author Ji Yongshan
 * 
 */
public abstract class BaseEntity {
	// 业务实体ID
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
