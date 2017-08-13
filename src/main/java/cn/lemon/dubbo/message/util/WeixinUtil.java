package cn.lemon.dubbo.message.util;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.lemon.dubbo.message.cache.WechatTokenCache;
import cn.lemon.framework.http.HttpClientHandler;
import cn.lemon.framework.utils.JsonUtil;

/**
 * 微信模板消息
 * @author lonyee
 */
@Component
public class WeixinUtil {
	private static Logger logger = LoggerFactory.getLogger(WeixinUtil.class);
	private String appId;
	private String appSecret;
	
	@Resource
	private WechatTokenCache wechatTokenCache;

	/**
	 * 获取全局授权码access_token
	 */
	public String getAccessToken() throws Exception  {
		String accessToken = wechatTokenCache.getWechatToken();
		if (accessToken!= null) {
			return accessToken;
		}
		// redis没有找到去获取授权码
		String tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appId, appSecret);
		String responseText = HttpClientHandler.get(tokenUrl);
		logger.debug("获取普通access_token：{}", responseText);
		WxAccessToken wechatAccessToken = JsonUtil.readValue(responseText, WxAccessToken.class);
		accessToken = wechatAccessToken.getAccess_token();
		if (accessToken==null) {
			logger.warn("授权信息AccessToken获取失败");
			throw new Exception("授权信息AccessToken获取失败");
		}
		wechatTokenCache.setWechatToken(accessToken);
		return accessToken;
	}
	
	/**
	 * 发送模版消息
	 * @param content 模版消息
	 */
	public String sendTemplateMessage(String content) throws Exception {
		return sendTemplateMessage(getAccessToken(), content);
	}
	
	/**
	 * 发送模版消息
	 * @param accessToken 全局授权码
	 * @param content 模版消息
	 */
	public String sendTemplateMessage(String accessToken, String content) throws Exception {
		String templateUrl = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);
		logger.debug("发送模版消息：templateText:{}", content);
		String responseText = HttpClientHandler.post(templateUrl, content);
		//logger.debug("模版消息结果：return: {}", responseText);
		WxTemplateInfo wxTemplateInfo = JsonUtil.readValue(responseText, WxTemplateInfo.class);
		if ("0".equals(wxTemplateInfo.getErrcode())) {
			return wxTemplateInfo.getMsgid();
		}
		logger.warn("模版消息发送失败");
		throw new Exception(responseText);
	}
	
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	/**
	 * 通过code获取access_token 请求成功的实体
	 * @author lonyee
	 */
	class WxAccessToken {
		/** 错误编号  */
		private Integer	errcode;
		/** 错误提示消息 */
		private String	errmsg;
		/** 接口调用凭证  */
		private String	access_token;
		/** access_token接口调用凭证超时时间，单位（秒） */
		private Integer	expires_in;
		/**  用户刷新access_token */
		private String	refresh_token;
		/**  授权用户唯一标识  */
		private String	openid;
		/**   用户授权的作用域，使用逗号（,）分隔  */
		private String	scope;
		/** 微信用户全局唯一ID */
		private String	unionid;

		public Integer getErrcode() {
			return errcode;
		}
		public void setErrcode(Integer errcode) {
			this.errcode = errcode;
		}
		public String getErrmsg() {
			return errmsg;
		}
		public void setErrmsg(String errmsg) {
			this.errmsg = errmsg;
		}
		public String getAccess_token() {
			return access_token;
		}
		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}
		public Integer getExpires_in() {
			return expires_in;
		}
		public void setExpires_in(Integer expires_in) {
			this.expires_in = expires_in;
		}
		public String getRefresh_token() {
			return refresh_token;
		}
		public void setRefresh_token(String refresh_token) {
			this.refresh_token = refresh_token;
		}
		public String getOpenid() {
			return openid;
		}
		public void setOpenid(String openid) {
			this.openid = openid;
		}
		public String getScope() {
			return scope;
		}
		public void setScope(String scope) {
			this.scope = scope;
		}
		public String getUnionid() {
			return unionid;
		}
		public void setUnionid(String unionid) {
			this.unionid = unionid;
		}
	}
	/**
	 * 模板消息
	 * @author lonyee
	 */
	class WxTemplateInfo {
		/** 消息ID **/
		private String msgid;
		/** 错误码 **/
		private String errcode;
		/** 错误信息 **/
		private String errmsg;
		
		public String getMsgid() {
			return msgid;
		}
		public void setMsgid(String msgid) {
			this.msgid = msgid;
		}
		public String getErrcode() {
			return errcode;
		}
		public void setErrcode(String errcode) {
			this.errcode = errcode;
		}
		public String getErrmsg() {
			return errmsg;
		}
		public void setErrmsg(String errmsg) {
			this.errmsg = errmsg;
		}
	}
}
