package com.jiongsoft.cocit.mvc.adaptor;

import java.util.Map;

import org.nutz.mvc.adaptor.AbstractAdaptor;
import org.nutz.mvc.adaptor.ParamInjector;
import org.nutz.mvc.adaptor.injector.ArrayInjector;
import org.nutz.mvc.adaptor.injector.MapPairInjector;
import org.nutz.mvc.adaptor.injector.NameInjector;
import org.nutz.mvc.adaptor.injector.ObjectPairInjector;
import org.nutz.mvc.adaptor.injector.PathArgInjector;
import org.nutz.mvc.annotation.Param;

/**
 * 支持嵌套参数的识别
 * 
 * @author yongshan.ji
 */
public class EntityParamAdaptor extends AbstractAdaptor {

	@Override
	protected ParamInjector evalInjector(Class<?> type, Param param) {
		if (null == param)
			return new PathArgInjector(type);
		String pm = param.value();
		// POJO
		if ("..".equals(pm)) {
			if (type.isAssignableFrom(Map.class))
				return new MapPairInjector();
			return new ObjectPairInjector(null, type);
		}
		// POJO with prefix
		else if (pm.startsWith("::") && pm.length() > 2) {
			if (type.isAssignableFrom(EntityParamNode.class)) {
				return new EntityParamInjector(pm.substring(2));
			} else {
				return new ObjectPairInjector(pm.substring(2), type);
			}
		}
		// POJO[]
		else if (type.isArray())
			return new ArrayInjector(pm, type);

		// Name-value
		return new NameInjector(pm, type);
	}
}
