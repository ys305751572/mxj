package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UhoemErpMeasureHouseOrderServiceImpl extends BaseService {
    public static String PREFIX = UhoemErpMeasureHouseOrderServiceImpl.class.getName();

    @Transactional
    public void save(Map<String, Object> params) throws Exception {
        Map<String, Object> temp = this.getBaseDao().get(PREFIX + ".findByMember", params);
        if(temp==null || temp.size()==0) {
            params.put("create_user",params.get("member"));
            params.put("create_date",new Date());
            params.put("modify_date",new Date());
            this.getBaseDao().save(PREFIX + ".insert", params);
            insertMessages(params);
        }
    }

    private void insertMessages(Map<String, Object> params) throws Exception {
    	Map<String,Object> messageMap = new HashMap<String,Object>();
    	messageMap.put("memberId", params.get("member"));
    	messageMap.put("title", "预约量房消息");
    	messageMap.put("content", "您的预约量房已成功");
    	this.getBaseDao().save(PREFIX + ".insertMessages", messageMap);
	}

	// 根据用户查询
    public Map<String, Object> findByMember(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".findByMember", params);
    }

    // 根据用户查询
    public Map<String, Object> iBaseInfo(Map<String, Object> params) throws Exception {
    	Map<String,Object> resultMap = this.getBaseDao().get(PREFIX + ".iBaseInfo", params);
    	String gwMobile = this.getBaseDao().get(PREFIX + ".findGwMobile", resultMap);
    	if(resultMap != null) {
    		resultMap.put("gw_mobile", gwMobile == null ? "" : gwMobile);
    	}
        return resultMap;
    }
}