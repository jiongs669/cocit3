package com.jiongsoft.cocit.entity.sms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jiongsoft.cocit.entity.annotation.CocField;
import com.jiongsoft.cocit.entity.annotation.CocGroup;
import com.jiongsoft.cocit.entity.annotation.CocOperation;
import com.jiongsoft.cocit.entity.annotation.CocTable;
import com.jiongsoft.cocit.entity.impl.BaseEntity;
import com.jiongsoft.cocit.util.UrlAPI;

/**
 * MT(Mobile Terminated)短信：即下行短信，发送到终端的短信。这里的终端通常指手机。
 * <p>
 * 该类的实体对象表示一次短信发送记录。
 * 
 * @author jiongsoft
 * 
 */
@Entity
@CocTable(name = "短信发送管理", code = "MTSmsEntity", pathPrefix = UrlAPI.URL_NS, orderby = 1//
// 操作按钮
, actions = {
//
		@CocOperation(name = "发送短信", typeCode = 101, mode = "c", plugin = SmsPlugins.SendSMS.class)//
		, @CocOperation(name = "重新发送", typeCode = 102, mode = "e", plugin = SmsPlugins.SendSMS.class)//
		, @CocOperation(name = "删除短信", typeCode = 299, mode = "d") //
		, @CocOperation(name = "查看短信", typeCode = 102, mode = "v") //
		, @CocOperation(name = "查询余额", typeCode = 204, mode = "q", plugin = SmsPlugins.QueryBalance.class) //
}// end: actions
// 业务分组
, groups = { //
@CocGroup(name = "基本信息", code = "basic"//
// 业务字段
, fields = { @CocField(name = "短信主题", mode = "*:N v:S c:M e:M", property = "title", desc = "发送本次短信的主题是什么？")//
		, @CocField(name = "手机号码", mode = "*:N v:S c:M e:M", property = "mobiles", desc = "多个手机号码之间用“,”逗号分隔")//
		, @CocField(name = "短信内容", mode = "*:N v:S c:M e:M", property = "content", desc = "最多256个汉字")//
		// , @CocField(name = "上次余额(条)", mode = "*:N v:S", property = "preBalance", desc = "上次发送完成后剩下多少短信余额(条)") //
		// , @CocField(name = "本次余额(条)", mode = "*:N v:S", property = "balance", desc = "本次发送完成后剩下多少短信余额(条)") //
		// , @CocField(name = "本次计费(条)", mode = "*:N v:S", property = "cost", desc = "本次发送需要消费多少短信费用(条)") //
		, @CocField(name = "发送结果", mode = "*:N v:S", property = "result", desc = "0:发送是否成功,998:网络超时,999:操作频繁,") //
		, @CocField(name = "提交时间", mode = "*:N v:S", property = "created", pattern = "yyyy-MM-dd HH:mm:ss") //
// , @CocField(name = "定时发送", mode = "*:N v:S c:E e:E", property = "sendTime", pattern = "yyyy-MM-dd HH:mm:ss") //
}// end: fields
) // end: CocGroup
}// end: groups
)
public class MTSmsEntity extends BaseEntity {

	@Column(length = 64)
	String title;

	@Column(columnDefinition = "text")
	String mobiles;

	@Column(length = 512)
	String content;

	Date sendTime;

	@Column(length = 256)
	String result;

	@Column(updatable = false)
	Integer cost;

	@Column(updatable = false)
	Integer preBalance;

	@Column(updatable = false)
	Integer balance;

	@Column(updatable = false)
	Date created;

	public static MTSmsEntity make(String title, String mobiles, String content) {
		MTSmsEntity sms = new MTSmsEntity();
		sms.setTitle(title);
		sms.setContent(content);
		sms.setMobiles(mobiles);

		return sms;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getPreBalance() {
		return preBalance;
	}

	public void setPreBalance(Integer preBalance) {
		this.preBalance = preBalance;
	}
}
