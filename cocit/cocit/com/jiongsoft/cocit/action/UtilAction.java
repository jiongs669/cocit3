package com.jiongsoft.cocit.action;

import java.util.Date;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.jiongsoft.cocit.ActionContext;
import com.jiongsoft.cocit.Cocit;
import com.jiongsoft.cocit.entity.sms.MTSmsEntity;
import com.jiongsoft.cocit.service.SoftService;
import com.jiongsoft.cocit.ui.UIModelView;
import com.jiongsoft.cocit.ui.model.AlertsModel;
import com.jiongsoft.cocit.util.DateUtil;
import com.jiongsoft.cocit.util.HttpUtil;
import com.jiongsoft.cocit.util.Log;
import com.jiongsoft.cocit.util.UrlAPI;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class UtilAction {

	@At(UrlAPI.CHK_HEARTBEAT)
	public AlertsModel chkHeartbeat(String timestamp) {
		try {
			ActionContext ctx = Cocit.getActionContext();
			Date date = new Date(Long.parseLong(timestamp));
			String strDate = DateUtil.format(date, DateUtil.DEFAULT_DATE_TIME_PATTERN);
			Log.debug("应用程序心跳检测......{IP:%s, referer:%s, jsessionid:%s}", ctx.getRequest().getRemoteAddr(), ctx.getRequest().getHeader("referer"), ctx.getRequest().getRequestedSessionId());

			return AlertsModel.makeSuccess(strDate);
		} catch (Throwable e) {
			return AlertsModel.makeError(e.getMessage());
		}
	}

	@At(UrlAPI.GET_IMG_VERIFY_CODE)
	public void getImgVerifyCode() {
		ActionContext ctx = Cocit.getActionContext();
		HttpUtil.makeImgVerifyCode(ctx.getRequest(), ctx.getResponse());
	}

	@At(UrlAPI.CHK_IMG_VERIFY_CODE)
	public AlertsModel chkImgVerifyCode(String code) {
		String message = "";
		try {
			ActionContext ctx = Cocit.getActionContext();
			HttpUtil.checkImgVerifyCode(ctx.getRequest(), code, null);

			message = "检查验证码成功！";

			return AlertsModel.makeSuccess(message);
		} catch (Throwable e) {
			Log.warn("", e);
			message = "验证码非法！";

			return AlertsModel.makeError(message);
		}
	}

	@At(UrlAPI.GET_SMS_VERIFY_CODE)
	public AlertsModel getSmsVerifyCode(String tel) {
		String message = "";
		try {
			ActionContext ctx = Cocit.getActionContext();

			String code = HttpUtil.makeSmsVerifyCode(ctx.getRequest(), tel);

			SoftService soft = ctx.getSoftService();
			String tpl = soft.getConfig("sms.verify_code_content", "请在网页表单中输入您的验证码：%s");

			String content = String.format(tpl, code);

			/**
			 * 发送短信
			 */
			MTSmsEntity sms = MTSmsEntity.make("手机验证码", tel, content);
			ActionHelper actionHelper = ActionHelper.make("0:MTSmsEntity:c");
			actionHelper.entityManager.save(sms, "c");

			message = "获取短信验证码成功，请注意查看您的手机短信！";

			return AlertsModel.makeSuccess(message);
		} catch (Throwable e) {
			Log.warn("", e);
			message = "获取短信验证码失败！" + e.getMessage();

			return AlertsModel.makeError(message);
		}
	}

	@At(UrlAPI.GET_SMS_VERIFY_CODE2)
	public AlertsModel getSmsVerifyCode2(String tel) {
		String message = "";
		try {
			ActionContext ctx = Cocit.getActionContext();

			String code = HttpUtil.makeSmsVerifyCode(ctx.getRequest(), tel);

			// SoftService soft = ctx.getSoftService();
			// String tpl = soft.getConfig("sms.verify_code_content", "请在网页表单中输入您的验证码：%s");
			//
			// String content = String.format(tpl, code);

			// /**
			// * 发送短信
			// */
			// MTSmsEntity sms = MTSmsEntity.make("手机验证码", tel, content);
			// ActionHelper actionHelper = ActionHelper.make("0:MTSmsEntity:c");
			// actionHelper.entityManager.save(sms, "c");

			message = "短信通道异常，请输入验证码：" + code;

			return AlertsModel.makeSuccess(message);
		} catch (Throwable e) {
			Log.warn("", e);
			message = "获取短信验证码失败！" + e.getMessage();

			return AlertsModel.makeError(message);
		}
	}

	@At(UrlAPI.CHK_SMS_VERIFY_CODE)
	public AlertsModel chkSmsVerifyCode(String tel, String code) {
		String message = "";
		try {
			ActionContext ctx = Cocit.getActionContext();
			HttpUtil.checkSmsVerifyCode(ctx.getRequest(), tel, code, null);

			message = "检查验证码成功！";

			return AlertsModel.makeSuccess(message);
		} catch (Throwable e) {
			Log.warn("", e);
			message = "验证码非法！";

			return AlertsModel.makeError(message);
		}
	}
}
