package com.jiongsoft.cocit.modules.sms;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jiongsoft.cocit.Demsy;
import com.jiongsoft.cocit.lang.Ex;
import com.jiongsoft.cocit.log.Log;
import com.jiongsoft.cocit.log.Logs;

/**
 * 定时接收短信
 * 
 * @author yongshan.ji
 */
public class ReceiveSmsJob implements Job {
	public static Log log = Logs.getLog(ReceiveSmsJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.debugf("接收短信......");
		try {
			ISmsEngineFactory factory = Demsy.bean("smsEngineFactory");
			ISmsEngine engine = factory.getSmsEngine();

			log.debugf("接收短信: 短信引擎[%s]", engine.getClass().getName());

			engine.receiveSms();

			log.debugf("接收短信: 结束.");
		} catch (Throwable e) {
			log.errorf("接收短信出错! %s", Ex.msg(e));
		}
	}

}
