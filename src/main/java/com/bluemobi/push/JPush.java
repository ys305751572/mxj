package com.bluemobi.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 
 * 极光推送接口
 * @author yesong
 *
 */
public class JPush {
	protected static final Logger LOG = LoggerFactory.getLogger(JPush.class);

	private static final String appKey = "d41efbb5b0337435da3ce65a";
	private static final String masterSecret = "61da5d1061755be83af21fe2";

	/**
	 * 测试
	public static final String MSG_CONTENT = "来自YS的demo推送测试";
	public static final String REGISTRATION_AD_ID = "050d20dde30";
	public static final String REGISTRATION_IOS_ID = "050d20dde30";
	 */

	public static void main(String[] args) {
//		testSendPush();
	}

	public static void push(final String registration_id,final String msg_content) {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		PushPayload payload = buildPushObject_all_alias_alert(registration_id,msg_content);
		try {
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);

		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
		}
	}

	public static PushPayload buildPushObject_all_alias_alert(final String registration_id,final String msg_content ) {
		return PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.registrationId(registration_id))
				.setNotification(Notification.alert(msg_content)).build();
	}
}
