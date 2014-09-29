package com.jiongsoft.cocit.entity.sms;

import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.entity.ActionEvent;
import com.jiongsoft.cocit.entity.plugin.BasePlugin;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.sms.SmsClient;
import com.jiongsoft.cocit.util.CocException;

public abstract class SmsPlugins {
	public static class QueryBalance extends BasePlugin {
		@Override
		public void before(ActionEvent event) {
			SoftService softService = Cocit.getActionContext().getSoftService();
			SmsClient smsClient = softService.getSmsClient();

			event.setReturnValue("您的短信余额为 " + smsClient.getBalance() + " 条！");
		}
	}

	public static class SendSMS extends BasePlugin<MTSmsEntity> {

		@Override
		public void before(ActionEvent<MTSmsEntity> event) {
			MTSmsEntity entity = event.getEntity();
			SoftService softService = Cocit.getActionContext().getSoftService();
			SmsClient smsClient = softService.getSmsClient();

			if (smsClient == null)
				throw new CocException("短信客户端接口不可用，请检查“系统参数设置>>短信参数设置”！");

			// 重新发送
			Long id = entity.getId();
			if (id != null && id > 0) {
				if ("0".equals(entity.getResult())) {
					throw new CocException("短信已经发送成功，不允许重复发送！");
				}
			}
			// entity.setPreBalance(smsClient.getBalance());
			String sign = softService.getConfig("sms.signature", "");

			String returnValue = smsClient.send(entity.getMobiles(), sign+entity.getContent(), "", "", "");
			entity.setResult(returnValue);

			// entity.setBalance(smsClient.getBalance());

			Integer pre = entity.getPreBalance();
			Integer bal = entity.getBalance();
			if (pre != null && bal != null) {
				entity.setCost(pre - bal);
			}
		}
	}
}
