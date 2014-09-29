package com.cocit.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cocit.Demsy;
import com.cocit.api.APIConst;
import com.cocit.api.security.ITenantPreference;
import com.cocit.api.security.ITenant;
import com.cocit.lang.Str;
import com.cocit.orm.IOrm;
import com.jiongsoft.cocit.orm.expr.Expr;

public class SoftConfigManager {
	public static final String ADMIN_UI_TOP_HEIGHT = "admin.ui.topHeight";
	public static final String ADMIN_UI_LEFT_WIDTH = "admin.ui.leftWidth";

	private static Map<Long, SoftConfigManager> cache = new HashMap();

	private Map<String, ITenantPreference> map = new HashMap();

	protected ITenant soft;

	private SoftConfigManager(ITenant soft) {
		this.soft = soft;

	}

	public static void clearCache() {
		Long soft = Demsy.me().getSoft().getId();
		SoftConfigManager config = cache.get(soft);
		if (config != null)
			config.map.clear();

		cache.remove(soft);
	}

	public static SoftConfigManager me() {
		Long softID = Demsy.me().getSoft().getId();
		SoftConfigManager config = cache.get(softID);
		if (config != null)
			return config;

		config = new SoftConfigManager(Demsy.me().getSoft());
		cache.put(softID, config);

		IOrm orm = Demsy.orm();
		List<ITenantPreference> list = orm.query(Demsy.entityDefEngine.getStaticType(APIConst.BIZSYS_ADMIN_CONFIG), Expr.eq(APIConst.F_SOFT_ID, softID));
		for (ITenantPreference ele : list) {
			config.map.put(ele.getCode(), ele);
		}

		return config;
	}

	public String getAlipayPartner() {
		ITenantPreference c = map.get("alipay.partner");
		if (c != null)
			return c.getValue();

		return "";
	}

	public String getAlipayKey() {
		ITenantPreference c = map.get("alipay.key");
		if (c != null)
			return c.getValue();

		return "";
	}

	public String getAlipaySellerEmail() {
		ITenantPreference c = map.get("alipay.seller_email");
		if (c != null)
			return c.getValue();

		return "";
	}

	public String getAlipayNotifyUrl() {
		ITenantPreference c = map.get("alipay.notify_url");

		String ret = "";
		if (c != null)
			ret = c.getValue();

		if (ret.startsWith("/")) {
			String domain = Demsy.me().getDomain();
			int port = Demsy.me().getPort();
			return "http://" + domain + (port != 80 ? (":" + port) : "") + ret;
		}

		return ret;
	}

	public String getAlipayReturnUrl() {
		ITenantPreference c = map.get("alipay.return_url");

		String ret = "";
		if (c != null)
			ret = c.getValue();

		if (ret.startsWith("/")) {
			String domain = Demsy.me().getDomain();
			int port = Demsy.me().getPort();
			return "http://" + domain + (port != 80 ? (":" + port) : "") + ret;
		}

		return ret;
	}

	public String getAlipayShowUrl() {
		ITenantPreference c = map.get("alipay.show_url");

		String ret = "";
		if (c != null)
			ret = c.getValue();

		if (ret.startsWith("/")) {
			String domain = Demsy.me().getDomain();
			int port = Demsy.me().getPort();
			return "http://" + domain + (port != 80 ? (":" + port) : "") + ret;
		}

		return ret;
	}

	/**
	 * 获取支付宝支付服务类型 可选值：标准双接口(trade_create_by_buyer)即时到帐接口
	 * (create_direct_pay_by_user)担保交易接口 (create_partner_trade_by_buyer)
	 * 
	 * @return
	 */
	public String getAlipayPayService() {
		ITenantPreference c = map.get("alipay.pay_service");

		String ret = "";
		if (c != null)
			ret = c.getValue();

		return ret;
	}

	public Double getEshopNotPostFee() {
		ITenantPreference c = map.get("eshop.not_post_fee");

		String ret = "";
		if (c != null)
			ret = c.getValue();
		try {
			if (!Str.isEmpty(ret)) {
				return new Double(ret);
			}
		} catch (Throwable e) {
		}

		return 100.0;
	}

	public String getEshopNotPostFeeDesc() {
		ITenantPreference c = map.get("eshop.not_post_fee");

		String ret = "";
		if (c != null)
			ret = c.getDesc();

		return ret;
	}

	public Double getEshopPostFee() {
		ITenantPreference c = map.get("eshop.post_fee");

		String ret = "";
		if (c != null)
			ret = c.getValue();
		try {
			if (!Str.isEmpty(ret)) {
				return new Double(ret);
			}
		} catch (Throwable e) {
		}

		return 0.0;
	}

	public String getEshopPostFeeDesc() {
		ITenantPreference c = map.get("eshop.post_fee");

		String ret = "";
		if (c != null)
			ret = c.getDesc();

		return ret;
	}

	public String get(String key, String defaultValue) {
		ITenantPreference c = map.get(key);

		if (c != null && !Str.isEmpty(c.getValue()))
			return c.getValue();

		return defaultValue;
	}

	public int getInt(String key, int defaultValue) {
		ITenantPreference c = map.get(key);

		try {
			if (c != null && !Str.isEmpty(c.getValue()))
				return Integer.parseInt(c.getValue());
		} catch (Throwable e) {
		}

		return defaultValue;
	}

	public ITenantPreference getConfig(String key) {
		return map.get(key);
	}
}
