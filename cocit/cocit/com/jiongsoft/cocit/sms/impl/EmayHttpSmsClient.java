package com.jiongsoft.cocit.sms.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.service.ConfigManager;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.sms.SmsClient;
import com.jiongsoft.cocit.util.Log;

/**
 * 亿美嵌入型短信接口：http://www.emay.cn
 * 
 * <UL>
 * <LI>0 成功
 * <LI>-1 系统异常
 * <LI>-2 客户端异常
 * <LI>-9001 序列号格式错误
 * <LI>-9002 密码格式错误
 * <LI>-9003 客户端key格式错误
 * <LI>-9025 客户端请求sdk5超时
 * <LI>-101 命令不被支持
 * <LI>-104 请求超过限制
 * <LI>-110 号码注册激活失败
 * <LI>-1100 序列号错误，序列号不存在内存中或尝试攻击的用户
 * <LI>-1103 序列号key错误
 * <LI>-1105 注册号状态异常，未用1
 * <LI>-1107 注册号状态异常，停用3
 * <LI>-1108 注册号状态异常，停止5
 * <LI>-126 路由信息失败
 * <LI>-1104 路由失败，请联系系统管理员
 * <LI>-190 数据操作失败
 * <LI>-1901 数据库更新操作失败
 * <LI>303 客户端网络超时或网络故障
 * <LI>305 服务器端返回错误，错误的返回值（返回值不是数字字符串）
 * <LI>999 操作频繁
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class EmayHttpSmsClient implements SmsClient {

	private String proxyHost;

	private int proxyPort;

	private String url;

	private String uid;

	private String pwd;// 本地明文密码

	private String key;// 序列号

	public EmayHttpSmsClient() {
		ActionContext ctx = Cocit.getActionContext();

		if (ctx != null) {
			SoftService soft = ctx.getSoftService();

			this.proxyHost = soft.getConfig(ConfigManager.SMS_PROXY_HOST, "");
			this.proxyPort = soft.getConfig(ConfigManager.SMS_PROXY_PORT, 80);

			this.url = soft.getConfig(ConfigManager.SMS_URL, "http://sdkhttp.eucp.b2m.cn");
			this.uid = soft.getConfig(ConfigManager.SMS_UID, "");
			this.pwd = soft.getConfig(ConfigManager.SMS_PWD, "");
			this.key = soft.getConfig("sms.key", "");

			this.register();
		}
		Log.info("SmsClientZucpImpl.new: {url:%s, sn:%s, pwd:%s, key:%S, proxyHost:%s, proxyPort:%S}", url, uid, pwd, key, proxyHost, proxyPort);
	}

	public String register() {
		String result = null;

		HttpURLConnection httpURLConnection = null;
		String strUrl = String.format("%s/sdkproxy/regist.action?cdkey=%s&password=%s", url, uid, pwd);
		try {
			httpURLConnection = this.createConnection(strUrl);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=gb2312");

			result = getResult(httpURLConnection);

			Log.info("EmayHttpSmsClient.register: %s {strUrl:%s}", result, strUrl);

		} catch (Throwable e) {
			Log.error("EmayHttpSmsClient.register: 失败！{strUrl:%s}", strUrl, e);
		}

		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}

		return result;
	}

	@Override
	public Integer getBalance() {
		String result = null;

		HttpURLConnection httpURLConnection = null;
		String strUrl = String.format("%s/sdkproxy/querybalance.action?cdkey=%s&password=%s", url, uid, pwd);
		try {
			httpURLConnection = this.createConnection(strUrl);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=gb2312");

			result = getResult(httpURLConnection);

			Log.info("EmayHttpSmsClient.queryBalance: %s {strUrl:%s}", result, strUrl);

		} catch (Throwable e) {
			Log.error("EmayHttpSmsClient.queryBalance: 失败！{strUrl:%s}", strUrl, e);
		}

		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}

		return new Double(Double.parseDouble(result) * 10).intValue();
	}

	/**
	 * <p>
	 * 参数：
	 * <UL>
	 * <LI>mobiles 手机号码(群发为字符串数组推荐最多为200个手机号码或以内)
	 * <LI>smsContent 短信内容(最多500个汉字或1000个纯英文，emay服务器程序能够自动分割；亿美有多个通道为客户提供服务，所以分割原则采用最短字数的通道为分割短信长度的规则，请客户应用程序不要自己分割短信以免造成混乱)
	 * <LI>addSerial 扩展号 (长度小于15的字符串) 用户可通过扩展号自定义短信类别
	 * <LI>smsPriority 优先级(级别从1到5的正整数，数字越大优先级越高，越先被发送)
	 * 
	 * <p>
	 * 返回值：
	 * <UL>
	 * <UL>
	 * <LI>0 成功
	 * <LI>-1 系统异常
	 * <LI>-2 客户端异常
	 * <LI>-9001 序列号格式错误
	 * <LI>-9002 密码格式错误
	 * <LI>-9003 客户端key格式错误
	 * <LI>-9016 发送短信包大小超出范围
	 * <LI>-9017 发送短信内容格式错误
	 * <LI>-9019 发送短信优先级格式错误
	 * <LI>-9020 发送短信手机号格式错误
	 * <LI>-9022 发送短信唯一序列值错误
	 * <LI>-9025 客户端请求sdk5超时
	 * <LI>-101 命令不被支持
	 * <LI>-104 请求超过限制
	 * <LI>-117 发送短信失败
	 * <LI>-126 路由信息失败
	 * <LI>-1104 路由失败，请联系系统管理员
	 * <LI>101 客户端网络故障
	 * <LI>307 目标电话不符合规则，电话号码必须是以0，1开头
	 * <LI>303 由于客户端网络问题导致信息发送超时，该信息是否成功下发无法确定
	 * <LI>305 服务器端返回错误，错误的返回值（返回值不是数字字符串）
	 * </UL>
	 */
	@Override
	public String send(String mobile, String content, String extCode, String time, String rrid) {
		String result = null;
		//
		// // 验证手机号码
		// if (StringUtil.trim(mobile).length() < 11) {
		// Log.warn("%s.send: 非法手机号码！{mobile:%s,  content:%s,  extCode:%s,  time:%s,  rrid:%s}", logPrefix, mobile, content, extCode, time, rrid);
		//
		// return "";
		// }
		//
		// if (StringUtil.isNil(content)) {
		// Log.warn("%s.send: 短信内容不允许为空！{mobile:%s,  content:%s,  extCode:%s,  time:%s,  rrid:%s}", logPrefix, mobile, content, extCode, time, rrid);
		//
		// return null;
		// }

		HttpURLConnection httpURLConnection = null;
		String strUrl = String.format("%s/sdkproxy/sendsms.action?cdkey=%s&password=%s&phone=%s&content=%s", this.url, this.uid, this.pwd, mobile, content);
		try {
			httpURLConnection = this.createConnection(strUrl);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=gb2312");

			result = getResult(httpURLConnection);

			Log.info("EmayHttpSmsClient.send: %s {strUrl:%s}", result, strUrl);

		} catch (Throwable e) {
			Log.error("EmayHttpSmsClient.send: 失败！{strUrl:%s}", strUrl, e);
		}

		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}

		return result;
	}

	@Override
	public List<String[]> receive() {
		List result = new ArrayList();

		HttpURLConnection httpURLConnection = null;
		String strUrl = String.format("%s/sdkproxy/getmo.action?cdkey=%s&password=%s", this.url, this.uid, this.pwd);
		try {
			httpURLConnection = this.createConnection(strUrl);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=gb2312");

			String strResult = getResult(httpURLConnection);

			Log.info("EmayHttpSmsClient.receive: %s {strUrl:%s}", strResult, strUrl);

			// TODO

		} catch (Throwable e) {
			Log.error("EmayHttpSmsClient.receive: 失败！{strUrl:%s}", strUrl, e);

			result = null;
		}

		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}

		return result;
	}

	private HttpURLConnection createConnection(String strUrl) throws Exception {
		URL url = new URL(strUrl);

		URLConnection connection = null;
		try {
			connection = url.openConnection();
		} catch (Throwable e) {
			Log.warn("", e);
			if (this.proxyHost != null && proxyHost.trim().length() > 0) {
				SocketAddress proxyAddress = new InetSocketAddress(this.proxyHost, this.proxyPort);
				Proxy typeProxy = new Proxy(Proxy.Type.HTTP, proxyAddress);

				connection = url.openConnection(typeProxy);
			}
		}

		return (HttpURLConnection) connection;
	}

	private String getResult(HttpURLConnection httpURLConnection) throws IOException {
		String result = null;

		InputStream httpIn = httpURLConnection.getInputStream();

		if (httpIn != null) {

			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			byte tempByte;
			while (-1 != (tempByte = (byte) httpIn.read()))
				byteOut.write(tempByte);

			result = new String(byteOut.toByteArray(), "gb2312");
		}

		return result;

	}
}
