package com.jiongsoft.cocit.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiongsoft.cocit.Demsy;
import com.jiongsoft.cocit.lang.Str;
import com.jiongsoft.cocit.orm.IOrm;
import com.kmjsoft.cocit.entity.EntityConst;
import com.kmjsoft.cocit.entity.config.IPreferenceOfTenant;
import com.kmjsoft.cocit.entity.security.ISystemTenant;
import com.kmjsoft.cocit.orm.expr.Expr;

public class SoftConfigManager {
	public static final String ADMIN_UI_TOP_HEIGHT = "admin.ui.topHeight";
	public static final String ADMIN_UI_LEFT_WIDTH = "admin.ui.leftWidth";

	private static Map<Long, SoftConfigManager> cache = new HashMap();

	private Map<String, IPreferenceOfTenant> map = new HashMap();

	protected ISystemTenant soft;

	private SoftConfigManager(ISystemTenant soft) {
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
		List<IPreferenceOfTenant> list = orm.query(Demsy.entityDefManager.getStaticType(EntityConst.BIZSYS_ADMIN_CONFIG), Expr.eq(EntityConst.F_SOFT_ID, softID));
		for (IPreferenceOfTenant ele : list) {
			config.map.put(ele.getCode(), ele);
		}

		return config;
	}

	public String getAlipayPartner() {
		IPreferenceOfTenant c = map.get("alipay.partner");
		if (c != null)
			return c.getValue();

		return "";
	}

	public String getAlipayKey() {
		IPreferenceOfTenant c = map.get("alipay.key");
		if (c != null)
			return c.getValue();

		return "";
	}

	public String getAlipaySellerEmail() {
		IPreferenceOfTenant c = map.get("alipay.seller_email");
		if (c != null)
			return c.getValue();

		return "";
	}

	public String getAlipayNotifyUrl() {
		IPreferenceOfTenant c = map.get("alipay.notify_url");

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
		IPreferenceOfTenant c = map.get("alipay.return_url");

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
		IPreferenceOfTenant c = map.get("alipay.show_url");

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
		IPreferenceOfTenant c = map.get("alipay.pay_service");

		String ret = "";
		if (c != null)
			ret = c.getValue();

		return ret;
	}

	public Double getEshopNotPostFee() {
		IPreferenceOfTenant c = map.get("eshop.not_post_fee");

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
		IPreferenceOfTenant c = map.get("eshop.not_post_fee");

		String ret = "";
		if (c != null)
			ret = c.getDesc();

		return ret;
	}

	public Double getEshopPostFee() {
		IPreferenceOfTenant c = map.get("eshop.post_fee");

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
		IPreferenceOfTenant c = map.get("eshop.post_fee");

		String ret = "";
		if (c != null)
			ret = c.getDesc();

		return ret;
	}

	public String get(String key, String defaultValue) {
		IPreferenceOfTenant c = map.get(key);

		if (c != null && !Str.isEmpty(c.getValue()))
			return c.getValue();

		return defaultValue;
	}

	public int getInt(String key, int defaultValue) {
		IPreferenceOfTenant c = map.get(key);

		try {
			if (c != null && !Str.isEmpty(c.getValue()))
				return Integer.parseInt(c.getValue());
		} catch (Throwable e) {
		}

		return defaultValue;
	}

	public IPreferenceOfTenant getConfig(String key) {
		return map.get(key);
	}
}
