package com.kmjsoft.cocit.orm.nutz;

import java.lang.reflect.Field;

import org.nutz.dao.Dao;
import org.nutz.dao.entity.Link;
import org.nutz.dao.impl.DemsyInsertInvoker;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;

import com.jiongsoft.cocit.lang.Obj;
import com.kmjsoft.cocit.entity.IDataEntity;

public class SaveManyInvoker extends DemsyInsertInvoker {

	public SaveManyInvoker(Dao dao, Object mainObj, Mirror<?> mirror) {
		super(dao, mainObj, mirror);
	}

	public void invoke(final Link link, Object many) {
		Object first = Lang.first(many);
		if (null != first) {
			Field refer = link.getReferField();
			if (null == refer) {
				Lang.each(many, new Each<Object>() {
					public void invoke(int index, Object ta, int size) throws ExitLoop {
						((IExtDao) dao).save(ta, null, true);
					}
				});
			} else {
				final Mirror<?> mta = Mirror.me(first.getClass());
				Lang.each(many, new Each<Object>() {
					public void invoke(int index, Object ta, int size) throws ExitLoop {
						mta.setValue(ta, link.getTargetField(), mainObj);
						// AJAX操作状态——删除
						// if (ta instanceof IDataEntity && ((IDataEntity) ta).getStatusForJsonData() == 1) {
						// if (!Obj.isEmpty(ta))
						// ((IExtDao) dao).delete(ta);
						// } else {
						((IExtDao) dao).save(ta, null, true);
						// }
					}
				});
			}
		}
	}
}
