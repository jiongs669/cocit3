package com.jiongsoft.cocit.orm.listener.impl;

import static com.kmjsoft.cocit.entity.EntityConst.F_GUID;

import java.io.Serializable;

import com.jiongsoft.cocit.lang.Cls;
import com.jiongsoft.cocit.lang.Obj;
import com.jiongsoft.cocit.lang.Str;
import com.jiongsoft.cocit.orm.generator.Generator;
import com.jiongsoft.cocit.orm.generator.impl.GuidGenerator;
import com.jiongsoft.cocit.orm.generator.impl.OperationLogGenerator;
import com.jiongsoft.cocit.orm.listener.EntityListener;
import com.jiongsoft.cocit.orm.mapping.EnMapping;
import com.jiongsoft.cocit.orm.nutz.IExtDao;

public class DemsyEntityListener implements EntityListener {
	private Generator guid;

	private OperationLogGenerator oplog;

	private void init() {
		if (guid == null) {
			guid = new GuidGenerator();
			oplog = new OperationLogGenerator();
		}
	}

	@Override
	public void deleteBefore(IExtDao dao, EnMapping mapping, Object obj) {
	}

	@Override
	public void insertBefore(IExtDao dao, EnMapping mapping, Object obj) {
		init();
		synchronized (obj) {
			oplog.generate(dao, obj);

			String idfld = mapping.getIdProperty();
			if (!Str.isEmpty(idfld)) {
				Serializable id = mapping.getIdGenerator().generate(dao, mapping);
				Obj.setValue(obj, idfld, id);

				// // 生成时序ID
				// if (obj instanceof ITimeID) {
				// int len = 6;
				// ITimeID timeObj = (ITimeID) obj;
				// String idstr = id.toString();
				// while (idstr.length() > len) {
				// idstr = idstr.substring(len);
				// }
				// for (int i = idstr.length(); i < len; i++) {
				// idstr = "0" + idstr;
				// }
				// timeObj.setTimeID(Dates.getCurrentDate("yyyyMMdd") + "-" + idstr);
				// }
			}

			if (Cls.hasField(obj.getClass(), F_GUID) && Str.isEmpty((String) Obj.getValue(obj, F_GUID)))
				Obj.setValue(obj, F_GUID, (String) guid.generate(dao, obj));

		}
	}

	@Override
	public void updateBefore(IExtDao dao, EnMapping mapping, Object obj) {
		init();
		synchronized (obj) {
			oplog.generate(dao, obj);
		}

		// if (bizEngine.hasField(obj.getClass(), F_GUID) &&
		// Str.isEmpty((String) Mirrors.getValue(obj, F_GUID)))
		// Mirrors.setValue(obj, F_GUID, (String) guid.generate(dao, obj));
	}

	@Override
	public void deleteAfter(IExtDao dao, EnMapping mapping, Object entity) {

	}

	@Override
	public void insertAfter(IExtDao dao, EnMapping mapping, Object entity) {

	}

	@Override
	public void updateAfter(IExtDao dao, EnMapping mapping, Object entity) {

	}

}
