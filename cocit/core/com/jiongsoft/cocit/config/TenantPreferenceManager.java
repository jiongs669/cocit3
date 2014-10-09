package com.jiongsoft.cocit.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiongsoft.cocit.lang.Str;
import com.kmjsoft.cocit.Demsy;
import com.kmjsoft.cocit.entity.EntityConst;
import com.kmjsoft.cocit.entity.config.ITenantPreference;
import com.kmjsoft.cocit.entity.security.ITenant;
import com.kmjsoft.cocit.orm.ExtOrm;
import com.kmjsoft.cocit.orm.expr.Expr;

public class TenantPreferenceManager {
	public static final String ADMIN_UI_TOP_HEIGHT = "admin.ui.topHeight";

	public static final String ADMIN_UI_LEFT_WIDTH = "admin.ui.leftWidth";

	private static Map<Long, TenantPreferenceManager> cache = new HashMap();

	private Map<String, ITenantPreference> map = new HashMap();

	protected ITenant tenant;

	private TenantPreferenceManager(ITenant tenant) {
		this.tenant = tenant;

	}

	public static void clearCache() {
		Long soft = Demsy.me().getTenant().getId();
		TenantPreferenceManager config = cache.get(soft);
		if (config != null)
			config.map.clear();

		cache.remove(soft);
	}

	public static TenantPreferenceManager me() {
		Long softID = Demsy.me().getTenant().getId();
		TenantPreferenceManager config = cache.get(softID);
		if (config != null)
			return config;

		config = new TenantPreferenceManager(Demsy.me().getTenant());
		cache.put(softID, config);

		ExtOrm orm = Demsy.orm();
		List<ITenantPreference> list = orm.query(Demsy.entityDefManager.getStaticType(EntityConst.BIZSYS_ADMIN_CONFIG), Expr.eq(EntityConst.F_SOFT_ID, softID));
		for (ITenantPreference ele : list) {
			config.map.put(ele.getPrefKey(), ele);
		}

		return config;
	}

	public String get(String key, String defaultValue) {
		ITenantPreference c = map.get(key);

		if (c != null && !Str.isEmpty(c.getPrefValue()))
			return c.getPrefValue();

		return defaultValue;
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
