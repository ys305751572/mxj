package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UhoemErpDictionaryServiceImpl extends BaseService {
    public static String PREFIX = UhoemErpDictionaryServiceImpl.class.getName();

    public List<Map<String, Object>> houseType(Map<String, Object> params) throws Exception {
        params.put("TypeId","2007");
        params.put("IsEnable","1");
        params.put("IsDelete","0");
        return this.getBaseDao().getList(PREFIX + ".houseType", params);
    }

    public Map<String, Object> getByHourseType(String hourseType) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("DicValue",hourseType);
        params.put("TypeId","2007");
        params.put("IsEnable","1");
        params.put("IsDelete","0");
        return this.getBaseDao().get(PREFIX + ".getByHourseType", params);
    }


}