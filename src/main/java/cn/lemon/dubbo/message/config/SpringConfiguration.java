package cn.lemon.dubbo.message.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import cn.lemon.dubbo.message.util.AlismsUtil;
import cn.lemon.dubbo.message.util.WeixinUtil;

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
	public AlismsUtil alismsUtil() {
		return new AlismsUtil();
	}
	
	@Bean
	@ConfigurationProperties(prefix="app.wechat")
	public WeixinUtil weixinUtil() {
		return new WeixinUtil();
	}
	
}
