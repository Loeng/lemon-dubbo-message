/**
* Organization: lemon-china <br>
* Date: 2017-08-12 14:08:59 <br>
* Automatically Generate By EasyCodeGenerine <br>
* Copyright (c) 2017 All Rights Reserved.
*/
package cn.lemon.dubbo.message.entity;

import java.util.Date;

import cn.lemon.framework.core.BasicEntityBean;

import com.fasterxml.jackson.annotation.JsonFormat;

/**************************
 * MessageRecord
 * 消息发送记录表
 * @author lonyee
 * @date 2017-08-12 14:08:59
 * 
 **************************/
public class MessageRecord extends BasicEntityBean<Long> {
	private static final long serialVersionUID = 1L;
	//fields
	/** 模板ID **/
	private Long templateId;
	/** 第三方模板中心ID **/
	private String centerId;
	/** 推送方式 INL: 站内信 SMS：短信 EMI：邮件 WXM：微信 IOS：ios ANR：android RMQ：rabbitMQ **/
	private String pushMethod;
	/** 发送地址  手机号、openId等 **/
	private String receiver;
	/** 标题 **/
	private String title;
	/** 名称 **/
	private String message;
	/** 访问地址 **/
	private String url;
	/** 接收人员ID **/
	private Long receiverId;
	/** 发送次数 0 待发送 1-3 发送失败次数 9 发送成功 **/
	private Integer sendTimes;
	/** 预约发送时间 空为立即发送 **/
	private Date scheduleTime;


	public void setTemplateId(Long templateId) {
		this.templateId=templateId;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public String getCenterId() {
		return centerId;
	}
	public void setCenterId(String centerId) {
		this.centerId = centerId;
	}
	public void setPushMethod(String pushMethod) {
		this.pushMethod=pushMethod;
	}
	public String getPushMethod() {
		return pushMethod;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setMessage(String message) {
		this.message=message;
	}
	public String getMessage() {
		return message;
	}
	public void setUrl(String url) {
		this.url=url;
	}
	public String getUrl() {
		return url;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId=receiverId;
	}
	public Long getReceiverId() {
		return receiverId;
	}
	public void setSendTimes(Integer sendTimes) {
		this.sendTimes=sendTimes;
	}
	public Integer getSendTimes() {
		return sendTimes;
	}
	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime=scheduleTime;
	}
	@JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
	public Date getScheduleTime() {
		return scheduleTime;
	}
	public void setReceiver(String receiver) {
		this.receiver=receiver;
	}
	public String getReceiver() {
		return receiver;
	}
}
