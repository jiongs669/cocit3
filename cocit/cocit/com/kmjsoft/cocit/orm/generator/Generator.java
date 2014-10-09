package com.kmjsoft.cocit.orm.generator;

import java.io.Serializable;

import com.kmjsoft.cocit.orm.nutz.IExtDao;

public interface Generator<T> {

	public Serializable generate(IExtDao dao, T obj);

}
