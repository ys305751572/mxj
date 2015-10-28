package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UhoemErpCoordinateServiceImpl extends BaseService {
    public static String PREFIX = UhoemErpCoordinateServiceImpl.class.getName();

    public Map<String, Object> findOneById(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".findOneById", params);
    }


}