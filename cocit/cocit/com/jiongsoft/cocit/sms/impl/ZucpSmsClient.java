package com.jiongsoft.cocit.sms.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.service.ConfigManager;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.sms.SmsClient;
import com.jiongsoft.cocit.util.Log;

/**
 * 漫道短信接口实现。http://www.zucp.net/
 * <P>
 * webservice返回集合对照表:
 * <UL>
 * <LI>1 没有需要取得的数据: 取用户回复就出现1的返回值,表示没有回复数据.调用mo方法时，可能会出现。
 * <LI>-1 重复注册: 多次点击“注册”按钮或注册方法（Register）的“调用”按钮
 * <LI>-2 帐号/密码不正确: 1.序列号未注册2.密码加密不正确3.密码已被修改4.序列号已注销
 * <LI>-3 重复登陆
 * <LI>-4 余额不足: 直接调用查询看是否余额为0或不足
 * <LI>-5 数据格式错误: 只能自行调试了。或与技术支持联系
 * <LI>-6 参数有误: 看参数传的是否均正常,请调试程序查看各参数
 * <LI>-7 权限受限: 该序列号是否已经开通了调用该方法的权限
 * <LI>-8 流量控制错误
 * <LI>-9 扩展码权限错误: 该序列号是否已经开通了扩展子号的权限,把ext这个参数置空。
 * <LI>-10 内容长度长: 短信内容过长，纯单字节不能超过1000个，双字节不能超过500个字符2.彩信不得超过50KB
 * <LI>-11 数据库错误
 * <LI>-12 序列号状态错误: 序列号是否被禁用
 * <LI>-13 没有提交增值内容: 提交时，无文本或无图片
 * <LI>-14 服务器写文件失败
 * <LI>-17 该接口禁止使用该方法
 * <LI>-18 等上一批提交结果返回再继续下一批提交
 * <LI>-15 内容长度长
 * <LI>-16 提交时，无文本或无图片
 * <LI>-17 没有权限: 如发送彩信仅限于SDK3
 * <LI>-18 上次提交没有等待返回不能继续提交: 默认不支持多线程
 * <LI>-19 禁止同时使用多个接口地址 :每个序列号提交只能使用一个接口地址
 * <LI>-20 相同手机号，相同内容重复提交
 * <LI>-22 Ip鉴权失败: 提交的IP不是所绑定的IP
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public class ZucpSmsClient implements SmsClient {

	private String proxyHost;

	private int proxyPort;

	private String serviceURL;// 服务URL

	private String sn;// 序列号

	private String password;// 本地明文密码

	private String pwdMD5;// 密码

	public ZucpSmsClient() {
	}

	private void init() {

		ActionContext ctx = Cocit.getActionContext();
		SoftService soft = ctx.getSoftService();

		this.proxyHost = soft.getConfig(ConfigManager.SMS_PROXY_HOST, "");
		this.proxyPort = soft.getConfig(ConfigManager.SMS_PROXY_PORT, 80);

		this.serviceURL = soft.getConfig(ConfigManager.SMS_URL, "http://sdk2.zucp.net:8060/webservice.asmx");
		this.sn = soft.getConfig(ConfigManager.SMS_UID, "");
		this.password = soft.getConfig(ConfigManager.SMS_PWD, "");
		this.pwdMD5 = this.getMD5(sn + password);

		Log.info("ZucpSmsClient.init: {serviceURL:%s, sn:%s, password:%s, pwdMD5:%s, proxyHost=%s, proxyPort}", serviceURL, sn, password, pwdMD5, proxyHost, proxyPort);
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
			Log.error("ZucpSmsClient.getMD5: 失败! {sourceStr:}", sourceStr, e);
			return null;
		}
	}

	/**
	 * http://sdk2.zucp.net:8060/webservice.asmx?op=Register
	 * 
	 * @param province
	 * @param city
	 * @param trade
	 * @param entname
	 * @param linkman
	 * @param phone
	 * @param mobile
	 * @param email
	 * @param fax
	 * @param address
	 * @param postcode
	 * @return
	 */
	public String register(String province, String city, String trade, String entname, String linkman, String phone, String mobile, String email, String fax, String address, String postcode) {
		String result = "";
		String soapAction = "http://tempuri.org/Register";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">";
		xml += "<soap12:Body>";
		xml += "<Register xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + password + "</pwd>";
		xml += "<province>" + province + "</province>";
		xml += "<city>" + city + "</city>";
		xml += "<trade>" + trade + "</trade>";
		xml += "<entname>" + entname + "</entname>";
		xml += "<linkman>" + linkman + "</linkman>";
		xml += "<phone>" + phone + "</phone>";
		xml += "<mobile>" + mobile + "</mobile>";
		xml += "<email>" + email + "</email>";
		xml += "<fax>" + fax + "</fax>";
		xml += "<address>" + address + "</address>";
		xml += "<postcode>" + postcode + "</postcode>";
		xml += "<sign></sign>";
		xml += "</Register>";
		xml += "</soap12:Body>";
		xml += "</soap12:Envelope>";

		try {
			HttpURLConnection httpconn = this.createConnection(serviceURL);

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			// bout.write(xml.getBytes("GBK"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<RegisterResult>(.*)</RegisterResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			in.close();
			return new String(result.getBytes(), "utf-8");
		} catch (Exception e) {
			Log.error("ZucpSmsClient.register: 失败!{province:%s, city:%s, trade:%s, entname:%s, linkman:%s, phone:%s, mobile:%s, email:%s, fax:%s, address:%s, postcode:%s}", province, city, trade, entname, linkman, phone, mobile, email, fax, address, postcode, e);
			return "";
		}
	}

	@Override
	public Integer getBalance() {
		init();

		String result = "";
		String soapAction = "http://tempuri.org/balance";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<balance xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwdMD5 + "</pwd>";
		xml += "</balance>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		try {
			HttpURLConnection httpconn = this.createConnection(serviceURL);

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<balanceResult>(.*)</balanceResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			in.close();

			return Integer.parseInt(result);
		} catch (Exception e) {
			Log.error("ZucpSmsClient.queryBalance: 失败!", e);
		}

		return null;
	}

	public String modifyPassword(String newPwd) {
		init();

		String result = "";
		String soapAction = "http://tempuri.org/UDPPwd";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">";
		xml += "<soap12:Body>";
		xml += "<UDPPwd  xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + password + "</pwd>";
		xml += "<newpwd>" + newPwd + "</newpwd>";
		xml += "</UDPPwd>";
		xml += "</soap12:Body>";
		xml += "</soap12:Envelope>";

		try {
			HttpURLConnection httpconn = this.createConnection(serviceURL);

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<UDPPwdResult>(.*)</UDPPwdResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}
			in.close();
			// return result;
			return new String(result.getBytes(), "utf-8");
		} catch (Exception e) {
			Log.error("ZucpSmsClient.modifyPassword: 失败! {newPwd:%s}", newPwd, e);
		}

		return "";
	}

	@Override
	public String send(String mobile, String content, String extCode, String time, String rrid) {
		init();

		String result = "";
		// System.out.print(pwd);
		String soapAction = "http://tempuri.org/mt";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mt xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwdMD5 + "</pwd>";
		xml += "<mobile>" + mobile + "</mobile>";
		xml += "<content>" + content + "</content>";
		xml += "<ext>" + extCode + "</ext>";
		xml += "<stime>" + time + "</stime>";
		xml += "<rrid>" + rrid + "</rrid>";
		xml += "</mt>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		try {
			HttpURLConnection httpconn = this.createConnection(serviceURL);

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			// bout.write(xml.getBytes());

			bout.write(xml.getBytes("utf-8"));
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");// 这一句也关键
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStreamReader isr = new InputStreamReader(httpconn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				Pattern pattern = Pattern.compile("<mtResult>(.*)</mtResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
				}
			}

			return result;
		} catch (Exception e) {
			Log.error("ZucpSmsClient.send(mobile:%s,  content:%s,  extCode:%s,  time:%s,  rrid:%s): 失败!", mobile, content, extCode, time, rrid, e);
			return "";
		}
	}

	@Override
	public List<String[]> receive() {
		init();

		List result = new ArrayList();

		String soapAction = "http://tempuri.org/mo";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<mo xmlns=\"http://tempuri.org/\">";
		xml += "<sn>" + sn + "</sn>";
		xml += "<pwd>" + pwdMD5 + "</pwd>";
		xml += "</mo>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		try {
			HttpURLConnection httpconn = this.createConnection(serviceURL);

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type", "text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);

			OutputStream out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			InputStream isr = httpconn.getInputStream();
			StringBuffer buff = new StringBuffer();
			byte[] byte_receive = new byte[10240];
			for (int i = 0; (i = isr.read(byte_receive)) != -1;) {
				buff.append(new String(byte_receive, 0, i));
			}
			isr.close();
			String result_before = buff.toString();
			int start = result_before.indexOf("<moResult>");
			int end = result_before.indexOf("</moResult>");

			String strResult = result_before.substring(start + 10, end);

			Log.info("ZucpSmsClient.receive: %s", strResult);
			// TODO

		} catch (Exception e) {
			Log.error("ZucpSmsClient.receive: 失败!", e);
			result = null;
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

	// private String getResult(HttpURLConnection httpURLConnection) throws IOException {
	// String result = null;
	//
	// InputStream httpIn = httpURLConnection.getInputStream();
	//
	// if (httpIn != null) {
	//
	// ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	// byte tempByte;
	// while (-1 != (tempByte = (byte) httpIn.read()))
	// byteOut.write(tempByte);
	//
	// result = new String(byteOut.toByteArray(), "gb2312");
	// }
	//
	// return result;
	//
	// }

}
