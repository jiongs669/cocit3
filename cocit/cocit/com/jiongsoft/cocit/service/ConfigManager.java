package com.jiongsoft.cocit.service;

/**
 * 软件配置助理：用于辅助Cocit软件{@link SoftService}完成配置项管理工作。
 * 
 * @author jiongs753
 * 
 */
public interface ConfigManager {

	/**
	 * 软件配置项KEY：短信服务商类型
	 * <p>
	 * 可选值：
	 * <UL>
	 * <LI>zucp: 漫道短信
	 * <LI>emay: 亿美短信
	 * <LI>zr: 展仁短信
	 * </UL>
	 */
	public static String SMS_TYPE = "sms.type";

	/**
	 * 代理服务器主机IP
	 */
	public static String SMS_PROXY_HOST = "sms.proxy.host";

	/**
	 * 代理服务器主机端口
	 */
	public static String SMS_PROXY_PORT = "sms.proxy.port";

	/**
	 * 软件配置项KEY：uid
	 */
	public static String SMS_UID = "sms.uid";

	/**
	 * 软件配置项KEY：密码
	 */
	public static String SMS_PWD = "sms.pwd";

	/**
	 * 软件配置项KEY：URL
	 */
	public static String SMS_URL = "sms.url";

	public <T> T get(String configKey, T defaultReturn);
}
