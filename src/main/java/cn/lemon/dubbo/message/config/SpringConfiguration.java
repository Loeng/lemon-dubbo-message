package cn.lemon.dubbo.message.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import cn.lemon.dubbo.message.client.AlismsClient;
import cn.lemon.dubbo.message.client.WechatClient;

/**
 * 系统配置
 * @author lonyee
 */
@Configuration
public class SpringConfiguration {	
	
	@Bean
	@ConfigurationProperties("app.mail")
	public JavaMailSender javaMailSender() {
		return new JavaMailSenderImpl();
	}
	
	@Bean
	@ConfigurationProperties(prefix="app.alisms")
	public AlismsClient alismsClient() {
		return new AlismsClient();
	}
	
	@Bean
	@ConfigurationProperties(prefix="app.wechat")
	public WechatClient wechatClient() {
		return new WechatClient();
	}
	
}
