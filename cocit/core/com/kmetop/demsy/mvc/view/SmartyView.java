package com.kmetop.demsy.mvc.view;

import java.io.File;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.comlib.entity.IDemsySoft;

public class SmartyView extends AbstractView {

	public SmartyView(String path) {
		super(path, TPL_ST);
	}

	public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
		Map ctx = this.initContext(req, resp);

		if (obj instanceof Map)
			ctx.putAll((Map) obj);
		else
			ctx.put("data", obj);

		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", -1);
		resp.setContentType("text/html; charset=UTF-8");

		Writer out = null;
		String tplPath = null;
		String contextPath = "";
		try {
			IDemsySoft soft = Demsy.me().getSoft();
			String softCode = Demsy.appconfig.getDefaultSoftCode();
			if (soft != null) {
				softCode = soft.getCode();
			}
			contextPath = "/" + softCode.replace(".", "_");
		} catch (Throwable e) {

		}
		File tplFile = new File(Demsy.contextDir + contextPath + "/" + this.path);
		if (tplFile.exists()) {
			tplPath = contextPath + "/" + this.path;
		} else {
			tplPath = Demsy.appconfig.getTplPackage().replace(".", "/") + "/" + this.path;
		}
		try {
			out = resp.getWriter();
			MvcUtil.tplEngineST.render(tplPath, ctx, out);
		} catch (Throwable e) {
			log.error("解析模板出错! [" + tplPath + "]", e);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Throwable iglore) {
			}
			resp.flushBuffer();
		}
	}
}