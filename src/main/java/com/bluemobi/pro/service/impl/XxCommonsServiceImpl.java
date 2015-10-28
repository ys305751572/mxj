package com.bluemobi.pro.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluemobi.sys.service.BaseService;

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
}
