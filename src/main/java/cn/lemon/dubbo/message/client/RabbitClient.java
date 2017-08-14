/**
 * Organization: lemon-china<br>
 * Date: 2017年8月14日下午4:59:08<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.message.client;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

/**
 * rabbitmq消息服务类
 * @date 2017年8月14日 下午4:59:08 <br>
 * @author lonyee
 */
@Component
public class RabbitClient {
	private Logger logger = LoggerFactory.getLogger(RabbitClient.class);
	@Resource
	public RabbitTemplate rabbitTemplate;
	
	/**
	 * 发送消息
	 */
	public void sendMessage(final String routingkey, final String msgId, final String message) {
    	logger.info("sendMessage [msgId: {} message:{}]", msgId, message);
    	rabbitTemplate.setCorrelationKey(msgId);
    	rabbitTemplate.convertAndSend(routingkey, (Object) message, new MessagePostProcessor() {            
        	@Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setTimestamp(new Date());
                message.getMessageProperties().setMessageId(UUID.randomUUID().toString());
                message.getMessageProperties().setCorrelationId(msgId.getBytes());
                return message;
            }
        }, new CorrelationData(msgId));
    }
}
