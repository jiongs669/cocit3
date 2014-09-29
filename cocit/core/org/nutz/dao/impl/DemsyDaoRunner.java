package org.nutz.dao.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.nutz.dao.ConnCallback;
import org.nutz.dao.ConnectionHolder;
import org.nutz.dao.DaoRunner;
import org.nutz.dao.Daos;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class DemsyDaoRunner implements DaoRunner {
	public static Log log = Logs.getLog(DemsyDaoExecutor.class);

	public void run(DataSource dataSource, ConnCallback callback) {
		log.debugf("DAO>>DaoRunner: 打开数据库连接...");
		ConnectionHolder ch = Daos.getConnection(dataSource);
		try {
			log.debugf("DAO>>DaoRunner: 执行回调方法...");
			ch.invoke(callback);
		} catch (Throwable e) {
			try {
				log.debugf("DAO>>DaoRunner: 回滚事务...");
				ch.rollback();
			} catch (SQLException e1) {
				if (log.isWarnEnabled())
					log.warn("DAO>>DaoRunner: 回滚事务出错!!!", e1);
			}
			throw Lang.wrapThrow(e);
		} finally {
			log.debugf("DAO>>DaoRunner: 关闭数据库连接...");
			Daos.releaseConnection(ch);
		}
	}

}
