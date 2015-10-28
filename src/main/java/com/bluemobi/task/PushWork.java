package com.bluemobi.task;

import com.bluemobi.push.JPush;
import com.bluemobi.utils.TaskWork;

public class PushWork extends TaskWork{

	private String date;
	
	private String registrationId;
	private String msgContent;
	
	public PushWork(String registrationId,String msgContent) {
		this.registrationId = registrationId;
		this.msgContent = msgContent;
	}
	
	@Override
	public void execute() {
		push();
	}

	// 推送消息
	private void push() {
		JPush.push(registrationId, msgContent);
	}

	public String getDate() {
		return date;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
}
