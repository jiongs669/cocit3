package com.cocit.orm.generator;

import java.io.Serializable;

import com.cocit.orm.nutz.IExtDao;

public interface Generator<T> {

	public Serializable generate(IExtDao dao, T obj);

}
