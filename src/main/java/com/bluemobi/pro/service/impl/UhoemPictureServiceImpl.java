package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UhoemPictureServiceImpl extends BaseService {
    public static String PREFIX = UhoemPictureServiceImpl.class.getName();

    public List<Map<String, Object>> homepageAd(Map<String, Object> params) throws Exception {
        return this.getBaseDao().getList(PREFIX + ".homepageAd", params);
    }


}