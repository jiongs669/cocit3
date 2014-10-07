package com.jiongsoft.cocit.orm;

import java.sql.Connection;

public interface NoTransConnCallback {

	Object invoke(Connection conn) throws Exception;

}
