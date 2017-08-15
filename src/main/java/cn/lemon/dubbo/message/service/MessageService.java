package cn.lemon.dubbo.message.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import cn.lemon.dubbo.message.api.IMessageService;
import cn.lemon.dubbo.message.client.AlismsClient;
import cn.lemon.dubbo.message.client.EmailClient;
import cn.lemon.dubbo.message.client.RabbitClient;
import cn.lemon.dubbo.message.client.WechatClient;
import cn.lemon.dubbo.message.dao.IMessageRecordDao;
import cn.lemon.dubbo.message.dao.IMessageTemplateDao;
import cn.lemon.dubbo.message.em.PushMethodEnum;
import cn.lemon.dubbo.message.entity.MessageRecord;
import cn.lemon.dubbo.message.entity.MessageTemplate;
import cn.lemon.framework.core.BasicService;
import cn.lemon.framework.query.Query;
import cn.lemon.framework.utils.DateUtil;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.base.Strings;

/**
 * 发送消息服务
 * @author lonyee
 */
@Service
public class MessageService extends BasicService implements IMessageService {
	@Resource
	private IMessageRecordDao messageRecordDao;
	@Resource
	private IMessageTemplateDao messageTemplateDao;
	@Resource
	private AlismsClient alismsClient;
	@Resource
	private EmailClient emailClient;
	@Resource
	private WechatClient wechatClient;
	@Resource
	private RabbitClient rabbitClient;
	
	/**
	 * 发送模板消息
	 * @param userId 用户ID
	 * @param messageType 发送消息类型
	 * @param params 消息参数
	 */
	public void sendMessage(Long userId, Integer messageType, Map<String, String> params) {
		this.sendMessage(userId, messageType, null, params);
	}
	
	/**
	 * 发送模板消息
	 * @param userId 用户ID
	 * @param messageType 发送消息类型
	 * @param sendTo 消息发送给xx  mobile/email/routingkey...
	 * @param params 消息参数
	 */
	public void sendMessage(Long userId, Integer messageType, String sendTo, Map<String, String> params) {
		this.sendMessage(userId, messageType, sendTo, null, params);
	}
	
	/**
	 * 发送模板消息
	 * @param userId 用户ID
	 * @param messageType 发送消息类型
	 * @param sendTo 消息发送给xx  mobile/email/routingkey...
	 * @param scheduleTime 定时消息发送时间
	 * @param params 消息参数
	 */
	public void sendMessage(Long userId, Integer messageType, String sendTo, Date scheduleTime, Map<String, String> params) {
		if (params==null || messageType<=0){ return; }
		
		Query query = new Query();
		query.put("messageType", messageType);
		List<MessageTemplate> templateList = messageTemplateDao.findAll(query);
		for(MessageTemplate template: templateList) {
			String title = Strings.isNullOrEmpty(template.getTitle())? "": template.getTitle();
			String url = Strings.isNullOrEmpty(template.getUrl())? "": template.getUrl();
			String content = Strings.isNullOrEmpty(template.getTemplate())? "": template.getTemplate();
			for(Entry<String, String> entry: params.entrySet()) {
				String key = "${"+entry.getKey()+"}";
				String value = Strings.isNullOrEmpty(entry.getValue())? "": entry.getValue();
				url = url.replace(key, value);
				title = title.replace(key, value);
				content = content.replace(key, value);
			}
			if (!Strings.isNullOrEmpty(template.getUrl())) {
				content = content.replace("${url}", url);
			}
			//发送消息
			MessageRecord messageRecord = new MessageRecord();
			messageRecord.setTemplateId(template.getId());
			messageRecord.setPushMethod(template.getPushMethod());
			messageRecord.setCenterId(template.getCenterId());
			messageRecord.setSendTo(sendTo);
			messageRecord.setTitle(title);
			messageRecord.setMessage(content);
			messageRecord.setUrl(url);
			messageRecord.setSendTimes(0);
			messageRecord.setScheduleTime(scheduleTime);
			messageRecord.setUserId(userId);
			messageRecord.setCreatedDate(DateUtil.getNowTime());
			messageRecordDao.save(messageRecord);
			//定时任务不执行发送
			if (messageRecord.getScheduleTime()==null || DateUtil.getNowTime().before(messageRecord.getScheduleTime())) {
				MessageThread messageThread = new MessageThread(messageRecord);
				messageThread.start();
			}
		}
	}
	

	/** 
	 * 定时消息发送
	 **/
	public void scheduledMessage(PushMethodEnum pushMethod) {
		logger.info("start send scheduled {} message.", pushMethod);
		Query query = new Query();
		//query.put("maxSendTimes", 5);  //错误重试最多5次（自动重复3次，定时处理2次）
		query.put("pushMethod", pushMethod);
		query.put("sendTimes", 0);
		query.put("scheduleTime", DateUtil.getNowTime());
		List<MessageRecord> list = messageRecordDao.findAll(query);
		if (list.size() >=1) {
			//创建定长10线程池
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
			for (MessageRecord messageRecord: list) {
				fixedThreadPool.execute(new MessageThread(messageRecord));
			}
		}
		logger.info("finished send {} scheduled {} message.", list.size(), pushMethod);
	}
	
	/**
	 * 采用多线程发送消息
	 * @author lonyee
	 */
	class MessageThread extends Thread {
		private MessageRecord messageRecord;
		/**
		 * 发送消息方法（内部捕获异常，保证发送失败时其他业务逻辑正常执行）
		 * @param messageRecord 消息对象
		 */
		MessageThread(MessageRecord messageRecord) {
			this.messageRecord = messageRecord;
		}
		
		public void run() {
			do {
				//发送失败时记录失败次数   系统自动重发3次
				this.send(messageRecord);
				messageRecordDao.update(messageRecord);
				try {
					Thread.sleep(messageRecord.getSendTimes() * 100L); //歇一会再发
				} catch (InterruptedException e) { }		
			} while(messageRecord.getSendTimes()<3); //失败时自动重发 共3次
		}
		
		/**
		 * 发送消息
		 */
		private void send(MessageRecord messageRecord) {
			try {
				switch (messageRecord.getPushMethod()) {
					case INL: // 发送站内消息
						logger.error("站内消息发送失败, {}。\r\n {}", "未开通的发送服务", messageRecord.getMessage());
						break;
					case SMS: // 发送短信
						alismsClient.send(messageRecord.getSendTo(), messageRecord.getCenterId(), messageRecord.getMessage());
						messageRecord.setSendTimes(9);
						logger.info("短信发送成功。 {}", messageRecord.getMessage());
						break;
					case EMI: // 发送邮件消息
						emailClient.send(messageRecord.getSendTo(), messageRecord.getTitle(), messageRecord.getMessage());
						messageRecord.setSendTimes(9);
						logger.info("邮件发送成功。 {}", messageRecord.getMessage());
						break;
					case RBQ: // 发送MQ消息
						rabbitClient.sendMessage(messageRecord.getSendTo(), messageRecord.getId().toString(), messageRecord.getMessage());
						messageRecord.setSendTimes(9);
						logger.info("MQ消息发送成功。 {}", messageRecord.getMessage());
						break;
					case WXM: // 发送微信模板消息
						wechatClient.sendTemplateMessage(messageRecord.getMessage());
						messageRecord.setSendTimes(9);
						logger.info("微信模板消息发送成功。 {}", messageRecord.getMessage());
						break;
					case ANR: // 发送安卓消息
						logger.error("安卓消息发送失败, {}。\r\n {}", "未开通的发送服务", messageRecord.getMessage());
						break;
					case IOS: // 发送IOS消息
						logger.error("IOS消息发送失败, {}。\r\n {}", "未开通的发送服务", messageRecord.getMessage());
						break;
				}
			} catch (Exception e) {
				messageRecord.setSendTimes(messageRecord.getSendTimes() + 1);
				logger.error("消息发送失败, {}。\r\n {}", e.getMessage(), messageRecord.getMessage());
			}
		}
		
		
	}
	
}
