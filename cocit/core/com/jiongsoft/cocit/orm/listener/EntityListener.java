package com.jiongsoft.cocit.orm.listener;

import com.jiongsoft.cocit.orm.mapping.EnMapping;
import com.jiongsoft.cocit.orm.nutz.IExtDao;

public interface EntityListener {
	void insertBefore(IExtDao dao, EnMapping mapping, Object entity);

	void insertAfter(IExtDao dao, EnMapping mapping, Object entity);

	void updateBefore(IExtDao dao, EnMapping mapping, Object entity);

	void updateAfter(IExtDao dao, EnMapping mapping, Object entity);

	void deleteBefore(IExtDao dao, EnMapping mapping, Object entity);

	void deleteAfter(IExtDao dao, EnMapping mapping, Object entity);
}