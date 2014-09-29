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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.service.ConfigManager;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.sms.SmsClient;
import com.jiongsoft.cocit.util.Log;

/**
 * 展信通。http://www.zrsms.com
 * <UL>
 * <LI>通过浏览器管理短信：http://v3.zrsms.com
 * <LI>uid: zlsandi
 * <LI>pwd: zlsandi
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class ZrSmsClient implements SmsClient {

	private String proxyHost;

	private int proxyPort;

	private String url;// 服务URL

	private String uid;// 序列号

	private String pwd;// 本地明文密码

	private String pwdMD5;// 密码

	public ZrSmsClient() {
		init();
	}

	private void init() {

		ActionContext ctx = Cocit.getActionContext();
		SoftService soft = ctx.getSoftService();

		this.proxyHost = soft.getConfig(ConfigManager.SMS_PROXY_HOST, "");
		this.proxyPort = soft.getConfig(ConfigManager.SMS_PROXY_PORT, 80);

		this.url = soft.getConfig(ConfigManager.SMS_URL, "http://oa.zrsms.com");
		this.uid = soft.getConfig(ConfigManager.SMS_UID, "");
		this.pwd = soft.getConfig(ConfigManager.SMS_PWD, "");
		this.pwdMD5 = this.getMD5(pwd);

		Log.info("ZrSmsClient.init: {url:%s, sn:%s, pwd:%s, pwdMD5:%s, proxyHost:%s, proxyPort:%s}", url, uid, pwd, pwdMD5, proxyHost, proxyPort);
	}

	private String getMD5(String sourceStr) {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}

			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			Log.error("ZrSmsClient.getMD5: 失败! {sourceStr:}", sourceStr, e);
			return null;
		}
	}

	/**
	 * 
	 <p>
	 * http://oa.zrsms.com/api/user_info/?uid=ID&pwd=密码 　　
	 * <p>
	 * 查询用户余额接口
	 * <p>
	 * 其中ID和密码可以直接使用您在本系统中的帐号信息。在本系统中，您同样可以查看到调用接口发送的扣费明细，方便对接口的管理。
	 */
	@Override
	public Integer getBalance() {
		// init();

		String result = null;

		HttpURLConnection httpURLConnection = null;
		String strUrl = String.format("%s/api/user_info/?uid=%s&pwd=%s", url, uid, pwd);
		try {
			httpURLConnection = this.createConnection(strUrl);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=gb2312");

			result = getResult(httpURLConnection);

			Log.info("ZrSmsClient.queryBalance: %s {strUrl:%s}", result, strUrl);

			int eqIndex = result.lastIndexOf('=');
			if (eqIndex > -1) {
				result = result.substring(eqIndex + 1);
			}
			return Integer.parseInt(result);
		} catch (Throwable e) {
			Log.error("ZrSmsClient.queryBalance: 失败！{strUrl:%s}", strUrl, e);
		} finally {

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}

		return null;
	}

	/**
	 * 发送短信, 批量发送手机号码用分号“;”分隔。
	 * 
	 * <UL>
	 * <LI>地址：http://oa.zrsms.com/api/get_send/
	 * <LI>地址：http://oa.zrsms.com/api/post_send/
	 * <LI>地址：http://oa.zrsms.com/api/get_send_md5/
	 * <LI>地址：http://oa.zrsms.com/api/post_send_md5/
	 * <LI>URL参数： ?uid=ID&pwd=md5(密码)&mobile=手机号&msg=短信内容&dtime=时间(时间为空为立即发送,格式:2007-12-01 00:00:00)
	 * </UL>
	 * <LI>注意:1066下发时，需要带上linkid变量，就是取上行时的linkid，例如：&linkid=100010
	 * 
	 * @param mobile
	 *            GET 接口目前只支持100个号码，以分号“;”分隔。POST 接口目前只支持1000个号码，以分号“;”分隔。
	 * @param content
	 *            短信内容
	 * @param extCode
	 * @param time
	 *            时间(时间为空为立即发送,格式:2007-12-01 00:00:00)
	 * @param rrid
	 * @return 返值：0发送成功!;2余额不足!;1用户名或密码错误!;3超过发送最大量100条;4此用户不允许发送!;5手机号或发送信息不能为空!;6含有敏感字,请修改后发送!;7超过70个字,请修改后发送!
	 */
	@Override
	public String send(String mobile, String content, String extCode, String time, String rrid) {
		// init();

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
		String strUrl = String.format("%s/api/post_send/?uid=%s&pwd=%s&mobile=%s&msg=%s&dtime=%s", this.url, this.uid, this.pwd, mobile, content, time);
		try {
			httpURLConnection = this.createConnection(strUrl);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=gb2312");

			result = getResult(httpURLConnection);

			Log.info("ZrSmsClient.send: %s {strUrl:%s}", result, strUrl);

		} catch (Throwable e) {
			Log.error("ZrSmsClient.send: 失败！{strUrl:%s}", strUrl, e);
		}

		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}

		return result;
	}

	/**
	 * 获取回复短信
	 * <UL>
	 * <LI>群发用户获取回复 http://oa.zrsms.com/api/reve/?uid=ID&pwd=密码 　　
	 * <LI>1066用户获取回复 http://oa.zrsms.com/api/remt/?uid=ID&pwd=密码 　　
	 * <LI>插件功能：信息接收。接收地址：例如：http://222.222.222.222/XXXX.XXX ；接收信息格式：Message=短信内容&Mobile=13088888888
	 * <LI>5. 返回信息格式 !!-begin--!!收到通道信息!!-end--!! 每次返回信息不能超过70个字，如果超长系统只发送前70个字。例如:!!--begin--!!信息!!--end--!!
	 * </UL>
	 */
	@Override
	public List<String[]> receive() {
		// init();

		List result = new ArrayList();

		HttpURLConnection httpURLConnection = null;
		String strUrl = String.format("%s/api/reve/?uid=%s&pwd=%s", this.url, this.uid, this.pwd);
		try {
			httpURLConnection = this.createConnection(strUrl);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=gb2312");

			String strResult = getResult(httpURLConnection);

			Log.info("ZrSmsClient.receive: %s {strUrl:%s}", strResult, strUrl);

			// TODO

		} catch (Throwable e) {
			Log.error("ZrSmsClient.receive: 失败！", e);

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

			result = new String(byteOut.toByteArray(), "UTF-8");
		}

		return result;

	}

}
