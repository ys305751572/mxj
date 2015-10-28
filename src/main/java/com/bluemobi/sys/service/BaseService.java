package com.bluemobi.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluemobi.sys.dao.base.BaseDao;

/**
 * 
 * <p>Title: BaseService.java</p> 
 * <p>Description: service基类</p> 
 * @author yesong  
 * @date 2014-11-5 下午04:26:29
 * @version V1.0 
 * ------------------------------------
 * 历史版本
 *
 */
@Service
public abstract class BaseService{

	@Autowired
	private BaseDao baseDao;

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	//TODO service公共方法  加入代码时请标明作者、时间、描述
	
}
