package com.cocit.modules.ckfinder;

import javax.servlet.http.HttpServletRequest;

import com.ckfinder.connector.configuration.DefaultPathBuilder;
import com.ckfinder.connector.utils.PathUtils;
import com.cocit.mvc.MvcConst.MvcUtil;

public class PathBuilder extends DefaultPathBuilder {
	@Override
	public String getBaseUrl(final HttpServletRequest request) {
		String baseURL = MvcUtil.getUploadBasePath();

		return PathUtils.addSlashToBeginning(PathUtils.addSlashToEnd(baseURL));
	}

	@Override
	public String getBaseDir(final HttpServletRequest request) {
		return super.getBaseDir(request);
	}

}
