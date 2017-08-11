package cn.lemon.dubbo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * dubbo消息服务，包含短信/邮件/微信/推送/rabbitMQ等
 * 
 * @date 2017年8月9日 下午7:18:54 <br>
 * @author lonyee
 */
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan({"cn.lemon.dubbo.message"})
public class MessageApplication {
	private static Logger logger = LoggerFactory.getLogger(MessageApplication.class);

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(MessageApplication.class);
		springApplication.setWebEnvironment(false);
		springApplication.run(args);
		logger.info("SpringBoot lemon-message Start Success");
	}

	@Scheduled(cron = "0 0 23 30 DEC ?")
	public void refreshApplication() {
		// 利用定时任务保障主线程存活
		logger.info("SpringBoot scheduling Success");
	}
}
