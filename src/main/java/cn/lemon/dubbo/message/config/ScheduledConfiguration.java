/**
 * Organization: lemon-china<br>
 * Date: 2017年8月14日下午1:03:43<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.message.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import cn.lemon.dubbo.message.api.IMessageService;
import cn.lemon.dubbo.message.em.PushMethodEnum;

/**
 * 定时任务设置
 * @date 2017年8月14日 下午1:03:43 <br>
 * @author lonyee
 */
@Configuration
public class ScheduledConfiguration {
	@Resource
	private IMessageService messageService;
	
	/**
	 * 短信定时任务(每3分钟执行一次)
	 */
	@Scheduled(cron="0 0/3 * * * ? ")
	public void scheduledSmsMessage() {
		messageService.scheduledMessage(PushMethodEnum.SMS);
	}
	
	/**
	 * 邮件定时任务(每5分钟执行一次)
	 */
	@Scheduled(cron="0 0/5 * * * ? ")
	public void scheduledEMIMessage() {
		messageService.scheduledMessage(PushMethodEnum.EMI);
	}
	
	/**
	 * 微信定时任务(每5分钟执行一次)
	 */
	@Scheduled(cron="0 0/5 * * * ? ")
	public void scheduledWXMMessage() {
		messageService.scheduledMessage(PushMethodEnum.WXM);
	}
}
