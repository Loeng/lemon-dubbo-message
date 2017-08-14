/**
 * Organization: lemon-china<br>
 * Date: 2017年8月14日下午2:06:57<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.message.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 * @date 2017年8月14日 下午2:06:57 <br>
 * @author lonyee
 */
@Configuration
public class RabbitConfiguration {
	@Value("${app.rabbitmq.queuename:''}")
	private String queueName ;
	@Value("${app.rabbitmq.exchange:''}")
	private String queueExchange;
	
	@Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
	
	@Bean
    @ConfigurationProperties(prefix="app.rabbitmq")
    public ConnectionFactory connectionFactory(){
    	return new CachingConnectionFactory (); //publisherConfirms 消息确认回调
    }
    
    @Bean
    public RabbitTemplate template(ConnectionFactory connectionFactory, MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        //template.setChannelTransacted(false);
        template.setMandatory(true);
        template.setExchange(queueExchange);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }
}
