package com.jiongsoft.cocit.orm.generator.impl;

import static com.kmjsoft.cocit.entity.EntityConst.F_CREATED;
import static com.kmjsoft.cocit.entity.EntityConst.F_CREATED_BY;
import static com.kmjsoft.cocit.entity.EntityConst.F_CREATED_IP;
import static com.kmjsoft.cocit.entity.EntityConst.F_ID;
import static com.kmjsoft.cocit.entity.EntityConst.F_UPDATED;
import static com.kmjsoft.cocit.entity.EntityConst.F_UPDATED_BY;
import static com.kmjsoft.cocit.entity.EntityConst.F_UPDATED_IP;

import java.io.Serializable;
import java.util.Date;

import com.jiongsoft.cocit.Demsy;
import com.jiongsoft.cocit.lang.Cls;
import com.jiongsoft.cocit.lang.Obj;
import com.jiongsoft.cocit.orm.generator.Generator;
import com.jiongsoft.cocit.orm.nutz.IExtDao;

public class OperationLogGenerator implements Generator {

	@Override
	public Serializable generate(IExtDao dao, Object obj) {

		Demsy me = Demsy.me();

		Serializable id = (Serializable) Obj.getValue(obj, F_ID);

		if (id == null || (id instanceof Number && ((Number) id).longValue() <= 0)) {
			if (Cls.hasField(obj.getClass(), F_CREATED))
				Obj.setValue(obj, F_CREATED, new Date());

			if (Cls.hasField(obj.getClass(), F_CREATED_BY) && me != null)
				Obj.setValue(obj, F_CREATED_BY, me.username());

			if (Cls.hasField(obj.getClass(), F_CREATED_IP) && me != null)
				Obj.setValue(obj, F_CREATED_IP, me.request().getRemoteAddr());
		}

		if (Cls.hasField(obj.getClass(), F_UPDATED))
			Obj.setValue(obj, F_UPDATED, new Date());

		if (Cls.hasField(obj.getClass(), F_UPDATED_BY) && me != null)
			Obj.setValue(obj, F_UPDATED_BY, me.username());

		if (Cls.hasField(obj.getClass(), F_UPDATED_IP) && me != null)
			Obj.setValue(obj, F_UPDATED_IP, me.request().getRemoteAddr());

		return null;
	}
}
