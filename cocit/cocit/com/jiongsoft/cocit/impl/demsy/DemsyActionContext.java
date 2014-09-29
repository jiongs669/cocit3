package com.jiongsoft.cocit.impl.demsy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jiongsoft.cocit.impl.BaseActionContext;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.StringUtil;
import com.kmetop.demsy.Demsy;

public class DemsyActionContext extends BaseActionContext {
	private Demsy demsyContext;

	public DemsyActionContext(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);

		//
		demsyContext = Demsy.me();
	}

	public int getBrowserWidth() {
		if (demsyContext.login() != null) {
			Double d = demsyContext.login().getClientWidth();
			return d.intValue();
		}
		return 1920;
	}

	public int getBrowserHeight() {
		if (demsyContext.login() != null) {
			Double d = demsyContext.login().getClientHeight();
			return d.intValue();
		}
		return 1000;
	}

	public int getAdminTopHeight() {
		int ret = 0;
		int browserWidth = getBrowserHeight();
		String w = this.getConfig("admin.ui.topHeight", "");

		try {
			if (!StringUtil.isNil(w))
				if (w.endsWith("%"))
					ret = browserWidth * Integer.parseInt(w.substring(0, w.length() - 1)) / 100;
				else
					ret = Integer.parseInt(w);
		} catch (Throwable e) {
			Log.warn("", e);
			ret = 95;
		}

		return ret + 45;
		// return 30;
	}

	public int getAdminLeftWidth() {
		int ret = 0;
		int browserWidth = getBrowserWidth();
		String w = this.getConfig("admin.ui.leftWidth", "");

		try {
			if (!StringUtil.isNil(w)) {
				if (w.endsWith("%"))
					ret = browserWidth * Integer.parseInt(w.substring(0, w.length() - 1)) / 100;
				else
					ret = Integer.parseInt(w);
			} else {
				ret = new Double(browserWidth * 0.2).intValue();
			}
		} catch (Throwable e) {
			Log.warn("", e);
			ret = new Double(browserWidth * 0.2).intValue();
		}

		return ret + 30;
	}

	@Override
	public int getClientUIHeight() {
		int ret = this.getParameterValue("_uiHeight", 0);
		if (ret == 0)
			ret = this.getBrowserHeight() - this.getAdminTopHeight();

		return ret;
	}

	@Override
	public int getClientUIWidth() {
		int ret = this.getParameterValue("_uiWidth", 0);
		if (ret == 0)
			ret = this.getBrowserWidth() - this.getAdminLeftWidth();

		return ret;
	}

	@Override
	public boolean isLocalHost() {
		return demsyContext.isLocal();
	}

	@Override
	public Long getSoftID() {
		if (demsyContext.getSoft() != null)
			return demsyContext.getSoft().getId();

		return null;
	}
}
