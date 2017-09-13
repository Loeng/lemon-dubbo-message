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
import cn.lemon.dubbo.message.entity.MessageRecord;
import cn.lemon.dubbo.message.entity.MessageTemplate;
import cn.lemon.dubbo.response.ResultDubboMessage;
import cn.lemon.framework.core.BasicService;
import cn.lemon.framework.query.Query;
import cn.lemon.framework.response.ServiceException;
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
	//定义线程池
	private static ExecutorService fixedThreadPool = Executors.newCachedThreadPool();

	/**
	 * 发送模板消息
	 * @param userId 用户ID
	 * @param messageType 发送消息类型
	 * @param receiverId 接收人员用户ID
	 * @param params 消息参数
	 */
	@Override
	public void sendMessage(Long userId, String messageType, Long receiverId, Map<String, String> params) throws ServiceException {
		this.sendMessage(userId, messageType, null, receiverId, params);
	}
	
	/**
	 * 发送模板消息
	 * @param userId 用户ID
	 * @param messageType 发送消息类型
	 * @param receiver 消息发送给xx  mobile/email/routingkey...
	 * @param params 消息参数
	 */
	@Override
	public void sendMessage(Long userId, String messageType, String sendTo, Map<String, String> params) throws ServiceException {
		this.sendMessage(userId, messageType, sendTo, null, params);
	}
	
	/**
	 * 发送模板消息 (receiver和receiverId必填一项)
	 * @param userId 用户ID
	 * @param messageType 发送消息类型
	 * @param receiver 消息发送给xx  mobile/email/routingkey...
	 * @param receiverId 接收人员用户ID
	 * @param params 消息参数
	 */
	@Override
	public void sendMessage(Long userId, String messageType, String receiver, Long receiverId, Map<String, String> params) throws ServiceException {
		this.sendMessage(userId, messageType, receiver, receiverId, null, params);
	}
	
	/**
	 * 发送模板消息 (receiver和receiverId必填一项)
	 * @param userId 用户ID
	 * @param messageType 发送消息类型
	 * @param receiver 消息发送给xx  mobile/email/routingkey...
	 * @param receiverId 接收人员用户ID
	 * @param scheduleTime 定时消息发送时间
	 * @param params 消息参数
	 */
	@Override
	public void sendMessage(Long userId, String messageType, String receiver, Long receiverId, Date scheduleTime, Map<String, String> params) throws ServiceException {
		if (params==null || Strings.isNullOrEmpty(messageType)){ return; }
		if (Strings.isNullOrEmpty(receiver) && receiverId==null) {
			throw new ServiceException(ResultDubboMessage.F5024);
		}
		
		Query query = new Query();
		query.put("messageType", messageType);
		List<MessageTemplate> templateList = messageTemplateDao.findAll(query);
		if (templateList.size()<=0) {
			throw new ServiceException(ResultDubboMessage.F5023);
		}
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
			messageRecord.setReceiver(receiver);
			messageRecord.setTitle(title);
			messageRecord.setMessage(content);
			messageRecord.setUrl(url);
			messageRecord.setSendTimes(0);
			messageRecord.setScheduleTime(scheduleTime);
			messageRecord.setReceiverId(receiverId);
			messageRecord.setCreator(userId);
			messageRecord.setCreatedDate(DateUtil.getNowTime());
			messageRecordDao.save(messageRecord);
			//定时任务不执行发送
			if (messageRecord.getScheduleTime()==null || DateUtil.getNowTime().after(messageRecord.getScheduleTime())) {
				fixedThreadPool.execute(new MessageThread(messageRecord));
			}
		}
	}
	

	/** 
	 * 定时消息发送
	 **/
	@Override
	public void scheduledMessage(String pushMethod) {
		logger.info("start send scheduled {} message.", pushMethod);
		Query query = new Query();
		//query.put("maxSendTimes", 5);  //错误重试最多5次（自动重复3次，定时处理2次）
		query.put("pushMethod", pushMethod);
		query.put("sendTimes", 0);
		query.put("scheduleTime", DateUtil.getNowTime());
		List<MessageRecord> list = messageRecordDao.findAll(query);
		if (list.size() >=1) {
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
					case "INL": // 发送站内消息
						throw new RuntimeException("站内消息发送服务未开通");
					case "SMS": // 发送短信
						alismsClient.send(messageRecord.getReceiver(), messageRecord.getCenterId(), messageRecord.getMessage());
						messageRecord.setSendTimes(9);
						logger.info("短信发送成功。 {}", messageRecord.getMessage());
						break;
					case "EMI": // 发送邮件消息
						emailClient.send(messageRecord.getReceiver(), messageRecord.getTitle(), messageRecord.getMessage());
						messageRecord.setSendTimes(9);
						logger.info("邮件发送成功。 {}", messageRecord.getMessage());
						break;
					case "RBQ": // 发送MQ消息
						rabbitClient.sendMessage(messageRecord.getReceiver(), messageRecord.getId().toString(), messageRecord.getMessage());
						messageRecord.setSendTimes(9);
						logger.info("MQ消息发送成功。 {}", messageRecord.getMessage());
						break;
					case "WXM": // 发送微信模板消息
						wechatClient.sendTemplateMessage(messageRecord.getMessage());
						messageRecord.setSendTimes(9);
						logger.info("微信模板消息发送成功。 {}", messageRecord.getMessage());
						break;
					case "ANR": // 发送安卓消息
						throw new RuntimeException("安卓消息发送服务未开通");
					case "IOS": // 发送IOS消息
						throw new RuntimeException("IOS消息发送服务未开通");
				}
			} catch (Exception e) {
				messageRecord.setSendTimes(messageRecord.getSendTimes() + 1);
				logger.error("消息发送失败, {}。\r\n {}", e.getMessage(), messageRecord.getMessage());
			}
		}
		
		
	}
	
}
