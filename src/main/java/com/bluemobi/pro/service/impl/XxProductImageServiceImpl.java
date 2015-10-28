package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import javax.jws.Oneway;
import java.util.List;
import java.util.Map;

@Service
public class XxProductImageServiceImpl extends BaseService {
    public static String PREFIX = XxProductImageServiceImpl.class.getName();

    public List<Map<String,Object>> findProductImage(Map<String, Object> params) throws Exception {
        return this.getBaseDao().getList(PREFIX + ".findProductImage", params);
    }


}