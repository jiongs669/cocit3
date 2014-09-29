package com.cocit.api.entity;

import java.util.List;

/**
 * 树形结构实体：实现该接口的所有实体类为树形实体类，其实体对象集是一棵自身递归树。
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public interface ITreeEntity<T> {
	public T getParent();

	public void setParent(T parent);

	public List<T> getChildren();

	public void setChildren(List<T> children);
}
