package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UhoemMessagesServiceImpl extends BaseService {
    public static String PREFIX = UhoemMessagesServiceImpl.class.getName();

    public Page<List<Map<String, Object>>> page(Map<String, Object> params, int pageNum,
            int pageSize) {
        return this.getBaseDao().page(PREFIX + ".page", params, pageNum, pageSize);
    }

    public void read(Map<String,Object> params) throws Exception {
    	
    	String[] ids = params.get("ids").toString().split(",");
    	for (String id : ids) {
    		Map<String,Object> idMap = new HashMap<String,Object>();
    		idMap.put("id", id);
			this.getBaseDao().update(PREFIX + ".modifyIsRead", idMap);
		}
    }
}