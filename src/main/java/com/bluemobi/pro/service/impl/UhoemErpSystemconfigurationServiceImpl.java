package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UhoemErpSystemconfigurationServiceImpl extends BaseService {
    public static String PREFIX = UhoemErpSystemconfigurationServiceImpl.class.getName();

    public Map<String, Object> basicYzPrice(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".basicYzPrice", params);
    }


}