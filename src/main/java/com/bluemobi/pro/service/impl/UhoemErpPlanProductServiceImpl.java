package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UhoemErpPlanProductServiceImpl extends BaseService {
    public static String PREFIX = UhoemErpPlanProductServiceImpl.class.getName();

    public List<Map<String, Object>> findByPlanAndProductMainCategory(Map<String, Object> params) throws Exception {
        return this.getBaseDao().getList(PREFIX + ".findByPlanAndProductMainCategory", params);
    }

    public void deleteByPlanid(Map<String, Object> params) throws Exception {
        this.getBaseDao().delete(PREFIX + ".deleteByPlanid", params);
    }


}