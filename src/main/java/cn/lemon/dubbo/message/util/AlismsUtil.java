package cn.lemon.dubbo.message.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 阿里大于短信
 * @author lonyee
 */
@Component
public class AlismsUtil {
	private static Logger logger = LoggerFactory.getLogger(AlismsUtil.class);
	private String url;
	private String appKey;
	private String appSecret;
	private String sign;
	
	/**
	 * 发送短信
	 */
	public String send(String mobiles, String templateId, String jsonContent) throws Exception {
		TaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(sign);
		req.setSmsParamString(jsonContent);
		req.setRecNum(mobiles);
		req.setSmsTemplateCode(templateId);
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		if (!rsp.isSuccess()) {
			logger.error("send sms fail. return code: {}, value：{}. please check service return value!", rsp.getErrorCode(), rsp.getMsg());
			throw new Exception("send sms fail. res=" + rsp.getErrorCode());
		}
		return rsp.getMsg();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
