/**
 * Organization: lemon-china<br>
 * Date: 2017年8月11日下午4:14:59<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.message.em;

/**
 * 推送方式
 * @date 2017年8月11日 下午4:14:59 <br>
 * @author lonyee
 */
public enum PushMethodEnum {
	/** 短信 **/
	SMS("SMS"),
	/** 邮件 **/ 
	EMAIL("EMI"),
	/** 微信 **/
	WECHAT("WXM"),
	/** IOS **/
	IOS("IOS"),
	/** android **/
	ANDROID("ANR"),
	/** rabbitMQ **/
	RABBITMQ("RMQ");
	
	
	private String method;
	PushMethodEnum(String method) {
		this.method = method;
	}
	
	public String toString() {
		return this.method;
	}
}
