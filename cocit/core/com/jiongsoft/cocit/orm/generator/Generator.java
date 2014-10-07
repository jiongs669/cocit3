package com.jiongsoft.cocit.orm.generator;

import java.io.Serializable;

import com.jiongsoft.cocit.orm.nutz.IExtDao;

public interface Generator<T> {

	public Serializable generate(IExtDao dao, T obj);

}
