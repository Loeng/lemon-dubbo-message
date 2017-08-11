/**
 * Organization: lemon-china<br>
 * Date: 2017年8月11日上午11:33:43<br>
 * Copyright (c) 2017, lonyee@live.com All Rights Reserved.
 *
 */
package cn.lemon.dubbo.message.entity;

import java.util.Date;

import cn.lemon.dubbo.message.em.PushMethodEnum;
import cn.lemon.framework.core.BasicEntityBean;

import com.fasterxml.jackson.annotation.JsonFormat;

/**************************
 * m_message_record
 * 消息发送记录表
 * @author lonyee
 * @date 2017-08-11 11:33:43
 * 
 **************************/
public class MessageRecord extends BasicEntityBean<Long> {
	private static final long serialVersionUID = 1L;
	//fields
	/** 消息模板ID **/
	private Integer templateId;
	/** 消息内容 **/
	private String message;
	/** 地址 **/
	private String url;
	/** 接收人员 **/
	private Long userId;
	/** 扩展信息，手机号、openId等 **/
	private String ext;
	/** 推送方式 SMS：短信 EMI：邮件 WXM：微信 IOS：ios ANR：android RMQ：rabbitMQ **/
	private PushMethodEnum pushMethod;
	/** 发送次数 0 待发送 1-3 发送失败次数 9 发送成功 **/
	private Integer sendTimes;
	/** 预约发送时间 空为立即发送 **/
	private Date scheduleTime;
	
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public PushMethodEnum getPushMethod() {
		return pushMethod;
	}
	public void setPushMethod(PushMethodEnum pushMethod) {
		this.pushMethod = pushMethod;
	}
	public Integer getSendTimes() {
		return sendTimes;
	}
	public void setSendTimes(Integer sendTimes) {
		this.sendTimes = sendTimes;
	}
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
}
