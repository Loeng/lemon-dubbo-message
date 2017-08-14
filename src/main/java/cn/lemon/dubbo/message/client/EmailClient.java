package cn.lemon.dubbo.message.client;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 邮件服务类
 * @author lonyee
 *
 */
@Component
public class EmailClient {
	
	@Resource
	private JavaMailSender mailSender;
	@Value("${app.mail.username:''}")
	private String username;
	@Value("${app.mail.sign:乐檬中国}")
	private String sign;
	
	/**
	 * 发送邮件
	 */
	public void send(String sendTo, String title, String content) throws MessagingException, UnsupportedEncodingException {
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
	    messageHelper.setFrom(username, sign);
	    messageHelper.setTo(sendTo);
	    messageHelper.setSubject(title);
	    messageHelper.setText(content);
	    mailSender.send(message);
	}
}
