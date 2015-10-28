package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XxProductCategoryServiceImpl extends BaseService {
    public static String PREFIX = XxProductCategoryServiceImpl.class.getName();

    public List<Map<String, Object>> list(Map<String, Object> params) throws Exception {
        return this.getBaseDao().getList(PREFIX + ".find", params);
    }

    // 递归查询子id
    private List<String> ids = new ArrayList<String>();
    public void getChildIds(Map<String, Object> params) throws Exception {
        ids.add(params.get("parent")+"");
        List<Map<String, Object>> temp = this.getBaseDao().getList(PREFIX + ".findByParent", params);
        if(temp != null && temp.size() > 0){
            for (int i = 0; i < temp.size(); i++) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("parent",temp.get(i).get("id"));
                getChildIds(map);
            }
        }
    }

    // 查询所有子id
    public List<String> allChildIdList(Map<String, Object> params) throws Exception {
        ids = new ArrayList<String>();
        getChildIds(params);
        return ids;
    }



}