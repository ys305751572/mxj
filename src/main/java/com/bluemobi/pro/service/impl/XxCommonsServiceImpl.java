package com.bluemobi.pro.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluemobi.sys.service.BaseService;
import com.bluemobi.utils.DateUtils;

@Service
public class XxCommonsServiceImpl extends BaseService{

	public static final String PRIFIX = XxCommonsServiceImpl.class.getName();
	
	// 根据区ID查询街道
	public List<Map<String,Object>> findStreet(Map<String,Object> params) throws Exception {
		return this.getBaseDao().getList(PRIFIX + ".findStreet", params);
	}
	
	// 查询首页图片
	public List<Map<String,Object>> findIndexImages() throws Exception {
		return this.getBaseDao().getList(PRIFIX + ".findIndexImages");
	}
	
	/**
	 * 未读消息数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public int indexMessageCount(Map<String,Object> params) throws Exception {
		int countNews = countNews(params);
		int countMessages = countMessages(params);
		return (countNews + countMessages);
	}
	
	// 计算消息的未读数
	private Integer countMessages(Map<String, Object> params) throws Exception {
		return this.getBaseDao().get(PRIFIX + ".countMessages", params);
	}

	// 计算通知的未读数
	private Integer countNews(Map<String, Object> params) throws Exception {
		return this.getBaseDao().get(PRIFIX + ".countNews", params);
	}
	
	// =============================test===========================
	public void insertMessage() throws Exception {
		Map<String,Object> messageMap = new HashMap<String,Object>();
    	messageMap.put("memberId", "1");
    	messageMap.put("create_time", DateUtils.getCurrentTime());
    	messageMap.put("title", "预约量房消息");
    	messageMap.put("content", "您的预约量房已成功");
    	messageMap.put("isread", 0);
    	this.getBaseDao().save("com.bluemobi.pro.service.impl.UhoemErpMeasureHouseOrderServiceImpl.insertMessages", messageMap);
    	
    	Map<String,Object> messageStatusMap = new HashMap<String,Object>();
    	messageStatusMap.put("messageId", messageMap.get("id"));
    	messageStatusMap.put("memberId", 1);
    	this.getBaseDao().save("com.bluemobi.pro.service.impl.UhoemErpMeasureHouseOrderServiceImpl.insertMessagesStatus", messageStatusMap);
	}
}
