package com.jiongsoft.cocit.sms;

import java.util.List;

public interface SmsClient {

	// /**
	// * 充值
	// *
	// * @return
	// */
	// public String chargeFee();

	/**
	 * 查询余额
	 * 
	 * @return 返回剩余多少条短信
	 */
	public Integer getBalance();

	// public String modifyPassword(String newPwd);

	/**
	 * 发送短信
	 * 
	 * @param mobiles
	 *            手机号：必填(支持10000个手机号,建议<=5000)多个手机号码用英文逗号“,”隔开
	 * @param content
	 *            短信内容：支持长短信
	 * @param ext
	 *            扩展码：扩展码的用法：您发送短信时，有个ext参数，您给它传值并且要保证每次唯一，用于区分给同一个客户发的多条短信。例如： 您给客户A，在10点发了一条短信,使ext=1，在12点又发了一条,使ext=2，15点又发了一条,使ext=3，到了晚上， 客户A给您回复了 。可以把当初给ext这个参数传的值带回来。以此来达到区分给同一个客户发的多条短信的目的。该参数和接收短信的方法配合使用
	 * @param time
	 *            定时时间：例如：2010-12-29 16:27:03（非定时置空）
	 * @param rrid
	 *            唯一标识：即当前发送短信批次的唯一标识，和rrid对应，如为空则返回系统生成的rrid，此方法推荐用于大量群发， 内容相同手机号多个。
	 * @return
	 */
	public String send(String mobiles, String content, String extCode, String time, String rrid);

	/**
	 * 接收上行短信。
	 * 
	 * <UL>
	 * <LI>返回值列表中的每个元素为一条短信信息；
	 * <LI>每条短信信息是一个数组，数组长度为3；[0]手机号码，[1]发送时间，[2]短信内容
	 * </UL>
	 * 
	 * @return
	 */
	public List<String[]> receive();
}
