package com.jiongsoft.cocit.mvc.view;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.View;

import com.jiongsoft.cocit.log.Log;
import com.jiongsoft.cocit.log.Logs;
import com.jiongsoft.cocit.mvc.MvcConst;

public abstract class AbstractView implements View, MvcConst {
	protected static Log log = Logs.getLog(AbstractView.class);

	protected String path;

	protected AbstractView() {
	}

	protected AbstractView(String path, String type) {
		if (!path.endsWith(type))
			this.path = path.trim() + type;
	}

	protected Map initContext(HttpServletRequest req, HttpServletResponse resp) {
		Map ctx = new HashMap();

		ctx.put("request", req);
		ctx.put("response", resp);
		ctx.put("session", req.getSession());

		return ctx;
	}
}