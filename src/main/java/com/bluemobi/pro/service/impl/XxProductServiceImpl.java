package com.bluemobi.pro.service.impl;

import com.bluemobi.sys.page.Page;
import com.bluemobi.sys.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XxProductServiceImpl extends BaseService {
    public static String PREFIX = XxProductServiceImpl.class.getName();

    @Autowired
    private XxProductCategoryServiceImpl xxProductCategoryServiceImpl;


    public Page<List<Map<String, Object>>> page(Map<String, Object> params, int pageNum,
            int pageSize) throws Exception {
        String product_category = (String) params.get("product_category");
        Integer searchType = 0;
        if(StringUtils.isEmpty(product_category)){
            searchType = 1;
        }
        else {
            if("3".equals(product_category) || "4".equals(product_category) || "5".equals(product_category)){
                searchType = 2;
            }
            else {
                searchType = 3;
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("parent",params.get("product_category"));
                List<String> ids = xxProductCategoryServiceImpl.allChildIdList(map);
                params.put("ids",ids);
            }
        }
        params.put("searchType",searchType);
        if(StringUtils.isNotEmpty((String) params.get("price"))){
            Integer temp = Integer.parseInt((String) params.get("price"));
            if(temp == 1){
                params.put("startPrice","0");
                params.put("endPrice","999");
            }
            if(temp == 2){
                params.put("startPrice","1000");
                params.put("endPrice","2999");
            }
            if(temp == 3){
                params.put("startPrice","3000");
                params.put("endPrice","4999");
            }
            if(temp == 4){
                params.put("startPrice","5000");
                params.put("endPrice","9999");
            }
            if(temp == 5){
                params.put("startPrice","10000");
            }
        }
        return this.getBaseDao().page(PREFIX + ".page", params, pageNum, pageSize);
    }

    public Page<List<Map<String, Object>>> pageHot(Map<String, Object> params, int pageNum,
                                                int pageSize) {
        return this.getBaseDao().page(PREFIX + ".pageHot", params, pageNum, pageSize);
    }

    public Page<List<Map<String, Object>>> sameKind(Map<String, Object> params, int pageNum,
                                                   int pageSize) {
        return this.getBaseDao().page(PREFIX + ".sameKind", params, pageNum, pageSize);
    }

    public Map<String, Object> findOne(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".findOne", params);
    }

    public Map<String, Object> iDetail(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".iDetail", params);
    }

    public Map<String, Object> getById(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".getById", params);
    }

    public Map<String, Object> findOneById(Map<String, Object> params) throws Exception {
        return this.getBaseDao().get(PREFIX + ".findOneById", params);
    }

    // 商品收藏量+1
    public int productFavoriteCountAddOne(Map<String, Object> params) throws Exception {
        params = getById(params);
        Integer favorite_count = Integer.parseInt((String)params.get("favorite_count"));
        favorite_count = favorite_count+1;
        Map<String,Object> product = new HashMap<>();
        product.put("id",params.get("id"));
        product.put("favorite_count",favorite_count);
        return this.getBaseDao().update(PREFIX + ".updateFavoriteCount", product);
    }

    // 商品收藏量-1
    public int productFavoriteCountCutOne(Map<String, Object> params) throws Exception {
        params = getById(params);
        Integer favorite_count = Integer.parseInt((String)params.get("favorite_count"));
        favorite_count = favorite_count-1;
        if(favorite_count<0){
            favorite_count=0;
        }
        Map<String,Object> product = new HashMap<>();
        product.put("id",params.get("id"));
        product.put("favorite_count",favorite_count);
        return this.getBaseDao().update(PREFIX + ".updateFavoriteCount", product);
    }


}