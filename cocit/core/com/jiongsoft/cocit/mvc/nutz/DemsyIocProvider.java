package com.jiongsoft.cocit.mvc.nutz;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.IocProvider;
import org.nutz.mvc.NutConfig;

import com.jiongsoft.cocit.Demsy;

public class DemsyIocProvider implements IocProvider {

	@Override
	public Ioc create(NutConfig config, String[] args) {
		return (Ioc) Demsy.ioc.getIoc();
	}

}
