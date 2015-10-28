package com.bluemobi.pro.entity;

// 施工人员
public class BaseUserVO {

	private Long UserId;
	private String RealName;
	private String UserAvatar;
	
	public Long getUserId() {
		return UserId;
	}
	public void setUserId(Long userId) {
		UserId = userId;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public String getUserAvatar() {
		return UserAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		UserAvatar = userAvatar;
	}
}
