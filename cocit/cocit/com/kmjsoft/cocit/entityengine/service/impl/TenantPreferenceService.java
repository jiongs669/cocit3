package com.kmjsoft.cocit.entityengine.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.EntityConst;
import com.kmjsoft.cocit.entity.config.ITenantPreference;
import com.kmjsoft.cocit.entity.impl.config.TenantPreference;
import com.kmjsoft.cocit.entityengine.service.ITenantPreferenceService;
import com.kmjsoft.cocit.orm.ExtOrm;
import com.kmjsoft.cocit.orm.expr.Expr;
import com.kmjsoft.cocit.util.Log;
import com.kmjsoft.cocit.util.StringUtil;

public class TenantPreferenceService implements ITenantPreferenceService {
	public static final String ADMIN_UI_TOP_HEIGHT = "admin.ui.topHeight";

	public static final String ADMIN_UI_LEFT_WIDTH = "admin.ui.leftWidth";

	private static Map<String, TenantPreferenceService> cache = new HashMap();

	private Map<String, ITenantPreference> map = new HashMap();

	private TenantPreferenceService() {
	}

	public static void clearCache() {
		Long soft = Demsy.me().getTenant().getId();
		TenantPreferenceService config = cache.get(soft);
		if (config != null)
			config.map.clear();

		cache.remove(soft);
	}

	public static TenantPreferenceService me() {
		String tenantGuid = Demsy.me().getTenant().getDataGuid();
		TenantPreferenceService service = cache.get(tenantGuid);
		if (service != null)
			return service;

		service = new TenantPreferenceService();
		cache.put(tenantGuid, service);

		ExtOrm orm = Demsy.orm();
		List<TenantPreference> list = orm.query(TenantPreference.class, Expr.eq(EntityConst.F_TENANT_OWNER_GUID, tenantGuid));
		for (TenantPreference ele : list) {
			service.map.put(ele.getPrefKey(), ele);
		}

		return service;
	}

	protected String getStr(String key) {
		return get(key, "");
	}

	public String get(String key, String defaultValue) {
		ITenantPreference c = map.get(key);

		if (c != null && !Str.isEmpty(c.getPrefValue()))
			return c.getPrefValue();

		return defaultValue;
	}

	public <T> T get(String configKey, T defaultReturn) {
		String value = this.getStr(configKey);

		try {
			return (T) StringUtil.castTo(value, defaultReturn);
		} catch (Throwable e) {
			Log.error("CoudSoftConfigImpl.get: 出错！ {key:%s, defaultReturn:%s}", configKey, defaultReturn, e);
		}

		return defaultReturn;
	}

	public int getInt(String key, int defaultValue) {
		ITenantPreference c = map.get(key);

		try {
			if (c != null && !Str.isEmpty(c.getPrefValue()))
				return Integer.parseInt(c.getPrefValue());
		} catch (Throwable e) {
		}

		return defaultValue;
	}

	public ITenantPreference getPreference(String key) {
		return map.get(key);
	}
}
