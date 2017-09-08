/**
* Organization: lemon-china <br>
* Date: 2017-08-12 14:08:59 <br>
* Automatically Generate By EasyCodeGenerine <br>
* Copyright (c) 2017 All Rights Reserved.
*/
package cn.lemon.dubbo.message.entity;

import cn.lemon.framework.core.BasicEntityBean;


/**************************
 * MessageTemplate
 * 消息模板表
 * @author lonyee
 * @date 2017-08-12 14:08:59
 * 
 **************************/
public class MessageTemplate extends BasicEntityBean<Long> {
	private static final long serialVersionUID = 1L;
	//fields
	/** 第三方模板中心ID **/
	private String centerId;
	/** 消息类型 **/
	private String messageType;
	/** 推送方式 INL: 站内信 SMS：短信 EMI：邮件 WXM：微信 IOS：ios ANR：android RMQ：rabbitMQ **/
	private String pushMethod;
	/** 标题 **/
	private String title;
	/** 内容模板 **/
	private String template;
	/** 地址 带http/https为全路径地址/不带自动加上当前项目地址 **/
	private String url;


	public void setCenterId(String centerId) {
		this.centerId=centerId;
	}
	public String getCenterId() {
		return centerId;
	}
	public void setMessageType(String messageType) {
		this.messageType=messageType;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setPushMethod(String pushMethod) {
		this.pushMethod=pushMethod;
	}
	public String getPushMethod() {
		return pushMethod;
	}
	public void setTitle(String title) {
		this.title=title;
	}
	public String getTitle() {
		return title;
	}
	public void setTemplate(String template) {
		this.template=template;
	}
	public String getTemplate() {
		return template;
	}
	public void setUrl(String url) {
		this.url=url;
	}
	public String getUrl() {
		return url;
	}
}
