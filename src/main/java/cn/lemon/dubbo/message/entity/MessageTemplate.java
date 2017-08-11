/**
 * Organization: lemon-china<br>
 * Date: 2017年8月11日上午11:33:43<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.message.entity;

import cn.lemon.dubbo.message.em.PushMethodEnum;
import cn.lemon.framework.core.BasicEntityBean;

/**
 * 消息模板表
 * 
 * @date 2017年8月11日 上午11:33:43 <br>
 * @author lonyee
 */
public class MessageTemplate extends BasicEntityBean<Long> {
	private static final long serialVersionUID = 1L;
	// fields
	/** 第三方模板中心ID **/
	private String centerId;
	/** 消息类型 **/
	private Integer messageType;
	/** 推送方式 SMS：短信 EMI：邮件 WXM：微信 IOS：ios ANR：android RMQ：rabbitMQ **/
	private PushMethodEnum pushMethod;
	/** 标题 **/
	private String title;
	/** 内容模板 **/
	private String template;
	/** 地址 带http/https为全路径地址/不带自动加上当前项目地址 **/
	private String url;
	
	public String getCenterId() {
		return centerId;
	}
	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}
	public Integer getMessageType() {
		return messageType;
	}
	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}
	public PushMethodEnum getPushMethod() {
		return pushMethod;
	}
	public void setPushMethod(PushMethodEnum pushMethod) {
		this.pushMethod = pushMethod;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
