package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class XxDecorGroupProductServiceImpl extends BaseService {
    public static String PREFIX = XxDecorGroupProductServiceImpl.class.getName();

    public List<Map<String, Object>> list(Map<String, Object> params) throws Exception {
        return this.getBaseDao().getList(PREFIX + ".find", params);
    }


}