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
 * MO(Mobile Originate)短信：即上行短信，有终端手机设备发送来的短信。
 * <p>
 * 该类的实体对象即表示手机发送到系统的一条短信。
 * 
 * @author jiongsoft
 * 
 */
@Entity
@CocTable(name = "短信回复管理", code = "MOSmsEntity", pathPrefix = UrlAPI.URL_NS, orderby = 2//
// 操作按钮
, actions = {
// @CocOperation(name = "回复短信", typeCode = 101, mode = "c")//
		@CocOperation(name = "删除短信", typeCode = 299, mode = "d") //
		, @CocOperation(name = "查看短信", typeCode = 102, mode = "v") //
}// end: actions
// 业务分组
, groups = { //
@CocGroup(name = "基本信息", code = "basic"//
// 业务字段
, fields = { @CocField(name = "手机号码", mode = "*:N v:S", property = "mobile")//
		, @CocField(name = "短信内容", mode = "*:N v:S", property = "content")//
		, @CocField(name = "回复时间", mode = "*:N v:S", property = "sendTime", pattern = "yyyy-MM-dd HH-mm-ss")//
		, @CocField(name = "下载时间", mode = "*:N v:S", property = "created", pattern = "yyyy-MM-dd HH-mm-ss") //
}// end: fields
) // end: CocGroup
}// end: groups
)
public class MOSmsEntity extends BaseEntity {

	@Column(length = 20)
	String mobile;

	@Column(length = 512)
	String content;

	Date sendTime;

	@Column(updatable = false)
	Date created;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
