package cn.lemon.dubbo.message.cache;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class WechatTokenCache {

	private String wechatToken = "WECHAT:ACCESSTOKEN";
	private String jsapiTicket = "WECHAT:JSAPITICKET";
	private long expireTime = 110*60L; //微信过期时间为120分钟, 设置110分钟过期
	@Resource
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 设置微信全局授权码 accessToken
	 */
	public String setWechatToken(final String accessToken) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		operations.set(wechatToken, accessToken, expireTime, TimeUnit.SECONDS);
		return accessToken;
	}
	
	/**
	 * 设置微信全局授权码 accessToken
	 */
	public String getWechatToken() {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		return operations.get(wechatToken);
	}
	
	/**
	 * 设置jsapi_ticket授权码
	 */
	public String setJsapiTicket(final String jsTicket) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		operations.set(jsapiTicket, jsTicket, expireTime, TimeUnit.SECONDS);
		return jsTicket;
	}
	
	/**
	 * 获取jsapi_ticket授权码
	 */
	public String getJsapiTicket() {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		return operations.get(jsapiTicket);
	}
	
}
